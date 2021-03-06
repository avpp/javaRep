/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.Random;

/**
 *
 * @author Alexey
 */
public abstract class Deck {
    public enum SideType {top, low};
    
    public abstract int countCardsInSimpleDeck();
    protected int deckCount;
    protected int topNum, lowNum;
    protected Card allCards[];
    
    public Deck(int deckCount)
    {
        this.deckCount = deckCount;
        allCards = new Card[deckCount*countCardsInSimpleDeck()];
        topNum = 0;
        lowNum = allCards.length - 1;
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
    
    public void Shuffle()
    {
        Shuffle(1);
    }
    
    public void Shuffle(int count)
    {
        if (count < 1)
            count = 1;
        for (int j = 0; j < count; j++)
        {
            java.util.Random r = new Random();
            Card tmpCard;
            int pos;
            for (int i = 0; i < allCards.length; i++)
            {
                pos = (Math.abs(r.nextInt()))%allCards.length;
                tmpCard = allCards[i];
                allCards[i] = allCards[pos];
                allCards[pos] = tmpCard;
            }
        }
    }
    
    public Card getCard(SideType side)
    {
        if (topNum > lowNum)
            return null;
        switch (side)
        {
            case top: {return allCards[topNum++];}
            case low: {return allCards[lowNum--];}
        }
        return null;
    }
    
    public Card viewCard(SideType side)
    {
        if (topNum > lowNum)
            return null;
        switch (side)
        {
            case top: {return allCards[topNum];}
            case low: {return allCards[lowNum];}
        }
        return null;
    }
    
    public int size()
    {
        return lowNum - topNum + 1;
    }
    
    public void PrintDeck()
    {
        int i =0;
        for(Card card : allCards)
        {
            System.out.print(card.getName() + " ");
            if ((++i)%4 == 0)
                System.out.println();
        }
    }
}
