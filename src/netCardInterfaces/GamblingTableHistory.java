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
public class GamblingTableHistory {
    public class TableLog {
        private ICardSource _source;
        private ICardDestination _destinantion;
        private Card _card;
        public TableLog(ICardSource source, ICardDestination destination, Card card) {
            _source = source;
            _destinantion = destination;
            _card = card;
        }

        /**
         * @return the _source
         */
        public ICardSource getSource() {
            return _source;
        }

        /**
         * @return the _destinantion
         */
        public ICardDestination getDestinantion() {
            return _destinantion;
        }

        /**
         * @return the _card
         */
        public Card getCard() {
            return _card;
        }
    }
    
    private ArrayList<TableLog> _log;
    public ArrayList<TableLog> getLog() {
        return _log;
    }
    
    private GamblingTable _gt;
    public GamblingTable getGamblingTable() {
        return _gt;
    }
    
    public GamblingTableHistory() {
        _log = new ArrayList<TableLog>();
        _gt = new GamblingTable();
    }
    
    public boolean moveCard(Card card,
            ICardSource source, CardCoordinate sourceCrd,
            ICardDestination destination, CardCoordinate destinationCrd) {
        Card c = source.fetchCard(card, sourceCrd);
        if (c == null)
            return false;
        destination.pushCard(c, destinationCrd);
        getLog().add(new TableLog(source, destination, c));
        return true;
    }
}
