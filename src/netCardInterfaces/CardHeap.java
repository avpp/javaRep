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
    private ArrayList<RelationCardOwner> _ownerCards;
    
    /**
     * @return the _ownersCards
     */
    public ArrayList<RelationCardOwner> getOwnerCards() {
        return _ownerCards;
    }
    
    public CardHeap() {
        _ownerCards = new ArrayList<RelationCardOwner>();
    }
    
    public Card viewCard(int index) {
        return getOwnerCards().get(index).getCard();
    }
}
