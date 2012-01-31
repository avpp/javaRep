/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public class RelationCardOwner {
    private Card _card;
    private ICardOwner _owner;
    public RelationCardOwner(Card card, ICardOwner owner) {
        _card = card;
        _owner = owner;
    }

    /**
     * @return the _card
     */
    public Card getCard() {
        return _card;
    }

    /**
     * @return the _owner
     */
    public ICardOwner getOwner() {
        return _owner;
    }

    /**
     * @param card the _card to set
     */
    public void setCard(Card card) {
        this._card = card;
    }
}
