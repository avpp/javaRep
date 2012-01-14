/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, принимающий запросы на подключение
 * @author Alexey
 */
public class Server implements Runnable{
    /**
     * класс, добавляющий клиентов в список клиентов
     */
    private class AddClient implements Runnable {
        private Socket s;
        public AddClient(Socket socket)
        {
            s = socket;
        }
        @Override
        public void run() {
            try {
                ServPlayer p;
                p = new ServPlayer(s, a);
                sem.acquire();
                /*Boolean check = true;
                while (check)
                {
                    check = false;
                    for (int i = 0; i < players.size() && !check; i++)
                    {
                        check = players.get(i).name.equals(p.name);
                    }
                    if (check)
                    {
                        sem.release();
                        //p = new ServPlayer(s, a);
                        //p.changeName();
                        sem.acquire();
                    }
                }*/
                players.add(p);
                a.playersChanged();
                sem.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private ServerSocket ssocket;
    private Admin a;
    public Boolean work;
    // Socket csockets[];
    /**
     * список клиетов, подключённых к серверу
     */
    public LinkedList<ServPlayer> players;
    /**
     * Конструктор, инициализирующий сервер
     * @param admin Администратор, управляющий работой сервера
     */
    public Server(Admin admin)
    {
        a = admin;
        /*
        try {
            ssocket = new ServerSocket(15147, 100, null);
            System.out.println(ssocket.getLocalSocketAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
         * При инициализации сервера не должно происходить
         * открытие сокета
         * Сокет должен открываться при
         * непосредственном (!) запуске сервера
         */
        //csockets = new Socket[0];
        players = new LinkedList<ServPlayer>();
        work = true;
        sem = new Semaphore(1);
    }
    private Semaphore sem;
    /**
     * Для синхронизации. Продолжение работы сервера (добавление новых клиентов)
     */
    public void ContinueWork()
    {
        sem.release();
    }
    /**
     * Для синхронизации. Приостановка работы сервера (добавления новых клиентов)
     */
    public void PauseWork()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Метод, вызываемый при запуске потока сервер (реализация класса {@link Runnable})
     */
    @Override
    public void run() {
        try {
            ssocket = new ServerSocket(15147, 100, null);
            System.out.println(ssocket.getLocalSocketAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            
            return;
        }
        
        while (work)
        {
            try {
                Socket s = ssocket.accept();
                Thread th = new Thread(new AddClient(s));
                th.setName("Request name by ".concat(s.getInetAddress().toString()).concat(":").concat(String.valueOf(s.getPort())));
                th.start();
                /*System.out.print("Socket... ");
                try {
                    sem.acquire();
                    System.out.println("accepted");
                    players.add(new ServPlayer(s, a));
                    System.out.println("add client with name " + players.getLast().name);
                    sem.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                /*
                Socket tmp[] = new Socket[csockets.length + 1];
                System.arraycopy(csockets, 0, tmp, 0, csockets.length);
                tmp[csockets.length] = s;
                csockets = tmp;*/
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            ssocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
