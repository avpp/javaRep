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
    void drawDeck(IDeck d);
    void drawGamblTable(GamblingTable gt);
    void drawPlayerCard(CardHeap ch);
}
