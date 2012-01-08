/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;

/**
 *
 * @author Alexey
 */
public class GamePlayer {
    public LinkedList<Card> cards;
    public String name;
    private ServPlayer p;
    public GamePlayer(ServPlayer pl)
    {
        p = pl;
        p.setGamePlayer(this);
        name = pl.name;
        cards = new LinkedList<Card>();
    }
    public String move(String s)
    {
        return p.move(s);
    }
    public String getCards()
    {
        String ans = "your/";
        for (Card c : cards)
        {
            ans = ans.concat(c.toString()).concat(",");
        }
        ans = ans.concat("\n");
        return ans;
    }
    public void sendCards()
    {
        p.sendMessage(getCards());
    }
}
