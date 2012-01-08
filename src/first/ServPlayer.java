/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class ServPlayer {
    private class Listen implements Runnable {
        private Socket s;
        public Listen(Socket socket)
        {
            s = socket;
        }
        @Override
        public void run() {
            while (!s.isClosed())
            {
                try {
                    if (s.getInputStream().available() > 0)
                    {
                        byte b[] = new byte[s.getInputStream().available()];
                        s.getInputStream().read(b);
                        String mes = new String(b);
                        LinkedList<String> submes = new LinkedList(java.util.Arrays.asList(mes.split("\0")));
                        /*for (String curString : submes)
                        {
                            curString += "\0";
                        }*/
                        while (submes.size() > 0)
                        {
                            acceptMessage(submes.removeFirst());
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private Thread l_th;
    private Socket s;
    public String name;
//    public LinkedList<Card> cards;
    private String answer;
    private Semaphore sem;
    private Admin myAdmin;
    private GamePlayer gp;
    
    public ServPlayer(Socket socket, Admin admin) throws InterruptedException
    {
        myAdmin = admin;
        gp = null;
        s = socket;
        try {
            Boolean check = true;
            do
            {
                s.getOutputStream().write("name".getBytes());
                while(s.getInputStream().available() <= 0)
                {
                    Thread.sleep((long)100);
                    if (s.isInputShutdown())
                        throw new InterruptedException("client don't has name");
                }
                byte b[] = new byte [s.getInputStream().available()];
                s.getInputStream().read(b);
                name = new String(b);
                LinkedList<String> ReservedNames = myAdmin.getPlayerList();
                check = false;
                for (int i = 0; i < ReservedNames.size() && !check; i++)
                {
                    check = ReservedNames.get(i).equals(name);
                }
            } while (check);
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        l_th = new Thread(new Listen(socket));
        l_th.setName("Listen player ".concat(name));
        l_th.start();
        sem = new Semaphore(0);
    }
    public void setGamePlayer(GamePlayer p)
    {
        gp = p;
    }
    public GamePlayer getGamePlayer()
    {
        return gp;
    }
    public void closeSocket()
    {
        try {
            if (!s.isClosed())
                s.close();
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public synchronized void setAnswer(String s)
    {
        answer = s;
        sem.release();
    }
    private String getAnswer()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer;
    }
    public String move(String situation)
    {
        String ans = "";
        sendMessage(situation);
        ans = getAnswer();
        return ans;
    }
    public void acceptMessage(String message)
    {
        myAdmin.AddMessage(this, message);
    }
    public void sendMessage(String message)
    {
        if (s.isClosed())
            return;
        try {
            s.getOutputStream().write(message.getBytes());
        } catch (SocketException ex)
        {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
            try {
                s.close();
                myAdmin.AddMessage(this, "exit/");
            } catch (IOException ex1) {
                Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
