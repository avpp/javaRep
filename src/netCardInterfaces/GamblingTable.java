/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.ArrayList;

/**
 * Данный класс реализует игровой стол
 * @author Alexey
 */
public class GamblingTable implements ICardSource, ICardDestination {
    /**
     * игровое поле, на котором происходит игра.
     * представляет из себя двумерный "массив" ходов, т.е.
     * набор игровых событий, состоящих из ходов
     */
    private ArrayList<ArrayList<CardHeap>> _table;
    
    private boolean _resizable;
    
    /**
     * Конструктор, инициализирующий игровой стол
     */
    public GamblingTable() {
        _table = new ArrayList<ArrayList<CardHeap>>();
        _resizable = true;
    }
    
    public GamblingTable(int rowsCount, int columnCount, boolean resizable)
    {
        _table = new ArrayList<ArrayList<CardHeap>>(rowsCount);
        for (int i = 0; i < rowsCount; i++)
        {
            _table.add(new ArrayList<CardHeap>(columnCount));
            for (int j = 0; j < columnCount; j++)
                _table.get(i).add(new CardHeap());
        }
        _resizable = resizable;
    }
    /**
     * Просмотр определённого хода
     * @param actionNum первй индекс "массива", показывает номер игрового события (-1, если необходимо обратится к последнему событию)
     * @param cardNum второй индекс массива, показывает номер карты в этом игровом событии (-1, если необходимо обратится к последней карте)
     * @return возвращает объект класса {@link Turn} либо null, если указан неверный номер
     */
    public Card viewCard(int rowNum, int colNum, int cardNum)
    {
        if (rowNum == -1)
            rowNum = _table.size() - 1;
//        if (rowNum < 0 || rowNum >= _table.size())
//            return null;
        if (colNum == -1)
            colNum = _table.get(rowNum).size() - 1;
//        if (colNum < 0 || colNum >= _table.get(rowNum).size())
//            return null;
        if (cardNum == -1)
            cardNum = _table.get(rowNum).get(colNum).getCards().size() - 1;
 //       if (cardNum < 0 || cardNum >= _table.get(rowNum).get(colNum).getOwnerCards().size())
 //           return null;
        return _table.get(rowNum).get(colNum).viewCard(cardNum);
    }
    /**
     * Добавляет новый ход в конец игрового события
     * @param turn ход, который добавляется
     * @param actionNum номер игрового события или -1, если требуется добавить в последнее
     */
    public void addCard(Card card, int rowNum, int colNum, int cardNum) {
        if (rowNum == -1)
        {
            if (_resizable)
                _table.add(new ArrayList<CardHeap>());
            rowNum = _table.size() - 1;
        }
        if (colNum == -1)
        {
            if (_resizable)
                _table.get(rowNum).add(new CardHeap());
            colNum = _table.get(rowNum).size() - 1;
        }
        if (cardNum == -1)
        {
            if (_resizable)
                _table.get(rowNum).get(colNum).getCards().add(card);
        }
        else
        {
            _table.get(rowNum).get(colNum).getCards().add(cardNum, card);
        }
    }
    
    public Card removeCard(int rowNum, int colNum, int cardNum) {
        if (rowNum == -1)
            rowNum = _table.size() - 1;
        if (colNum == -1)
            colNum = _table.get(rowNum).size() - 1;
        if (cardNum == -1)
            cardNum = _table.get(rowNum).get(colNum).getCards().size() - 1;
        Card answer = _table.get(rowNum).get(colNum).getCards().remove(cardNum);
        if (_table.get(rowNum).get(colNum).getCards().isEmpty())
        {
            _table.get(rowNum).remove(colNum);
            if (_table.get(rowNum).isEmpty())
                _table.remove(rowNum);
        }
        return answer;
    }
    /**
     * Получает список всех карт, находящихся на игровом столе
     * @return список карт
     */
    public ArrayList<Card> getAllCards() {
        ArrayList<Card> answer = new ArrayList<Card>();
        for(ArrayList<CardHeap> cha: _table)
        {
            for(CardHeap ch: cha)
            {
                answer.addAll(ch.getCards());
            }
        }
        return answer;
    }
    
    /**
     * Преобразует игровой стол в строковое представление.
     * @return строчку
     */
    @Override
    public String toString() {
        String ans = "gamt";
        for (ArrayList<CardHeap> cha: _table)
        {
            ans = ans.concat("/");
            for(CardHeap ch: cha)
            {
                ans = ans.concat("|");
                for (Card c : ch.getCards())
                {
                    ans = ans.concat(c.toString()).concat(",");
                }
            }
        }
        ans = ans.concat("/\n");
        return ans;
    }
    
    /**
     * Данный метод возвращает игровой стол по его строковому представлению
     * @param str строчка, хранящая описание стола
     * @return новый игровой стол
     */
    public static GamblingTable fromString(String str) {
        String[] r = str.split("/");
        if (r.length <= 0)
            return null;
        GamblingTable ans = new GamblingTable();
        for (int i = 0; i < r.length; i++)
        {
            String[] c = r[i].split("|");
            for (int j = 0; j < c.length; j++)
            {
                String cards[] =c[j].split(",");
                for (int k = 0; k < cards.length; k++)
                {
                    ans.addCard(Card.fromString(cards[k]), i, j, k);
                }
            }
            
        }
        return ans;
    }

    @Override
    public Card fetchCard(Card card, CardCoordinate coordinate) {
        if (coordinate == null || coordinate.getCoordinates().length < 3)
            return null;
        if (Card.isEqual(card, viewCard(coordinate.getCoordinates()[0], 
                                        coordinate.getCoordinates()[1], 
                                        coordinate.getCoordinates()[2]))) {
            return removeCard(coordinate.getCoordinates()[0], 
                              coordinate.getCoordinates()[1], 
                              coordinate.getCoordinates()[2]);
        }
        return null;
    }

    @Override
    public void pushCard(Card card, CardCoordinate coordinate) {
        addCard(card, coordinate.getCoordinates()[0], 
                      coordinate.getCoordinates()[1], 
                      coordinate.getCoordinates()[2]);
    }

    @Override
    public int getCurrentAmount() {
        return getAllCards().size();
    }
}
