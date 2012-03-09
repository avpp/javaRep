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
 *
 * @author Alexey
 */
public class Local extends ServPlayer implements IClient {

    private ArrayList<String> _messages;
    private Semaphore _semRead, _semAdding;
    
    public Local(Admin admin) {
        super(admin);
        _messages = new ArrayList<String>();
        _semRead = new Semaphore(0);
        _semAdding = new Semaphore(1);
    }
    
    @Override
    protected void sendMessage(String message) {
        try {
            _semAdding.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Local.class.getName()).log(Level.SEVERE, null, ex);
        }
        _messages.add(message);
        _semRead.release();
        _semAdding.release();
    }

    @Override
    public String read() {
        try {
            _semRead.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Local.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            _semAdding.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Local.class.getName()).log(Level.SEVERE, null, ex);
        }
        String answer = _messages.remove(0);
        _semAdding.release();
        return answer;
    }

    @Override
    public void write(NetPack pack) {
        while (!pack.isEmpty())
            acceptMessage(pack.getMessage());
    }

    @Override
    public boolean isConnected() {
        return true;
    }
    
}
