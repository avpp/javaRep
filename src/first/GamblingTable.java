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
public class GamblingTable {
    public LinkedList<LinkedList<Turn>> table;
    public GamblingTable()
    {
        table = new LinkedList<LinkedList<Turn>>();
    }
    public Turn viewTurn(int actionNum, int cardNum)
    {
        if (actionNum == -1)
            actionNum = table.size() - 1;
        if (actionNum < 0 || actionNum >= table.size())
            return null;
        if (cardNum == -1)
            cardNum = table.get(actionNum).size() - 1;
        if (cardNum < 0 || cardNum >= table.get(actionNum).size())
            return null;
        return table.get(actionNum).get(cardNum);
    }
    public void AddTurn(Turn turn, int actionNum)
    {
        if (actionNum < 0 || actionNum >= table.size())
        {
            actionNum = table.size();
            table.add(new LinkedList<Turn>());
        }
        table.get(actionNum).add(turn);
    }
    public LinkedList<Card> getAllCards()
    {
        int count = 0;
        for(LinkedList<Turn> t: table)
        {
            count += t.size();
        }
        LinkedList<Card> answer = new LinkedList<Card>();
        int i = 0;
        for(LinkedList<Turn> t: table)
        {
            for(Turn tt: t)
            {
                answer.add(tt.getCard());
            }
        }
        return answer;
    }
    public void PrintGamblingTable()
    {
        for (LinkedList<Turn> t: table)
        {
            for(Turn tt: t)
            {
                System.out.print(tt.getCard().getName() + " ");
            }
            System.out.println();
        }
    }
}
