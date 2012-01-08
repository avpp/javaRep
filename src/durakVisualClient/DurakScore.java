/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durakVisualClient;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Andrew
 */
public class DurakScore {
    private Map<String, Integer> m_scoreTable;
    
    public void clearAll() {
        m_scoreTable.clear();
    }
    
    public void add(String name, int numOfWins) {
        m_scoreTable.put(name, numOfWins);
    }
    
    public void changeByName(String name, int newNumOfWins) {
        for (Entry<String, Integer> e : m_scoreTable.entrySet())
            if (e.getKey().equals(name))
                e.setValue(newNumOfWins);
    }
}
