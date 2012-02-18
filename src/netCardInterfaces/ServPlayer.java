/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Экземпляр данного класса осуществляет связь с удалённым клиентом
 * @author Alexey
 */
public abstract class ServPlayer {
    private class AnsweredMessageServPlayer extends AnsweredMessage {
        
        public AnsweredMessageServPlayer(String message) {
            super(message);
        }

        @Override
        protected void sendNext(String message) {
            sendMessage("answered/".concat(message));
        }
        
    }

    private static int _lastNumber = -1;
    private int _id;
    private String _name;
//    public LinkedList<Card> cards;
    private AnsweredMessageList _answers;
    protected Admin myAdmin;
    private GamePlayer gp;
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
        sendMessage("name/");
        _answers = new AnsweredMessageList();
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
    public String sendAndWaitAnswer(String message)
    {
        return _answers.addNew(new AnsweredMessageServPlayer(message)).getAnswer();
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
    public abstract void sendMessage(String message);
    
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
