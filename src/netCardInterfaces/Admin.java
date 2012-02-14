/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.lang.reflect.InvocationTargetException;
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
        private class GatheringOneMessage implements Runnable{
            private Message _message;
            GatheringOneMessage(Message message) {
                _message = message;
            }
            @Override
            public void run() {
                gatheringMessage(_message);
            }
            
        }
        
        @Override
        public void run() {
            while (true)
            {
                Message m = _messageQueue.getNextMessage();
                Thread th = new Thread(new GatheringOneMessage(m), "Gathering message ".concat(m.getName()));
                th.start();
                try {
                    th.join(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    private MultipleServPlayer _adminRightsServPlayers;
    
    protected ArrayList<String> _messagesNames;
    protected ArrayList<String> _adminMessageNames;
    
    private ArrayList<MessageHandler> _messageHandlers;
    private ArrayList<MessageHandler> _adminMessageHandler;
    
    /**
     * Список игроков, зарезервированных на игру
     */
    private MultipleServPlayer _reservedPlayers;
    
    /**
     * Экземпляр класса {@link Dealer}, который используется для игры
     */
    protected Dealer _dealer;
    
    private ArrayList<AboutGame> _availableGames;
    /**
     * Конструктор класса. Инициализирует переменные.
     */
    public Admin(ArrayList<AboutGame> games) {
        fillMessageNames();
        _messageHandlers = new ArrayList<MessageHandler>();
        for (String mName : _messagesNames)
            _messageHandlers.add(new MessageHandler(this, "onMessageHandler_", mName));
        _adminMessageHandler = new ArrayList<MessageHandler>();
        for (String mName : _adminMessageNames)
            _adminMessageHandler.add(new MessageHandler(this, "onAdminMessageHandler_", mName));
        _messageQueue = new MessageQueue();
        _reservedClientNames = new ArrayList<String>();
        _reservedPlayers = new MultipleServPlayer();
        _servPlayers = new MultipleServPlayer();
        _adminRightsServPlayers = new MultipleServPlayer();
        
        _availableGames = games;
                
        Local MainPlayer = new Local(this);
        _servPlayers.addPlayer(MainPlayer);
        _adminRightsServPlayers.addPlayer(MainPlayer);
        
        _server = new SocketServer(this);
        _servTh = new Thread(_server);
        _servTh.setName("Server thread");
    }
    
    public void addClient(ServPlayer sp) {
        _servPlayers.addPlayer(sp);
    }
    
    private void createDealer(Class<Dealer> cd) {
        if (cd == null)
            return;
        if (_dealer != null) {
            ArrayList<MessageHandler> delMh = new ArrayList<MessageHandler>();
            for (MessageHandler mh : _messageHandlers) {
                if (mh.isHandler(_dealer))
                    delMh.add(mh);
            }
            _messageHandlers.removeAll(delMh);
        }
        try {
            try {
                _dealer = cd.getConstructor(Admin.class).newInstance(this);
            } catch (InstantiationException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (_dealer == null)
            return;
        for (String mName : _dealer.getMessageNames())
            _messageHandlers.add(new MessageHandler(_dealer, "onMessageHandler_", mName));
    }
    
    private void fillMessageNames() {
        String messages[] = {"exit", "admin", "mesg", "cards", "answer"};
        String adminMessages[] = {"add", "remove", "kick", "admin", "dlopt", "lsdl", "setdl", "start"};
        _messagesNames = new ArrayList<String>();
        _messagesNames.addAll(java.util.Arrays.asList(messages));
        _adminMessageNames = new ArrayList<String>();
        _adminMessageNames.addAll(java.util.Arrays.asList(adminMessages));
        fillAdditionalMessageNames();
    }
    
    public void fillAdditionalMessageNames() {
    }
    
    /**
     * Обработка входящих сообщений
     */
// <editor-fold desc="Message handlers">
    public void onMessageHandler_exit(Message m) throws Exception {
        _servPlayers.removePlayer(m.getSource());
        _reservedPlayers.removePlayer(m.getSource());
        if (m.getSource().getGamePlayer() != null && _dealer.players.contains(m.getSource().getGamePlayer()))
            stopGame();
        if (_adminRightsServPlayers.removePlayer(m.getSource())) {
            if (_adminRightsServPlayers.getPlayers().isEmpty())
                throw new Exception("no admin =( available");
        }
    }
    
    public void onMessageHandler_admin(Message m) {
        if (_adminRightsServPlayers.contains(m.getSource())) {
            Message am = new Message(m.getSource(), m.getMessage());
            if (m.getMessage() == null || m.getMessage().isEmpty())
                return;
            for (MessageHandler mh : _adminMessageHandler)
                if (mh.tryParseMessage(m)) {
                    m.getSource().sendMessage("admin/accept");
                    break;
                }
        }
        else {
            m.getSource().sendMessage("admin/norights");
        }
    }
    
    public void onMessageHandler_mesg(Message m) {
        if (m.getMessage() == null || m.getMessage().isEmpty())
            return;
        String prefix = "mesg/".concat(m.getSource().getName()).concat("/");
        String sm[] = m.getMessage().split("/");
        if (sm.length > 1) {
            ServPlayer sp = _servPlayers.findByName(sm[0]);
            if (sp != null) {
                sp.sendMessage(prefix.concat(m.getMessage().substring(sm[0].length() + 1)));
                return;
            }
        }
        _servPlayers.sendMessageToAll(prefix.concat(m.getMessage()));
    }
    
    public void onMessageHandler_cards(Message m) {
        if (m.getSource().getGamePlayer() == null) {
            m.getSource().sendMessage("your/");
            return;
        }
        m.getSource().getGamePlayer().sendCards();
    }
    
    public void onMessageHandler_answer(Message m) {
        m.getSource().setAnswer(m.getMessage());
    }
    // <editor-fold desc="Admin message handlers">
    public void onAdminMessageHandler_add(Message m) {
        addPlayer(_servPlayers.findByName(m.getMessage()));
    }
    public void onAdminMessageHandler_remove(Message m) {
        removePlayer(_servPlayers.findByName(m.getMessage()));
    }
    public void onAdminMessageHandler_kick(Message m) {
        ServPlayer sp = _servPlayers.findByName(m.getMessage());
        if (sp == null)
            return;
        sp.sendMessage("exit/");
        sp.acceptMessage("exit/");
    }
    public void onAdminMessageHandler_admin(Message m) {
        ServPlayer sp = _servPlayers.findByName(m.getMessage());
        if (sp == null)
            return;
        _adminRightsServPlayers.addPlayer(sp);
    }
    public void onAdminMessageHandler_dlopt(Message m) {
        
        if ("get".equals(m.getMessage())) {
            m.getSource().sendMessage(_dealer.getOptions());
        }
        else {
            _dealer.setOptions(m.getMessage());
        }
    }
    public void onAdminMessageHandler_lsdl(Message m) {
        String answer = "lsdl/";
        for (AboutGame ag : _availableGames) {
            answer = answer.concat(ag.toString()).concat("/");
        }
        m.getSource().sendMessage(answer);
    }
    public void onAdminMessageHandler_setdl(Message m) {
        Class<Dealer> dc = null;
        for (AboutGame ag : _availableGames) {
            if (ag.isThisGame(m.getMessage())) {
                dc = ag.getDealerClass();
                break;
            }
        }
        if (dc == null) {
            m.getSource().sendMessage("setdl/nodealer");
        }
        createDealer(dc);
        m.getSource().acceptMessage("dlopt/get");
    }
    public void onAdminMessageHandler_start(Message m) {
        startGame();
    }
    // </editor-fold>
// </editor-fold>

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
        return _dealer.checkPlayer(player.sendAndWaitAnswer("check/"));
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
     * @return the _reservedPlayers
     */
    public ArrayList<ServPlayer> getReservedPlayers() {
        return _reservedPlayers.getPlayers();
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
        if (player == null)
            return false;
        if (!_reservedPlayers.contains(player))
            if (isPlayerOK(player))
                _reservedPlayers.addPlayer(player);
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
        if (player == null)
            return false;
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
        if (_dealer == null)
            return;
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
    public void startGathering() {
        _mesTh = new Thread(new GatheringMessages());
        _mesTh.setName("Message gathering thread");
        _mesTh.start();
    }
    /**
     * Остановка работы потока зазбора сообщений
     */
    public void stopGathering() {
        if (_mesTh.isAlive())
            _mesTh.interrupt();
    }
    
    /**
     * Запуск сервера (приём входящих запросов на подключение)
     */
    public void startServer() {
        _servTh.start();
    }
    
    /**
     * Остановка работы сервера (приём входящих запросов на подключение)
     */
    public void stopServer() {
        if (_servTh.isAlive())
            _servTh.interrupt();
    }
    /**
     * Ожидание окончания игры
     * @return возвращает true, если игра закончилась нормально
     */
    public Boolean waitEndGame() {
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
