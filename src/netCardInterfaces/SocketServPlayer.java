/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class SocketServPlayer extends ServPlayer {
    /**
     * Класс для прослушивания входящих сообщений
     */
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
                    try {
                        Thread.sleep((long)500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (s.getInputStream().available() > 0)
                    {
                        byte b[] = new byte[s.getInputStream().available()];
                        s.getInputStream().read(b);
                        String mes = new String(b);
                        LinkedList<String> submes = new LinkedList(java.util.Arrays.asList(mes.split("@")));
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
    
    public SocketServPlayer(Admin admin, Socket socket) {
        super(admin);
        s = socket;
        l_th = new Thread(new Listen(socket));
        l_th.setName("Listen player ".concat(getName()));
        l_th.start();
    }
    
    /**
     * Завершение соединения
     */
    public void closeSocket()
    {
        try {
            if (!s.isClosed())
                s.close();
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void sendMessage(String message)
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
                myAdmin.addMessage(new Message(this, "exit/"));
            } catch (IOException ex1) {
                Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean setName(String name) {
        boolean answer;
        if (answer = super.setName(name))
            l_th.setName("Listen player ".concat(getName()));
        return answer;
    }
}
