/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public class LocalServPlayer extends ServPlayer {

    private LocalClient _client;
    
    public void setClient(LocalClient client) {
        _client = client;
        if (!_client.getServPlayer().equals(this))
            _client.setServPlayer(this);
    }
    
    public LocalClient getClient() {
        return _client;
    }
    
    public LocalServPlayer(Admin admin) {
        super(admin);
    }
    
    @Override
    public void sendMessage(String message) {
        _client.acceptMessage(message);
    }
    
}
