/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.ArrayList;

/**
 *
 * @author Alexey
 */
public class CardHeap {
    private ArrayList<Card> _cards;
    
    /**
     * @return the _ownersCards
     */
    public ArrayList<Card> getCards() {
        return _cards;
    }
    
    public CardHeap() {
        _cards = new ArrayList<Card>();
    }
    
    public Card viewCard(int index) {
        return getCards().get(index);
    }
}
