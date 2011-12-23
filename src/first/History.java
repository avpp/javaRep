/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;

/**
 *
 * @author Alexey
 */
public class History {
    private LinkedList<GamblingTable> tables;

    public History() {
        tables = new LinkedList<GamblingTable>();
    }
    public GamblingTable getLast() {
        return tables.getLast();
    }
    public GamblingTable getTable(int t) {
        return tables.get(t);
    }
    public void AddNew() {
        tables.add(new GamblingTable());
    }
}
