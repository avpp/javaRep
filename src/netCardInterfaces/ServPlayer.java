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
    private class AnsweredMessage {
        private String _answer;
        private String _sendingMessageName;
        private Semaphore _sem;
        private boolean _wait;
        public AnsweredMessage(String message) {
            _sendingMessageName = message.split("/")[0];
            _wait = true;
            _sem = new Semaphore(0);
            sendMessage(message);
        }
        public String getAnswer() {
            if (!_wait)
                return null;
            try {
                _sem.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            _wait = false;
            return _answer;
        }
        public boolean setAnswer(String answer) {
            if (!_wait || answer == null || !answer.split("/")[0].equals(_sendingMessageName))
                return false;
            _answer = answer;
            _sem.release();
            return true;
        }
    }

    private static int _lastNumber = -1;
    private int _id;
    private String _name;
//    public LinkedList<Card> cards;
    private ArrayList<AnsweredMessage> _answers;
    private Semaphore _sem;
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
        _answers = new ArrayList<AnsweredMessage>();
        _sem = new Semaphore(1);
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
        try {
            _sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i = 0;
        while (!_answers.get(i++).setAnswer(s));
        _sem.release();
    }
    public void setAllAnswers(String s) {
        try {
            _sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < _answers.size(); i++)
            _answers.get(i).setAnswer(s);
        _sem.release();
    }
    
    /**
     * Метод, возвращающий ответ на данное сообщение
     * @param message сообщение, передаваемое клиенту
     * @return ответ на запрос
     */
    public String sendAndWaitAnswer(String message)
    {
        try {
            _sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        AnsweredMessage am = new AnsweredMessage(message);
        _answers.add(am);
        _sem.release();
        String answer = am.getAnswer();
        try {
            _sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        _answers.remove(am);
        _sem.release();
        return answer;
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
