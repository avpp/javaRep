/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Экземпляр данного класса управляет взаимодействием между клиентами
 * @author Alexey
 */
public abstract class Admin {
    private Semaphore sem;
    /**
     * Класс Messge хранит в себе текстовое сообщение и отправителя
     */
    public class Message {
        public ServPlayer source;
        public String message;
        public Message(ServPlayer s, String m)
        {
            source = s;
            message = m;
        }
    }
    /**
     * Данный класс реализует метод {@link  Runnable}
     * для разбора пришедших сообщений
     */
    private class GatheringMessages implements Runnable {
        @Override
        public void run() {
            long pause = 100;
            while (true)
            {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (messages.size() > 0)
                {
                    PauseAdding();
                    Message m = messages.removeFirst();
                    ContinueAdding();
                    gatheringMessage(m);
                }
            }
        }
    }
    /**
     * Список сообщений
     */
    private LinkedList<Message> messages;
    /**
     * экземпляр класса {@link Server}, который использует администратор
     */
    protected Server server;
    private Thread servTh, gameTh, mesTh;
    /**
     * Экземпляр класса {@link Dealer}, который используется для игры
     */
    protected Dealer dealer;
    /**
     * Конструктор класса. Инициализирует переменные.
     */
    public Admin()
    {
        server = new Server(this);
        servTh = new Thread(server);
        servTh.setName("Server thread");
        sem = new Semaphore(1);
        messages = new LinkedList<Message>();
    }
    /**
     * Запуск сервера
     */
    public void startServer()
    {
        servTh.start();
    }
    
    /**
     * Метод для синхронизации. Приостанавливает добавление сообщений
     */
    public void PauseAdding()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Метод для синхронизации. Возобновляет возможность добавления сообщений
     */
    public void ContinueAdding()
    {
        sem.release();
    }
    /**
     * Добавление сообщений
     * @param sp источник сообщения
     * @param mesg само сообщение
     */
    public void AddMessage(ServPlayer sp, String mesg)
    {
        try {
            sem.acquire();
            messages.add(new Message(sp, mesg));
            sem.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Удаление сообщений от источника p из списка сообщений
     * @param p источник сообщений.
     */
    public void RemoveMessagesByPlayer(ServPlayer p)
    {
        PauseAdding();
        LinkedList<Message> delM = new LinkedList<Message>();
        for (Message m : messages)
        {
            if (p == m.source)
            {
                delM.add(m);
            }
        }
        messages.removeAll(delM);
        ContinueAdding();
    }
    /**
     * Возвращает список игроков, которые подключены к серверу
     * @return Список имён игроков
     */
    public LinkedList<String> getPlayerList()
    {
        LinkedList<String> ans = new LinkedList<String>();
        for(ServPlayer tmpPl : server.players)
        {
            ans.add(tmpPl.name);
        }
        return ans;
    }
    /**
     * Метод, создающий диллера
     */
    public abstract void createDealer();
    /**
     * Метод проверки клиента на возможность участия в данной игре
     * @param player Пороверяемый клиент
     * @return возвращает true, если игрок может принять участие, иначе false
     */
    public abstract Boolean isPlayerOK(ServPlayer player);
    /**
     * Метод по разбору одного сообщение
     * @param m сообщение для разбора
     */
    public abstract void gatheringMessage(Message m);
    /**
     * Запуск Визуальной части сервера
     */
    public abstract void runInterface();
    /**
     * Метод, который вызывается при изменении списка игроков
     */
    public abstract void playersChanged();
    /**
     * Список игроков, зарезервированных на игру
     */
    protected LinkedList<ServPlayer> reservedPlayers = new LinkedList<ServPlayer>();
    /**
     * Получение имён игроков, зарезервированных на игру
     * @return список имён игроков
     */
    public LinkedList<String> getReservedPlayerList()
    {
        LinkedList<String> ans = new LinkedList<String>();
        for (ServPlayer sp : reservedPlayers)
            ans.add(sp.name);
        return ans;
    }
    /**
     * Резервирование игрока
     * @param player игрок, который резервируется
     * @return возвращает true, если игрок может принять участие и ещё не содержится в списке зарезервированных игроков, иначе false
     */
    public Boolean addPlayer(ServPlayer player)
    {
        if (isPlayerOK(player))
            if (reservedPlayers.indexOf(player) == -1)
                reservedPlayers.add(player);
            else
                return false;
        else
            return false;
        return true;
    }
    /**
     * Добавление игрока по  индексу. (аналогично {@link addPlayer(ServPlayer player)})
     * @param index индекс игрока
     * @return аналогично {@link addPlayer(ServPlayer player)}
     */
    public Boolean addPlayer(int index)
    {
        return addPlayer(server.players.get(index));
    }
    /**
     * Удаления игрока из списка зарезервированных
     * @param player игрок для удаления
     * @return возвращает true, если игрок успешно удалён
     */
    public Boolean removePlayer(ServPlayer player)
    {
        return reservedPlayers.remove(player);
    }
    /**
     * Удаление игрока из списка зарезервированных по индексу
     * @param index индекс игрока
     * @return возвращает true, если игрок успешно удалён
     */
    public Boolean removePlayer(int index)
    {
        return removePlayer(reservedPlayers.get(index));
    }
    /**
     * Начало игры.
     * Добавлет зарезервированных игроков к диллеру и запускает игру.
     */
    public void startGame()
    {
        for (ServPlayer p : reservedPlayers)
            dealer.players.add(new GamePlayer(p));
        dealer.initGame();
        gameTh = new Thread(dealer);
        gameTh.setName("Dealer thread");
        gameTh.start();
    }
    /**
     * Остановка игры
     */
    public void stopGame()
    {
        //gameTh.interrupt();
        gameTh.stop();
    }
    /**
     * Начало разбора сообщений
     */
    public void startGathering()
    {
        mesTh = new Thread(new GatheringMessages());
        mesTh.setName("Message gathering thread");
        mesTh.start();
    }
    /**
     * Остановка работы сервера и разбора сообщений
     */
    public void stopServer()
    {
        if (servTh.isAlive())
            servTh.interrupt();
        if (mesTh.isAlive())
            mesTh.interrupt();
    }
    /**
     * Ожидание окончания игры
     * @return возвращает true, если игра закончилась нормально
     */
    public Boolean waitEndGame()
    {
        try {
            if (gameTh.isAlive())
                gameTh.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
