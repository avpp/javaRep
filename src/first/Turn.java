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
    private GamePlayer player;
    Turn (GamePlayer p, Card c)
    {
        card = c;
        player = p;
    }
    public Card getCard()
    {
        return card;
    }
    public GamePlayer getPlayer()
    {
        return player;
    }
}
