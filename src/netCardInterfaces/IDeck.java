/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public interface IDeck {
    public int getMaxAmount();
    public int getCurrentAmount();
    public void fromInfo(String info);
    public String getInfo();
}
