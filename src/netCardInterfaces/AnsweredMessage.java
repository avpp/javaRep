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
    public AnsweredMessage(NetPack pack) {
        String message = "";
        NetPack newPack = new NetPack();
        if (!pack.isEmpty()) {
            do {
                message = pack.getMessage();
                if (pack.isEmpty())
                    message = "answered/".concat(message);
                newPack.addMessage(message);
            } while(!pack.isEmpty());
        }
        String arr[] = message.split("/");
        if (arr.length < 2)
            return;
        _sendingMessageName = arr[1];
        _wait = true;
        _semAns = new Semaphore(0);
        //sendMessage("answered/".concat(message));
        sendNext(newPack.toString().replace("@", ""));
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
