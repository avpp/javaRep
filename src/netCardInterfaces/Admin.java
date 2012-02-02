/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Экземпляр данного класса управляет взаимодействием между клиентами
 * @author Alexey
 */
public abstract class Admin {

    /**
     * Данный класс реализует метод {@link  Runnable}
     * для разбора пришедших сообщений
     */
    private class GatheringMessages implements Runnable {
        @Override
        public void run() {
            while (true)
            {
                gatheringMessage(_messageQueue.getNextMessage());
            }
        }
    }
    
    /**
     * Очередь сообщений для разбора
     */
    private MessageQueue _messageQueue;
    
    /**
     * экземпляр класса {@link Server}, который использует администратор
     */
    protected Server _server;
    
    private Thread _servTh, _gameTh, _mesTh;
    
    private ArrayList<String> _reservedClientNames;
    private MultipleServPlayer _servPlayers;
    
    protected ArrayList<String> _messagesNames;
    protected ArrayList<String> _adminMessageNames;
    
    private ArrayList<MessageHandler> _messageHandlers;
    private ArrayList<MessageHandler> _adminMessageHandler;
    
    /**
     * Экземпляр класса {@link Dealer}, который используется для игры
     */
    protected Dealer _dealer;
    
    /**
     * Конструктор класса. Инициализирует переменные.
     */
    public Admin(Dealer d) {
        fillMessageNames();
        _messageHandlers = new ArrayList<MessageHandler>();
        for (String mName : _messagesNames)
            _messageHandlers.add(new MessageHandler(this, "onMessageHandler_", mName));
        _adminMessageHandler = new ArrayList<MessageHandler>();
        for (String mName : _adminMessageNames)
            _adminMessageHandler.add(new MessageHandler(this, "onAdminMessageHandler", mName));
        _dealer = d;
        for (String mName : _dealer.getMessageNames())
            _messageHandlers.add(new MessageHandler(_dealer, "onMessageHandler_", mName));
        _messageQueue = new MessageQueue();
        _reservedClientNames = new ArrayList<String>();
        _servPlayers = new MultipleServPlayer(this);
        _server = new SocketServer(this);
        _servTh = new Thread(_server);
        _servTh.setName("Server thread");
    }
    
    public void addClient(ServPlayer sp) {
        _servPlayers.AddPlayer(sp);
    }
    
    /**
     * Запуск сервера
     */
    public void startServer() {
        _servTh.start();
    }
    
    private void fillMessageNames() {
        String messages[] = {"exit", "admin"};
        String adminMessages[] = {};
        _messagesNames = new ArrayList<String>();
        _messagesNames.addAll(java.util.Arrays.asList(messages));
        _adminMessageNames = new ArrayList<String>();
        _adminMessageNames.addAll(java.util.Arrays.asList(adminMessages));
        fillAdditionalMessageNames();
    }
    
    public void fillAdditionalMessageNames() {
    }
    
    /**
     * Возвращает список игроков, которые подключены к серверу
     * @return Список имён игроков
     */
    public LinkedList<String> getPlayerList() {
        LinkedList<String> ans = new LinkedList<String>();
        for(ServPlayer tmpPl : _servPlayers.getPlayers())
        {
            ans.add(tmpPl.getName());
        }
        return ans;
    }
    
    public void addMessage(Message m) {
        _messageQueue.addMessage(m);
    }
    
    /**
     * Метод проверки клиента на возможность участия в данной игре
     * @param player Пороверяемый клиент
     * @return возвращает true, если игрок может принять участие, иначе false
     */
    public boolean isPlayerOK(ServPlayer player) {
        return _dealer.checkPlayer(player.move("check/"));
    }
    
    /**
     * Метод по разбору одного сообщение
     * @param m сообщение для разбора
     */
    public void gatheringMessage(Message m) {
        if (m == null || m.getMessage() == null || m.getMessage().isEmpty())
            return;
        for (MessageHandler mh : _messageHandlers)
            if (mh.tryParseMessage(m))
                break;
    }
    
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
    private ArrayList<ServPlayer> _reservedPlayers = new ArrayList<ServPlayer>();
    /**
     * @return the _reservedPlayers
     */
    public ArrayList<ServPlayer> getReservedPlayers() {
        return _reservedPlayers;
    }
    /**
     * Получение имён игроков, зарезервированных на игру
     * @return список имён игроков
     */
    /*public LinkedList<String> getReservedPlayerList()
    {
        LinkedList<String> ans = new LinkedList<String>();
        for (ServPlayer sp : getReservedPlayers())
            ans.add(sp.name);
        return ans;
    }*/
    /**
     * Резервирование игрока
     * @param player игрок, который резервируется
     * @return возвращает true, если игрок может принять участие и ещё не содержится в списке зарезервированных игроков, иначе false
     */
    public boolean addPlayer(ServPlayer player)
    {
        if (getReservedPlayers().indexOf(player) == -1)
            if (isPlayerOK(player))
                getReservedPlayers().add(player);
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
    public boolean addPlayer(int index)
    {
        return addPlayer(_servPlayers.getPlayers().get(index));
    }
    /**
     * Удаления игрока из списка зарезервированных
     * @param player игрок для удаления
     * @return возвращает true, если игрок успешно удалён
     */
    public boolean removePlayer(ServPlayer player)
    {
        return getReservedPlayers().remove(player);
    }
    /**
     * Удаление игрока из списка зарезервированных по индексу
     * @param index индекс игрока
     * @return возвращает true, если игрок успешно удалён
     */
    public boolean removePlayer(int index)
    {
        return removePlayer(getReservedPlayers().get(index));
    }
    /**
     * Начало игры.
     * Добавлет зарезервированных игроков к диллеру и запускает игру.
     */
    public void startGame()
    {
        for (ServPlayer p : getReservedPlayers())
            _dealer.players.add(new GamePlayer(p));
        _dealer.initGame();
        _gameTh = new Thread(_dealer);
        _gameTh.setName("Dealer thread");
        _gameTh.start();
    }
    /**
     * Остановка игры
     */
    public void stopGame()
    {
        //gameTh.interrupt();
        _gameTh.stop();
    }
    /**
     * Начало разбора сообщений
     */
    public void startGathering()
    {
        _mesTh = new Thread(new GatheringMessages());
        _mesTh.setName("Message gathering thread");
        _mesTh.start();
    }
    /**
     * Остановка работы сервера и разбора сообщений
     */
    public void stopServer()
    {
        if (_servTh.isAlive())
            _servTh.interrupt();
        if (_mesTh.isAlive())
            _mesTh.interrupt();
    }
    /**
     * Ожидание окончания игры
     * @return возвращает true, если игра закончилась нормально
     */
    public Boolean waitEndGame()
    {
        try {
            if (_gameTh.isAlive())
                _gameTh.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean reserveName(String name) {
        ArrayList<String> allNames = new ArrayList<String>();
        for (ServPlayer sp : _servPlayers.getPlayers()) {
            allNames.add(sp.getName());
        }
        _reservedClientNames.removeAll(allNames);
        allNames.addAll(_reservedClientNames);
        if (allNames.contains(name))
            return false;
        _reservedClientNames.add(name);
        return true;
    }
}
