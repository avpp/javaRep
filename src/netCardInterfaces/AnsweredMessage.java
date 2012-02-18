/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public abstract class AnsweredMessage {
    private String _answer;
    private String _sendingMessageName;
    private Semaphore _semAns;
    private boolean _wait;
    public AnsweredMessage(String message) {
        _sendingMessageName = message.split("/")[0];
        _wait = true;
        _semAns = new Semaphore(0);
        //sendMessage("answered/".concat(message));
        sendNext(message);
    }
    public String getAnswer() {
        if (!_wait)
            return null;
        try {
            _semAns.acquire();
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
        _semAns.release();
        return true;
    }
    
    protected abstract void sendNext(String message);
}
