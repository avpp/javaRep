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
 * Клиентская часть
 * @author Andrew
 */
public abstract class Client {
    
    private Semaphore sem;
    /**
     * Список сообщений
     */
    private LinkedList<String> messages;
    
    /**
     * Конструктор
     */
    public Client()
    {
        sem = new Semaphore(0);
        messages = new LinkedList<String>();
    }
    
    /**
     * Добавление нового сообщения
     * @param str сообщение
     */
    protected void addMessage(String str)
    {
        messages.add(str);
        sem.release();
    }
    /**
     * Чтение текущего сообщения. Если нет сообщений, то метод ожидает его
     * @return сообщение
     */
    public String read()
    {
        String ans = "";
        try {
            sem.acquire();
            ans = messages.removeFirst();
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return ans;
    }
    /**
     * Отправление сообщения
     * @param str сообщеие
     */
    public void write(String str)
    {
        str = str.concat("@");
        send(str);
    }
    
    protected abstract void send(String str);
    protected abstract boolean isConnected();
}
