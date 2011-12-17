/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class Turn {
    private Card card;
    private Player player;
    Turn (Player p, Card c)
    {
        card = c;
        player = p;
    }
    public Card getCard()
    {
        return card;
    }
    public Player getPlayer()
    {
        return player;
    }
}
