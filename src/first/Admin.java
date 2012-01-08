/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public abstract class Admin {
    public class Message {
        public ServPlayer source;
        public String message;
        public Message(ServPlayer s, String m)
        {
            source = s;
            message = m;
        }
    }
    private class GatheringMessages implements Runnable {
        @Override
        public void run() {
            long pause = 100;
            while (true)
            {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (messages.size() > 0)
                {
                    PauseAdding();
                    Message m = messages.removeFirst();
                    ContinueAdding();
                    gatheringMessage(m);
                }
            }
        }
    }
    private LinkedList<Message> messages;
    protected Server server;
    private Thread servTh, gameTh, mesTh;
    protected Dealer dealer;
    public Admin()
    {
        server = new Server(this);
        servTh = new Thread(server);
        servTh.setName("Server thread");
        sem = new Semaphore(1);
        messages = new LinkedList<Message>();
    }
    public void StartServer()
    {
        servTh.start();
    }
    private Semaphore sem;
    public void PauseAdding()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void ContinueAdding()
    {
        sem.release();
    }
    public void AddMessage(ServPlayer sp, String mesg)
    {
        try {
            sem.acquire();
            messages.add(new Message(sp, mesg));
            sem.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void RemoveMessagesByPlayer(ServPlayer p)
    {
        PauseAdding();
        LinkedList<Message> delM = new LinkedList<Message>();
        for (Message m : messages)
        {
            if (p == m.source)
            {
                delM.add(m);
            }
        }
        messages.removeAll(delM);
        ContinueAdding();
    }
    public LinkedList<String> getPlayerList()
    {
        LinkedList<String> ans = new LinkedList<String>();
        for(ServPlayer tmpPl : server.players)
        {
            ans.add(tmpPl.name);
        }
        return ans;
    }
    public abstract void createDealer();
    public abstract Boolean isPlayerOK(ServPlayer player);
    public abstract void gatheringMessage(Message m);
    public Boolean addPlayer(ServPlayer player)
    {
        if (isPlayerOK(player))
            dealer.players.add(new GamePlayer(player));
        else
            return false;
        return true;
    }
    public Boolean addPlayer(int index)
    {
        return addPlayer(server.players.get(index));
    }
    public void startGame()
    {
        dealer.initGame();
        gameTh = new Thread(dealer);
        gameTh.setName("Dealer thread");
        gameTh.start();
    }
    public void stopGame()
    {
        gameTh.interrupt();
    }
    public void startGathering()
    {
        mesTh = new Thread(new GatheringMessages());
        mesTh.setName("Message gathering thread");
        mesTh.start();
    }
    public void stopServer()
    {
        if (servTh.isAlive())
            servTh.interrupt();
        if (mesTh.isAlive())
            mesTh.interrupt();
    }
    public Boolean waitEndGame()
    {
        try {
            if (gameTh.isAlive())
                gameTh.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
