/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public interface IDeck extends ICardSource{
    public int getMaxAmount();
    public void fromInfo(String info);
    public String getInfo();
}
