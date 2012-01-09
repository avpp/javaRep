/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;
import java.util.Random;

/**
 * Класс Deck представляет из себя колоду (стопку карт).
 * Для конкретной игры требуется реализовать свою колоду
 * @author Alexey
 */
public abstract class Deck {
    /**
     * перечисление SideType представляет две стороны для доступа к колоде
     * top =  сверху
     * low = снизу
     */
    public enum SideType {top, low};
    /**
     * Данный метод должен возвращать количество карт в простой колоде
     * @return количество карт в одной простой колоде
     */
    public abstract int countCardsInSimpleDeck();
    /**
     * количество простых колод из которых состоит данная колода
     */
    protected int deckCount;
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
    protected LinkedList<Card> allCards;
    /**
     * Конструктор, который инициализирует начальные параметры.
     * Заполнение колоды картами производится в конструкторе класса,
     * реализующего данный класс
     * @param deckCount количество простыых колод
     */
    public Deck(int deckCount)
    {
        this.deckCount = deckCount;
        allCards = new LinkedList<Card>();//  Card[deckCount*countCardsInSimpleDeck()];
        topNum = 0;
        lowNum = 0;
    }
    /*{
        this.deckType = deckType;
        this.deckCount = deckCount;
        int startVal = (deckType == DeckType.count32)?5:(deckType == DeckType.count36)?4:0;
        allCards = new Card[deckCount*countCardsInSimpleDeck(deckType)];
        topNum = 0;
        lowNum = allCards.length - 1;
        for (int i = 0; i < deckCount; i++)
        {
            for (int j = 0; j < countCardsInSimpleDeck(deckType); j++)
            {
                allCards[i*countCardsInSimpleDeck(deckType) + j] = new Card(Card.Value.values()[startVal + (j/4)], Card.Color.values()[((j<53)?j:(j+1))%4]);
            }
        }
    }*/
    /**
     * Метод для перетасовки карт один раз
     */
    public void Shuffle()
    {
        Shuffle(1);
    }
    /**
     * Метод для перетасовки карт несколько раз
     * @param count количетво перетасовок
     */
    public void Shuffle(int count)
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
     * Размер колоды
     * @return возвращает размер колоды (количество оставшихся в колоде карт)
     */
    public int size()
    {
        return lowNum - topNum + 1;
    }
    /**
     * Возвращает размер полной колоды
     * @return возвращает максимальное количество карт в данной колоде
     */
    public int fullDeckSize()
    {
        return deckCount*countCardsInSimpleDeck();
    }
    /**
     * Просто вывод колоды на экран 
     */
    public void PrintDeck()
    {
        int i =0;
        for(Card card : allCards)
        {
            System.out.print(card.toString() + " ");
            if ((++i)%4 == 0)
                System.out.println();
        }
    }
    /**
     * Возвращает информацию о колоде
     * @return информация о колоде
     */
    public abstract String getInfo();
}
