/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public abstract class Player {
    public String name;
    public Card cards[];
    public abstract String move(String situation);
}
