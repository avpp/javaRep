/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public class CardCoordinate {
    private int _coordinates[];
    public CardCoordinate(int coordinates[]) {
        _coordinates = coordinates;
    }

    /**
     * @return the _coordinates
     */
    public int[] getCoordinates() {
        return _coordinates;
    }
}
