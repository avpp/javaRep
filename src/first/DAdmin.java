/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class DAdmin extends Admin {
    @Override
    public void createDealer()
    {
        dealer = new DDealer(new DDeck36(1), this);
    }

    @Override
    public Boolean isPlayerOK(ServPlayer player) {
        return true;
    }

    private String template[] = {"turn", "mesg", "exit", "your"};
    private String Dtemplate[] = {"lspl", "gamt", "trmp", "deck", "wint","your"};
    @Override
    public void gatheringMessage(Message m) {
        String sm[] = m.message.split("/");
        if (sm.length <= 0)
            return;
        if (m.source == null)
        {
            for (int i = 0; i < Dtemplate.length; i++)
            {
                if (Dtemplate[i].equals(sm[0]))
                {
                    server.PauseWork();
                    for (ServPlayer p : server.players)
                    {
                        switch (i)
                        {
                            case 0 : {
                                p.sendMessage(m.message.replaceFirst("%d", String.valueOf(dealer.players.indexOf(p.getGamePlayer()))));
                            } break;
                            case 1 :
                            case 2 :
                            case 3 :
                            case 4 : {
                                p.sendMessage(m.message);
                            } break;
                            case 5 : {
                                if (p.getGamePlayer() != null)
                                {
                                    p.sendMessage(p.getGamePlayer().getCards());
                                }
                            } break;
                        }
                    }
                    server.ContinueWork();
                    break;
                }
            }
            return;
        }
        for (int i = 0; i < template.length; i++)
        {
            if (template[i].equals(sm[0]))
            {
                switch (i)
                {
                    case 0 : {
                        if (sm.length < 2)
                            return;
                        m.source.setAnswer(sm[1]);
                    } break;
                    case 1 : {
                        String text = "mesg/".concat(m.source.name).concat(m.message.substring(4)).concat("\n");
                        server.PauseWork();
                        for (ServPlayer sp : server.players)
                        {
                            sp.sendMessage(text);
                        }
                        server.ContinueWork();
                    } break;
                    case 2 : {
                        m.source.closeSocket();
                        server.PauseWork();
                        server.players.remove(m.source);
                        server.ContinueWork();
                        if (dealer.players.remove(m.source.getGamePlayer()))
                            stopGame();
                        RemoveMessagesByPlayer(m.source);
                    } break;
                    case 3 : {
                        m.source.sendMessage(m.source.getGamePlayer().getCards());
                    }
                }
                break;
            }
        }
    }
}
