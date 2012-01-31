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
public class GamblingTable {
    private class TableLog {
        private Object _source, _destinantion;
        private Card _card;
        public TableLog(Object source, Object destination, Card card) {
            _source = source;
            _destinantion = destination;
            _card = card;
        }

        /**
         * @return the _source
         */
        public Object getSource() {
            return _source;
        }

        /**
         * @return the _destinantion
         */
        public Object getDestinantion() {
            return _destinantion;
        }

        /**
         * @return the _card
         */
        public Card getCard() {
            return _card;
        }
    }
    private ArrayList<TableLog> log;
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
    public RelationCardOwner viewRelationCardOwner(int rowNum, int colNum, int cardNum)
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
            cardNum = _table.get(rowNum).get(colNum).getOwnerCards().size() - 1;
 //       if (cardNum < 0 || cardNum >= _table.get(rowNum).get(colNum).getOwnerCards().size())
 //           return null;
        return _table.get(rowNum).get(colNum).getOwnerCards().get(cardNum);
    }
    /**
     * Добавляет новый ход в конец игрового события
     * @param turn ход, который добавляется
     * @param actionNum номер игрового события или -1, если требуется добавить в последнее
     */
    public void addCard(ICardOwner owner, Card card, int rowNum, int colNum, int cardNum) {
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
                _table.get(rowNum).get(colNum).getOwnerCards().add(new RelationCardOwner(card, owner));
        }
        else
        {
            _table.get(rowNum).get(colNum).getOwnerCards().add(cardNum, new RelationCardOwner(card, owner));
        }
    }
    
    public RelationCardOwner removeCard(int rowNum, int colNum, int cardNum) {
        if (rowNum == -1)
            rowNum = _table.size() - 1;
        if (colNum == -1)
            colNum = _table.get(rowNum).size() - 1;
        if (cardNum == -1)
            cardNum = _table.get(rowNum).get(colNum).getOwnerCards().size() - 1;
        RelationCardOwner answer = _table.get(rowNum).get(colNum).getOwnerCards().remove(cardNum);
        if (_table.get(rowNum).get(colNum).getOwnerCards().isEmpty())
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
                for (RelationCardOwner ro : ch.getOwnerCards())
                {
                    answer.add(ro.getCard());
                }
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
                for (RelationCardOwner ro : ch.getOwnerCards())
                {
                    ans = ans.concat(ro.getCard().toString()).concat(",");
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
        if (!"gamt".equals(r[0]))
            return null;
        GamblingTable ans = new GamblingTable();
        for (int i = 1; i < r.length; i++)
        {
            String[] c = r[i].split("|");
            for (int j = 0; j < c.length; j++)
            {
                String cards[] =c[j].split(",");
                for (int k = 0; k < cards.length; k++)
                {
                    ans.addCard(null, Card.fromString(cards[k]), i - 1, j, k);
                }
            }
            
        }
        return ans;
    }
}
