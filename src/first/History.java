/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class History {
    private GamblingTable tables[];

    public History() {
        tables = new GamblingTable[0];
    }
    public GamblingTable getTop() {
        return tables[0];
    }
    public GamblingTable getTable(int t) {
        return (t < 0 || t >= tables.length) ? null : tables[tables.length - t];
    }
    public void AddNew() {
        GamblingTable tmp[] = new GamblingTable[tables.length + 1];
        System.arraycopy(tables, 0, tmp, 1, tables.length);
        tables = tmp;
        tables[0] = new GamblingTable();
    }
}
