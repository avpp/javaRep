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
    public int getScore(GamePlayer p)
    {
        int i;
        for (i = 0; i < records.size() && !records.get(i).getPlayer().equals(p); i++);
        return records.get(i).getScore();
    }
    @Override
    public String toString()
    {
        String ans = "wint/";
        for(rec r : records)
        {
            ans = ans.concat(r.getPlayer().name).concat(",");
        }
        ans = ans.concat("\n");
        return ans;
    }
}
