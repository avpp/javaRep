/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Экземпляр данного класса осуществляет связь с удалённым клиентом
 * @author Alexey
 */
public abstract class ServPlayer {

    private static int _lastNumber = -1;
    private String _name;
//    public LinkedList<Card> cards;
    private String answer;
    private Semaphore sem;
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
        while (setName("Player".concat(String.valueOf(++_lastNumber))));
        sendMessage("name/");
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
    protected String getAnswer()
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
