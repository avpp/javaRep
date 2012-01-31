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
 *
 * @author Alexey
 */
public class MessageQueue {
    /**
     * Список сообщений
     */
    private LinkedList<Message> _messages;
    private Semaphore _addingSemaphore, _messagesSemaphore;
    
    public MessageQueue() {
        _messages = new LinkedList<Message>();
        _addingSemaphore = new Semaphore(1);
        _messagesSemaphore = new Semaphore(0);
    }
    
    /**
     * Добавление сообщений
     * @param sp источник сообщения
     * @param mesg само сообщение
     */
    public void addMessage(Message m) {
            pauseAdding();
            _messages.add(m);
            _messagesSemaphore.release();
            continueAdding();
    }
    
    /**
     * Метод для синхронизации. Приостанавливает добавление сообщений
     */
    public void pauseAdding() {
        try {
            _addingSemaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Метод для синхронизации. Возобновляет возможность добавления сообщений
     */
    public void continueAdding() {
        _addingSemaphore.release();
    }
    
    /**
     * Удаление сообщений от источника p из списка сообщений
     * @param p источник сообщений.
     */
    public void removeMessagesByPlayer(ServPlayer p) {
        pauseAdding();
        LinkedList<Message> delM = new LinkedList<Message>();
        for (Message m : _messages)
        {
            if (p == m.getSource())
            {
                delM.add(m);
            }
        }
        _messages.removeAll(delM);
        try {
            _messagesSemaphore.acquire(delM.size());
        } catch (InterruptedException ex) {
            Logger.getLogger(MessageQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
        continueAdding();
    }
    
    public Message getNextMessage() {
        Message m = null;
        try {
            _messagesSemaphore.acquire();
            pauseAdding();
            m = _messages.removeFirst();
            continueAdding();
        } catch (InterruptedException ex) {
            Logger.getLogger(MessageQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }
}
