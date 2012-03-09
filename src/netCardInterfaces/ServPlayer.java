/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 * Экземпляр данного класса осуществляет связь с удалённым клиентом
 * @author Alexey
 */
public abstract class ServPlayer {
    private class AnsweredMessageServPlayer extends AnsweredMessage {
        
        public AnsweredMessageServPlayer(NetPack pack) {
            super(pack);
        }

        @Override
        protected void sendNext(String message) {
            acceptMessage("answered/".concat(message));
            //sendMessage("answered/".concat(message));
        }
        
    }

    private static int _lastNumber = -1;
    private int _id;
    private String _name;
//    public LinkedList<Card> cards;
    private AnsweredMessageList _answers;
    protected Admin myAdmin;
    private InGamePlayer gp;
    /**
     * Конструктор данного класс, создаёт экземпляр данного класса.
     * @param socket сокет, с помощью которого осуществляется связь с клиентом
     * @param admin экземпляр класса {@link Admin}, который курирует работу данного объекта
     * @throws InterruptedException
     */
    public ServPlayer(Admin admin)
    {
        myAdmin = admin;
        gp = null;
        while (!setName("Player".concat(String.valueOf(_id = ++_lastNumber))));
        sendMessage(new NetPack("name/"));
        _answers = new AnsweredMessageList();
    }
    /**
     * Связь даного экземпляра с экземпляром класса {@link InGamePlayer}
     * @param p экземпляр класса {@link InGamePlayer}, с которым надо осуществить связь
     */
    public void setGamePlayer(InGamePlayer p)
    {
        gp = p;
    }
    /**
     * Получение связанного с данным классом объекта {@link InGamePlayer}
     * @return объекта класса {@link InGamePlayer}
     */
    public InGamePlayer getGamePlayer()
    {
        return gp;
    }
    /**
     * Установка ответа на запрос хода
     * @param s текст ответа
     */
    public void setAnswer(String s) {
        _answers.setAnswer(s);
    }
    public void setAllAnswers(String s) {
        _answers.setAllAnswers(s);
    }
    
    /**
     * Метод, возвращающий ответ на данное сообщение
     * @param message сообщение, передаваемое клиенту
     * @return ответ на запрос
     */
    public String sendAndWaitAnswer(NetPack pack, String message)
    {
        if (pack == null)
            pack = new NetPack();
        pack.addMessage(message);
        return _answers.addNew(new AnsweredMessageServPlayer(pack)).getAnswer();
    }
    
    /**
     * Добавление сообщения, пришедшего от имени этого игрока в список обработки сообщений администратора
     * @param message 
     */
    public void acceptMessage(String message)
    {
        myAdmin.addMessage(new Message(this, message));
    }
    
    /**
     * Отправка сообщения удалённому клиенту
     * @param message сообщение
     */
    public void sendMessage(NetPack pack) {
        sendMessage(pack.toString());
    }
    
    protected abstract void sendMessage(String message);
    
    public String getName() {
        return _name;
    }
    
    public boolean setName(String name) {
        if (!myAdmin.reserveName(name))
            return false;
        _name = name;
        return true;
    }
}
