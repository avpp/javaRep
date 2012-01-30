/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public interface GameInterface {
    void drawDeck(IDeck d, Card trump);
    void drawGamblTable(GamblingTable gt);
}
