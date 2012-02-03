/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public class LocalClient extends Client {

    private LocalServPlayer _sp;
    
    public void setServPlayer(LocalServPlayer sp) {
        _sp = sp;
        if (!_sp.getClient().equals(this))
            _sp.setClient(this);
    }
    
    public LocalServPlayer getServPlayer() {
        return _sp;
    }
    
    @Override
    protected void send(String str) {
        _sp.acceptMessage(str);
    }

    @Override
    protected boolean isConnected() {
        return _sp != null;
    }
    
    public void acceptMessage(String s) {
        addMessage(s);
    }
}
