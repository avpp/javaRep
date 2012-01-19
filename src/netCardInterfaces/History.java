/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * История игры, хранит в себе последовательность игровых столов {@link GamblingTable}
 * @author Alexey
 */
public class History {
    /**
     * Набор игровых столов
     */
    private LinkedList<GamblingTable> tables;
    /**
     * Конструктор истории
     */
    public History() {
        tables = new LinkedList<GamblingTable>();
    }
    /**
     * Возвращает последний игровой стол
     * @return последний игровой стол или null, если нет столов
     */
    public GamblingTable getLast() {
        try {
            return tables.getLast();
        } catch(NoSuchElementException ex)
        {
            return null;
        }
    }
    /**
     * Возвращает определённый игровой стол
     * @param t номер стола в истории
     * @return игровой стол под номером t или null, если не верно указан индекс
     */
    public GamblingTable getTable(int t) {
        try {
            return tables.get(t);
        } catch(IndexOutOfBoundsException ex)
        {
            return null;
        }
    }
    /**
     * Создаёт новый игровой стол и добаавляет его в историю
     * @return возвращает созданный игровой стол
     */
    public GamblingTable AddNew() {
        GamblingTable gt = new GamblingTable();
        tables.add(gt);
        return gt;
    }
}
