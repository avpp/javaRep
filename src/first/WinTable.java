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
public class WinTable {
    public class rec {
        private GamePlayer sp;
        private int score;
        public rec(GamePlayer p)
        {
            sp = p;
            score = 0;
        }
        public int getScore()
        {
            return score;
        }
        public GamePlayer getPlayer()
        {
            return sp;
        }
        public void incScore()
        {
            score++;
        }
    }
    public LinkedList<rec> records;
    public WinTable()
    {
        records = new LinkedList<WinTable.rec>();
    }
    public void addPlayer(GamePlayer p)
    {
        records.add(new rec(p));
    }
    public void incScore()
    {
        for (rec r : records)
        {
            r.incScore();
        }
    }
}
