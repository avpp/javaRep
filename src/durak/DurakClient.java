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
    private GamblingTable m_gambTable;
    private LinkedList<Card> m_cards;
    private LinkedList<DurakAdversary> m_adversaries;
    
    public void parseAllString(String bigMesg) {
        String delim = "\n";
        String[] data = bigMesg.split(delim);
        for (String s : data)
            parseMessage(s);
    }
        
    public void parseMessage(String message) {
        String template[] = {"turn", "take", "your",
                             "gamt", "wint", "scor",
                             "lspl", "deck", "trmp",
                             "mesg"};
        
        for (int i = 0; i < template.length; i++)
            if (message.startsWith(template[i])) {
                switch (i)
                {
                    case 0 : {
                        parseTurn(message);
                    } break;
                    case 1 : {
                        parseTake(message);
                    } break;
                    case 2 : {
                        parseYour(message);
                    } break;
                    case 3 : {
                        parseGamblingTable(message);
                    } break;
                    case 4 : {
                        parseWinTable(message);
                    } break;
                    case 5 : {
                        parseScore(message);
                    } break;
                    case 6 : {
                        parseListPlayers(message);
                    } break;    
                    case 7 : {
                        parseDeck(message);
                    } break;
                    case 8 : {
                        parseTrump(message);
                    } break;
                    case 9 : {
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
    
    private void parseTake(String message) {
        
    }
            
    private void parseYour(String message) {
        m_cards.clear();
        for (int i = 6; i < message.length(); i += 2) {
            int iColor = (int)message.charAt(i);
            int iValue = (int)message.charAt(i + 1);
            Color c = Color.values()[iColor];
            Value v = Value.values()[iValue];
            
            m_cards.add(new Card(v, c));
        }
    }
    
    private void parseListPlayers(String message) {
        m_adversaries.clear();
        String delimeter = "/";
        String plArr[] = message.split(delimeter);
        //Пропускаем 1й элемент(содержит команду "plls"
        for (int i = 1; i < plArr.length; i += 2) {
            int amount = Integer.parseInt(plArr[i + 1]);
            m_adversaries.add(
                    new DurakAdversary(plArr[i], amount));
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
