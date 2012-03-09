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
public class MultipleServPlayer {

    private ArrayList<ServPlayer> _players;
    private Semaphore _addingSem;
    
    public MultipleServPlayer() {
        _players = new ArrayList<ServPlayer>();
        _addingSem = new Semaphore(1);
    }
    
    public void sendMessageToAll(NetPack message) {
        for (ServPlayer sp : getPlayers())
            sp.sendMessage(message);
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
    
    public boolean removePlayer(ServPlayer p) {
        pauseAdding();
        boolean answer = getPlayers().remove(p);
        continueAdding();
        return answer;
    }
    
    public boolean contains(ServPlayer p) {
        return getPlayers().contains(p);
    }
    
    public ServPlayer findByName(String name) {
        ServPlayer sp = null;
        for (int i = 0; i < getPlayers().size() && sp == null; i++) {
            if (getPlayers().get(i).getName().equals(name))
                sp = getPlayers().get(i);
        }
        return sp;
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
