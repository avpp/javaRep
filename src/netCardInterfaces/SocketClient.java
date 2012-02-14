/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class SocketClient extends Client{
    /**
     * Класс для обработки входящих сообщений
     */
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
    
    private Socket s;
    private Thread th;
    
    public SocketClient() {
        super();
        s = new Socket();
    }
    
    /**
     * Данный метод пытается создать сетевое подключение
     * @param addr IP адрес сервера
     * @param Port порт на сервере
     * @return возвращает true, если подключение было успешным
     */
    public Boolean tryConnectTo(String addr, int port)
    {
        try {
            s.connect(new InetSocketAddress(InetAddress.getByName(addr), port));
            th = new Thread(new Listen());
            th.setName("listener");
            th.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @Override
    protected void send(String str) {
        try {
            s.getOutputStream().write(str.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean isConnected() {
        return s.isConnected();
    }
}
