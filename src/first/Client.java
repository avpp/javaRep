/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class Client {
   //Здесь реализовать базовое сетевое взаимодействие,
   //получение и принятие строки-сообщения
   //От этого класса будем наследовать конкретных игроков
    private class Listen implements Runnable {

        @Override
        public void run() {
            long pause = 100;
            while(true)
            {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (s.getInputStream().available() > 0)
                    {
                        byte b[] = new byte[s.getInputStream().available()];
                        s.getInputStream().read(b);
                        String inmes = new String(b);
                        String submes[] = inmes.split("\n");
                        for (String m : submes)
                        {
                            addMessage(m);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private Thread th;
    private Socket s;
    private Semaphore sem;
    private LinkedList<String> messages;
    
    public Client()
    {
        s = new Socket();
        sem = new Semaphore(0);
        messages = new LinkedList<String>();
    }
    
    public Boolean tryConnectTo(byte addr[], int Port)
    {
        try {
            s.connect(new InetSocketAddress(InetAddress.getByAddress(addr), Port));
            th = new Thread(new Listen());
            th.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    private void addMessage(String str)
    {
        messages.add(str);
        sem.release();
    }
    
    public String read()
    {
        String ans = "";
        try {
            sem.acquire();
            ans = messages.removeFirst();
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
    
    public void write(String str)
    {
        try {
            s.getOutputStream().write(str.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
