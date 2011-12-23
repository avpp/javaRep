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
public abstract class Dealer implements Runnable {
    //public ServPlayer players[];
    public LinkedList<ServPlayer> players;
    public Deck deck;
    public History history;
    public Dealer(Deck deck)
    {
        this.deck = deck;
        players = new LinkedList<ServPlayer>();
        history = new History();
    }
    public abstract void initGame();
    public void initGame(Deck deck)
    {
        this.deck = deck;
        initGame();
    }
    public abstract void play();
    @Override
    public void run() {
        play();
    }
}
