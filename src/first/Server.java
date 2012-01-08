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
    private class AddClient implements Runnable {
        private Socket s;
        public AddClient(Socket socket)
        {
            s = socket;
        }
        @Override
        public void run() {
            try {
                ServPlayer p;
                p = new ServPlayer(s, a);
                sem.acquire();
                /*Boolean check = true;
                while (check)
                {
                    check = false;
                    for (int i = 0; i < players.size() && !check; i++)
                    {
                        check = players.get(i).name.equals(p.name);
                    }
                    if (check)
                    {
                        sem.release();
                        //p = new ServPlayer(s, a);
                        //p.changeName();
                        sem.acquire();
                    }
                }*/
                players.add(p);
                sem.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private ServerSocket ssocket;
    private Admin a;
    public Boolean work;
    // Socket csockets[];
    public LinkedList<ServPlayer> players;
    public Server(Admin admin)
    {
        a = admin;
        try {
            ssocket = new ServerSocket(15147, 100, null);
            System.out.println(ssocket.getLocalSocketAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //csockets = new Socket[0];
        players = new LinkedList<ServPlayer>();
        work = true;
        sem = new Semaphore(1);
    }
    private Semaphore sem;
    public void ContinueWork()
    {
        sem.release();
    }
    public void PauseWork()
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
                Thread th = new Thread(new AddClient(s));
                th.setName("Request name by ".concat(s.getInetAddress().toString()).concat(":").concat(String.valueOf(s.getPort())));
                th.start();
                /*System.out.print("Socket... ");
                try {
                    sem.acquire();
                    System.out.println("accepted");
                    players.add(new ServPlayer(s, a));
                    System.out.println("add client with name " + players.getLast().name);
                    sem.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                /*
                Socket tmp[] = new Socket[csockets.length + 1];
                System.arraycopy(csockets, 0, tmp, 0, csockets.length);
                tmp[csockets.length] = s;
                csockets = tmp;*/
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            ssocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
