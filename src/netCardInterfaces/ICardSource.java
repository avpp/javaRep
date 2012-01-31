/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public interface ICardSource {
    Card fetchCard(Card card, CardCoordinate coordinate);
}
