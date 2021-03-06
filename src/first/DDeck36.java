/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class DDeck36 extends Deck{
    private final int deckSize = 36;
    @Override
    public int countCardsInSimpleDeck()
    {
        return deckSize;
    }
    public DDeck36(int deckCount)
    {
        super(deckCount);
        for (int i = 0; i < deckCount; i++)
        {
            for (int j = 0; j < deckSize; j++)
            {
                allCards[i*deckSize + j] = new Card(Card.Value.values()[5+(j/4)], Card.Color.values()[j%4]);
            }
        }
    }
}
