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
public class MultipleServPlayer extends ServPlayer {

    private ArrayList<ServPlayer> _players;
    private Semaphore _addingSem;
    
    public MultipleServPlayer(Admin admin) {
        super(admin);
        _players = new ArrayList<ServPlayer>();
        _addingSem = new Semaphore(1);
    }
    
    @Override
    public void sendMessage(String message) {
        for (ServPlayer sp : getPlayers())
            sp.sendMessage(message);
    }
    
    @Override
    public String getAnswer() {
        setAnswer("");
        return super.getAnswer();
    }

    /**
     * @return the _players
     */
    public ArrayList<ServPlayer> getPlayers() {
        return _players;
    }
    
    public void addPlayer(ServPlayer p) {
        pauseAdding();
        getPlayers().add(p);
        continueAdding();
    }
    
    public void removePlayer(ServPlayer p) {
        pauseAdding();
        getPlayers().remove(p);
        continueAdding();
    }
    
    public boolean contains(ServPlayer p) {
        return getPlayers().contains(p);
    }
    
    private void pauseAdding() {
        try {
            _addingSem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(MultipleServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void continueAdding() {
        _addingSem.release();
    }
}
