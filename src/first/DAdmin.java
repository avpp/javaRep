/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class DAdmin extends Admin {
    @Override
    public void createDealer()
    {
        dealer = new DDealer(new DDeck36(1));
    }

    @Override
    public Boolean isPlayerOK(ServPlayer player) {
        return true;
    }
}
