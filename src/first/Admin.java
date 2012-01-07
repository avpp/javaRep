/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.tools.jar.resources.jar;

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
            while (true)
            {
                while (messages.size() > 0)
                {
                    PauseAdding();
                    Message m = messages.removeFirst();
                    ContinueAdding();
                    gatheringMessage(m);
                }
            }
            /*
            String template[] = {"turn", "mesg", "exit"};
            LinkedList<ServPlayer> delPlayers = new LinkedList<ServPlayer>();
            while (true)
            {
                server.serverDontWork();
                for (ServPlayer curPlayer : server.players)
                {
                    int mes_count = curPlayer.messages.size();
                    for (int j =0; j < mes_count; j++)
                    {
                        String s = curPlayer.messages.removeFirst();
                        for (int i = 0; i < template.length; i++)
                        if (s.startsWith(template[i]))
                        {
                            switch (i)
                            {
                                case 0 : {
                                    curPlayer.setAnswer(s.substring(4));
                                } break;
                                case 1 : {
                                    String message = template[1].concat(curPlayer.name).concat(": ").concat(s.substring(4));
                                    for (ServPlayer tmpPlayer : server.players)
                                    {
                                        tmpPlayer.sendMessage(message);
                                    }
                                } break;
                                case 2 : {
                                    curPlayer.closeSocket();
                                    delPlayers.add(curPlayer);
                                } break;
                            }
                        }
                    }
                }
                if (delPlayers.size() > 0)
                {
                    server.players.removeAll(delPlayers);
                    if (dealer.players.removeAll(delPlayers))
                        gameTh.interrupt();
                    delPlayers.clear();
                }
                server.serverMayWork();
            }*/
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
        gameTh = new Thread(dealer);
        gameTh.start();
    }
    public void stopGame()
    {
        gameTh.interrupt();
    }
    public void startGathering()
    {
        mesTh = new Thread(new GatheringMessages());
        mesTh.start();
    }
}
