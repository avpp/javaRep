/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durak;

import java.util.LinkedList;
import first.*;
import first.Card.Color;
import first.Card.Value;

/**
 *
 * @author Andrew
 */
public class DurakClient extends Client {
    private String m_name;
    private GamblingTable m_gambTable;
    private LinkedList<Card> m_cards;
    private LinkedList<DurakAdversary> m_adversaries;
    private int m_active;
    private int m_passive;
    private char m_whoseTurn;
    private int m_me;
    
    public DurakClient
    
    public void parseAllString(String bigMesg) {
        String delim = "\n";
        String[] data = bigMesg.split(delim);
        for (String s : data)
            parseMessage(s);
    }
        
    public void parseMessage(String message) {
        String template[] = {"turn", "your", "gamt", 
                             "wint", "scor", "lspl",
                             "deck", "trmp", "mesg"};
        
        for (int i = 0; i < template.length; i++)
            if (message.startsWith(template[i])) {
                switch (i)
                {
                    case 0 : {
                        parseTurn(message);
                    } break;
                    case 1 : {
                        parseYour(message);
                    } break;
                    case 2 : {
                        parseGamblingTable(message);
                    } break;
                    case 3 : {
                        parseWinTable(message);
                    } break;
                    case 4 : {
                        parseScore(message);
                    } break;
                    case 5 : {
                        parseListPlayers(message);
                    } break;    
                    case 6 : {
                        parseDeck(message);
                    } break;
                    case 7 : {
                        parseTrump(message);
                    } break;
                    case 8: {
                        parseJustMessage(message);
                    } break;
                }
            }
    }
    
    private void parseTurn(String message) {
        int i = 6;
        if (message.charAt(i) == '+') {
            !!!makeTurn()
        }
        else if (message.charAt(i) == '-') {
            !!!responseTurn()
        }
    }
      
    private void parseYour(String message) {
        m_cards.clear();
        int beginIndex = 6;
        message = message.substring(beginIndex);
        String delim = ",";
        String[] data = message.split(delim);
        for (String s : data)
            m_cards.add(new Card.fromString(s));
    }
    
    private void parseListPlayers(String message) {
        m_adversaries.clear();
        
        String delim = "/";
        String[] data = message.split(delim);
        String p = data[1];
        
        m_me = (int)p.charAt(0);
        m_active = (int)p.charAt(1);
        m_passive = (int)p.charAt(2);
        m_whoseTurn = p.charAt(3);
        
        delim = ",";
        String[] players = data[2].split(delim);
        
        for (int i = 0; i < players.length; i += 2) {
            String name = players[i];
            int amount = Integer.parseInt(players[i + 1]);
            m_adversaries.add(
                    new DurakAdversary(name, amount));
        }
    }
        
    private void parseGamblingTable(String message) {
        m_gambTable = GamblingTable.fromString(message);
    }
    
    private void parseWinTable(String message) {
        
    }
    
    private void parseTurn(String message)
    private void parseTurn(String message)        
}
