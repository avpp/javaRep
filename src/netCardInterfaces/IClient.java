/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public interface IClient {
    public String read();
    public void write(String str);
    public boolean isConnected();
}
