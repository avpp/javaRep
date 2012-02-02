/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class SocketServer extends Server {

    /**
     * класс, добавляющий клиентов в список клиентов
     */
    private class AddClient implements Runnable {
        private Socket _socket;
        public AddClient(Socket socket)
        {
            _socket = socket;
        }
        @Override
        public void run() {
            _admin.addClient(new SocketServPlayer(_admin, _socket));
        }
    }
    
    private ServerSocket _ssocket;
    public Boolean work;
    
    
    public SocketServer(Admin admin)
    {
        super(admin);
        work = true;
    }
    
    @Override
    protected void work() {
        try {
            _ssocket = new ServerSocket(15147, 100, null);
            System.out.println(_ssocket.getLocalSocketAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            
            return;
        }
        
        while (work)
        {
            try {
                Socket s = _ssocket.accept();
                Thread th = new Thread(new AddClient(s));
                th.setName("Request name by ".concat(s.getInetAddress().toString()).concat(":").concat(String.valueOf(s.getPort())));
                th.start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            _ssocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
