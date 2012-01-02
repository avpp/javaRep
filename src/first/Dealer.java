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
    //public LinkedList<ServPlayer> players;
    public LinkedList<GamePlayer> players;
    public LinkedList<String> messages;
    public WinTable wtable;
    public Deck deck;
    public History history;
    public Dealer(Deck deck)
    {
        this.deck = deck;
        players = new LinkedList<GamePlayer>();
        history = new History();
        wtable = new WinTable();
    }
    public abstract Boolean checkDeck(Deck d);
    public abstract String generateAllGameInfo();
    protected Boolean checkDeck()
    {
        return checkDeck(deck);
    }
    public abstract Boolean initGame();
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
    public void sendToAll(String message)
    {
        sendToAll(message, null);
    }
    public void sendToAll(String message, ServPlayer except)
    {
        String m = "message:";
        m.concat(message);
        if (except != null)
            m.concat("\nexcept:").concat(except.name);
        m.concat("\n");
        messages.add(m);
    }
}
