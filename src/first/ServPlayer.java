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
                        for (String curString : submes)
                        {
                            curString += "\0";
                        }
                        messages.addAll(submes);
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
    public LinkedList<String> messages;
    
    public ServPlayer(Socket socket)
    {
        s = socket;
        try {
            s.getOutputStream().write("name".getBytes());
            while(s.getInputStream().available() <= 0);
            byte b[] = new byte [s.getInputStream().available()];
            s.getInputStream().read(b);
            name = new String(b);
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        l_th = new Thread(new Listen(socket));
        l_th.start();
        messages = new LinkedList<String>();
//        cards = new LinkedList<Card>();
        sem = new Semaphore(0);
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
                messages.clear();
                messages.add("delete");
            } catch (IOException ex1) {
                Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
