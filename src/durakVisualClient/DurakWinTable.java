/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durakVisualClient;

import java.util.LinkedList;

/**
 *
 * @author Andrew
 */
public class DurakWinTable {
    private LinkedList<String> m_winners;
    
    public DurakWinTable() {
        m_winners = new LinkedList<String>();
    }
    
    public void addWinner(String name) {
        m_winners.add(name);
    }
    
    public void clearAll() {
        m_winners.clear();
    }
}
