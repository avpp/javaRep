/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class GamblingTable {
    /*public enum Action {Add, Remove};
    private class HistoryItem{
        public Turn turn;
        public Action action;
        public int actionNum;
        public HistoryItem(Turn t, Action a, int aNum)
        {
            turn = t;
            action = a;
            actionNum = aNum;
        }
    };
    private HistoryItem history[];*/
    public Turn table[][];
    public GamblingTable()
    {
        table = new Turn[0][0];
        //history = new HistoryItem[0];
    }
    public Turn viewTurn(int actionNum, int cardNum)
    {
        if (actionNum < 0 || actionNum >= table.length)
            return null;
        if (cardNum < 0 || cardNum >= table[actionNum].length)
            return null;
        return table[actionNum][cardNum];
    }
    public void AddTurn(Turn turn, int actionNum)
    {
        if (actionNum < 0 || actionNum >= table.length)
        {
            actionNum = table.length;
            Turn tmpTable[][] = new Turn[table.length + 1][0];
            System.arraycopy(table, 0, tmpTable, 0, table.length);
            table = tmpTable;
        }
        Turn tmpTurns[] = new Turn[table[actionNum].length + 1];
        System.arraycopy(table[actionNum], 0, tmpTurns, 0, table[actionNum].length);
        tmpTurns[tmpTurns.length - 1] = turn;
        table[actionNum] = tmpTurns;
    }
    public Card[] getAllCards()
    {
        int count = 0;
        for(Turn[] t: table)
        {
            count +=t.length;
        }
        Card answer[] = new Card[count];
        int i = 0;
        for(Turn[] t: table)
        {
            for(Turn tt: t)
            {
                answer[i++] = tt.getCard();
            }
        }
        return answer;
    }
    public void PrintGamblingTable()
    {
        for (Turn[] t: table)
        {
            for(Turn tt: t)
            {
                System.out.print(tt.getCard().getName() + " ");
            }
            System.out.println();
        }
    }
}
