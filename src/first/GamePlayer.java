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
        name = pl.name;
        cards = new LinkedList<Card>();
    }
    public String move(String s)
    {
        return p.move(s);
    }
}