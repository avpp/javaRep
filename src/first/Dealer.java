/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public abstract class Dealer {
    public Player players[];
    public Deck deck;
    public History history;
    public abstract void initGame(Deck deck);
    public abstract void play();
    public void addPlayer(Player player)
    {
        Player tmp[] = new Player[players.length + 1];
        System.arraycopy(players, 0, tmp, 0, players.length);
        tmp[players.length] = player;
        players = tmp;
    }
    public void remPlayer(int index)
    {
        if (index < 0 || index >= players.length)
        {
            return;
        }
        Player tmp[] = new Player[players.length - 1];
        System.arraycopy(players, 0, tmp, 0, index-1);
        System.arraycopy(players, index+1, tmp, index, players.length - index);
        players = tmp;
    }
}
