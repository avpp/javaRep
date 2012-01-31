/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.LinkedList;
import java.util.Random;

/**
 * Класс Deck представляет из себя колоду (стопку карт).
 * Для конкретной игры требуется реализовать свою колоду
 * @author Alexey
 */
public abstract class Deck implements IDeck, ICardSource{
    /**
     * перечисление SideType представляет две стороны для доступа к колоде
     * top =  сверху
     * low = снизу
     */
    public enum SideType {top, low};
    
    /**
     * указатель на верхнюю карту в колоде
     */
    protected int topNum;
    
    /**
     * указатель на нижнюю карту в колоде
     */
    protected int lowNum;
    
    /**
     * спислк всех карт в колоде
     */
    private LinkedList<Card> allCards;
    
    protected void addCard(Card card)
    {
        allCards.add(card);
    }
    
    /**
     * Конструктор, который инициализирует начальные параметры.
     * Заполнение колоды картами производится в конструкторе класса,
     * реализующего данный класс
     * @param deckCount количество простыых колод
     */
    public Deck()
    {
        allCards = new LinkedList<Card>();//  Card[deckCount*countCardsInSimpleDeck()];
        _fillDeck();
        topNum = 0;
        lowNum = allCards.size() - 1;
    }
    private void _fillDeck()
    {
        fillDeck();
    }
    /**
     * Метод, заполняющий колоду
     */
    protected abstract void fillDeck();
    /**
     * Метод для перетасовки карт несколько раз
     * @param count количетво перетасовок
     */
    public void shuffle(int count)
    {
        if (count < 1)
            count = 1;
        for (int j = 0; j < count; j++)
        {
            java.util.Random r = new Random();
            Card tmpCard;
            int pos;
            for (int i = 0; i < allCards.size(); i++)
            {
                pos = (Math.abs(r.nextInt()))%allCards.size();
                tmpCard = allCards.get(i);
                allCards.set(i, allCards.get(pos));
                allCards.set(pos, tmpCard);
            }
        }
    }
    
    /**
     * Получение карты с определённой стороны. (В результате карта "вынимается" из колоды, в отличии от метода {@link viewCard(SideType side)})
     * @param side сторона, с которой следует взять карту
     * @return Возвращает саму карту
     */
    public Card getCard(SideType side)
    {
        if (topNum > lowNum)
            return null;
        switch (side)
        {
            case top: {return allCards.get(topNum++);}
            case low: {return allCards.get(lowNum--);}
        }
        return null;
    }
    
    /**
     * Просмотр карты с определённой стороны. (Карта остаётся в колоде, в отличии от метода {@link getCard(SideType side)}
     * @param side сторона, с которой следует посмотреть карту
     * @return Карта, котораю находится со стороны side
     */
    public Card viewCard(SideType side)
    {
        if (topNum > lowNum)
            return null;
        switch (side)
        {
            case top: {return allCards.get(topNum);}
            case low: {return allCards.get(lowNum);}
        }
        return null;
    }
    
    /**
     * Размер полной колоды
     * @return размер колоды
     */
    @Override
    public int getMaxAmount() {
        return allCards.size();
    }
    
    /**
     * Размер колоды
     * @return возвращает размер колоды (количество оставшихся в колоде карт)
     */
    @Override
    public int getCurrentAmount() {
        return lowNum - topNum + 1;
    }
    
    /**
     * Возвращает информацию о колоде
     * @return информация о колоде
     */
    @Override
    public String getInfo() {
        return "deck/".concat(String.valueOf(getCurrentAmount()))
                .concat(",").concat(String.valueOf(getMaxAmount()));
    }
    
    @Override
    public Card fetchCard(Card card, CardCoordinate coordinate) {
        if (coordinate == null || coordinate.getCoordinates().length < 1)
            return null;
        return getCard(SideType.values()[coordinate.getCoordinates()[0]]);
    }
}
