/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;

/**
 *
 * @author Alexey
 */
public abstract class Admin {
    private class GatheringMessages implements Runnable {
        @Override
        public void run() {
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
                                    String message = ((curPlayer.name).concat(": ")).concat(s.substring(7));
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
            }
        }
        
    }
    private Server server;
    private Thread servTh, gameTh, mesTh;
    protected Dealer dealer;
    public Admin()
    {
        server = new Server();
        servTh = new Thread(server);
    }
    public void StartServer()
    {
        servTh.start();
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
    public void startGathering()
    {
        mesTh = new Thread(new GatheringMessages());
        mesTh.start();
    }
}
