/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class Server implements Runnable{
    private ServerSocket ssocket;
    public Boolean work;
    // Socket csockets[];
    public LinkedList<ServPlayer> players;
    public Server()
    {
        try {
            ssocket = new ServerSocket(15147, 100, null);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //csockets = new Socket[0];
        players = new LinkedList<ServPlayer>();
        work = true;
        sem = new Semaphore(1);
    }
    private Semaphore sem;
    public void serverMayWork()
    {
        sem.release();
    }
    public void serverDontWork()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
        while (work)
        {
            try {
                Socket s = ssocket.accept();
                try {
                    sem.acquire();
                    players.add(new ServPlayer(s));
                    sem.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*
                Socket tmp[] = new Socket[csockets.length + 1];
                System.arraycopy(csockets, 0, tmp, 0, csockets.length);
                tmp[csockets.length] = s;
                csockets = tmp;*/
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
