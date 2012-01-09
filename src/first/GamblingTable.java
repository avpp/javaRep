/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;

/**
 * Данный класс реализует игровой стол
 * @author Alexey
 */
public class GamblingTable {
    /**
     * игровое поле, на котором происходит игра.
     * представляет из себя двумерный "массив" ходов, т.е.
     * набор игровых событий, состоящих из ходов
     */
    public LinkedList<LinkedList<Turn>> table;
    /**
     * Конструктор, инициализирующий игровой стол
     */
    public GamblingTable()
    {
        table = new LinkedList<LinkedList<Turn>>();
    }
    
    public LinkedList<Turn> getStackbyIndex(int index) {
        return table.get(index);
    }
    
    public int numOfStacks() {
        return table.size();
    }
    
    public void addStack(LinkedList<Turn> stack) {
        table.add(stack);
    }
    /**
     * Просмотр определённого хода
     * @param actionNum первй индекс "массива", показывает номер игрового события (-1, если необходимо обратится к последнему событию)
     * @param cardNum второй индекс массива, показывает номер карты в этом игровом событии (-1, если необходимо обратится к последней карте)
     * @return возвращает объект класса {@link Turn} либо null, если указан неверный номер
     */
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
    /**
     * Добавляет новый ход в конец игрового события
     * @param turn ход, который добавляется
     * @param actionNum номер игрового события или -1, если требуется добавить в последнее
     */
    public void AddTurn(Turn turn, int actionNum)
    {
        if (actionNum < 0 || actionNum >= table.size())
        {
            actionNum = table.size();
            table.add(new LinkedList<Turn>());
        }
        table.get(actionNum).add(turn);
    }
    /**
     * Получает список всех карт, находящихся на игровом столе
     * @return список карт
     */
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
    /**
     * Вывод на экран игрового стола
     */
    public void PrintGamblingTable()
    {
        for (LinkedList<Turn> t: table)
        {
            for(Turn tt: t)
            {
                System.out.print(tt.getCard().toString() + " ");
            }
            System.out.println();
        }
    }
    /**
     * Преобразует игровой стол в строковое представление.
     * @return строчку
     */
    @Override
    public String toString()
    {
        String ans = "gamt";
        for (LinkedList<Turn> t: table)
        {
            ans = ans.concat("/");
            for(Turn tt: t)
            {
                ans = ans.concat(tt.getCard().toString()).concat(",");
            }
        }
        ans = ans.concat("\n");
        return ans;
    }
    /**
     * Данный метод возвращает игровой стол по его строковому представлению
     * @param str строчка, хранящая описание стола
     * @return новый игровой стол
     */
    public static GamblingTable fromString(String str)
    {
        String[] s = str.split("/");
        if (s.length <= 0)
            return null;
        if (!"gamt".equals(s[0]))
            return null;
        GamblingTable ans = new GamblingTable();
        for (int i = 1; i < s.length; i++)
        {
            String cards[] =s[i].split(",");
            for (int j = 0; j < cards.length; j++)
            {
                ans.AddTurn(new Turn(null, Card.fromString(cards[j])), i - 1);
            }
        }
        return ans;
    }
}
