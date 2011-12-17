/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.tools.jar.resources.jar;

/**
 *
 * @author Alexey
 */
public class Server{
    java.net.ServerSocket ssocket;
    public Server()
    {
        try {
            ssocket = new ServerSocket(15147, 100, null);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
