/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Экземпляр данного класса осуществляет связь с удалённым клиентом
 * @author Alexey
 */
public class ServPlayer {
    /**
     * Класс для прослушивания входящих сообщений
     */
    private class Listen implements Runnable {
        private Socket s;
        public Listen(Socket socket)
        {
            s = socket;
        }
        @Override
        public void run() {
            while (!s.isClosed())
            {
                try {
                    try {
                        Thread.sleep((long)500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (s.getInputStream().available() > 0)
                    {
                        byte b[] = new byte[s.getInputStream().available()];
                        s.getInputStream().read(b);
                        String mes = new String(b);
                        LinkedList<String> submes = new LinkedList(java.util.Arrays.asList(mes.split("@")));
                        /*for (String curString : submes)
                        {
                            curString += "\0";
                        }*/
                        while (submes.size() > 0)
                        {
                            acceptMessage(submes.removeFirst());
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private Thread l_th;
    private Socket s;
    public String name;
//    public LinkedList<Card> cards;
    private String answer;
    private Semaphore sem;
    private Admin myAdmin;
    private GamePlayer gp;
    /**
     * Конструктор данного класс, создаёт экземпляр данного класса.
     * @param socket сокет, с помощью которого осуществляется связь с клиентом
     * @param admin экземпляр класса {@link Admin}, который курирует работу данного объекта
     * @throws InterruptedException
     */
    public ServPlayer(Socket socket, Admin admin) throws InterruptedException
    {
        myAdmin = admin;
        gp = null;
        s = socket;
        try {
            Boolean check = true;
            do
            {
                s.getOutputStream().write("name".getBytes());
                while(s.getInputStream().available() <= 0)
                {
                    Thread.sleep((long)100);
                    if (s.isInputShutdown())
                        throw new InterruptedException("client don't has name");
                }
                byte b[] = new byte [s.getInputStream().available()];
                s.getInputStream().read(b);
                name = (new String(b)).split("@")[0];
                LinkedList<String> ReservedNames = myAdmin.getPlayerList();
                check = false;
                for (int i = 0; i < ReservedNames.size() && !check; i++)
                {
                    check = ReservedNames.get(i).equals(name);
                }
            } while (check);
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        l_th = new Thread(new Listen(socket));
        l_th.setName("Listen player ".concat(name));
        l_th.start();
        sem = new Semaphore(0);
    }
    /**
     * Связь даного экземпляра с экземпляром класса {@link GamePlayer}
     * @param p экземпляр класса {@link GamePlayer}, с которым надо осуществить связь
     */
    public void setGamePlayer(GamePlayer p)
    {
        gp = p;
    }
    /**
     * Получение связанного с данным классом объекта {@link GamePlayer}
     * @return объекта класса {@link GamePlayer}
     */
    public GamePlayer getGamePlayer()
    {
        return gp;
    }
    /**
     * Завершение соединения
     */
    public void closeSocket()
    {
        try {
            if (!s.isClosed())
                s.close();
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    Boolean mayMakeTurn = true;
    /**
     * Установка ответа на запрос хода
     * @param s текст ответа
     */
    public synchronized void setAnswer(String s)
    {
        if (sem.availablePermits() != 0 || !mayMakeTurn)
            return;
        answer = s;
        sem.release();
    }
    /**
     * получение ответа на запрос хода
     * @return текст ответа
     */
    private String getAnswer()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer;
    }
    /**
     * Запрос хода. Посылает сообщение с текущей игровой ситуацией и ожидает ответа на это сообщение
     * @param situation игровая ситуация
     * @return ответ на запрос
     */
    public String move(String situation)
    {
        String ans = "";
        sendMessage(situation);
        mayMakeTurn = true;
        ans = getAnswer();
        mayMakeTurn = false;
        return ans;
    }
    /**
     * Добавление сообщения, пришедшего от имени этого игрока в список обработки сообщений администратора
     * @param message 
     */
    public void acceptMessage(String message)
    {
        myAdmin.AddMessage(this, message);
    }
    /**
     * Отправка сообщения удалённому клиенту
     * @param message сообщение
     */
    public void sendMessage(String message)
    {
        if (s.isClosed())
            return;
        try {
            s.getOutputStream().write(message.getBytes());
        } catch (SocketException ex)
        {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
            try {
                s.close();
                myAdmin.AddMessage(this, "exit/");
            } catch (IOException ex1) {
                Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
