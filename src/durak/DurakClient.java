/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durak;

import java.io.*;
import java.io.IOException;
import java.util.LinkedList;
import first.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Thread;
/**
 *
 * @author Andrew
 */
public class DurakClient extends Client {
    private String m_name;
    private DurakDeck m_deck;
    private GamblingTable m_gambTable;
    private LinkedList<Card> m_cards;
    private LinkedList<DurakAdversary> m_adversaries;
    private DurakWinTable m_winTable;
    private int m_active;
    private int m_passive;
    private char m_whoseTurn;
    private int m_me;
    private DurakScore m_score;
    private Card m_trump;
    private LinkedList<String> m_chat;
    
    public DurakClient() {
        m_gambTable = new GamblingTable();
        m_cards = new LinkedList<Card>();
        m_adversaries = new LinkedList<DurakAdversary>();
        m_winTable = new DurakWinTable();
        m_score = new DurakScore();
        m_chat = new LinkedList<String>();
    }
    
    public void runLoop(String address, int port) {
        if (this.tryConnectTo(address, port))
            mainLoop();
        else
            System.out.println("Ошибка соединения!");
    }
    
    public void mainLoop() {
        long pause = 100;
        while(true) {
            try {
                Thread.sleep(pause);
            } catch (InterruptedException ex) {
                Logger.getLogger(DurakClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String mesg = read();
            /*!!!!*/System.out.println(mesg);
            
            if (mesg == "name")
                this.write("Andrew");
            else
                parseAllString(mesg);
        }
    }
    
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
            try {
                makeTurn();
            } catch (IOException ex) {
                Logger.getLogger(DurakClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (message.charAt(i) == '-') {
            try {
                responseTurn();
            } catch (IOException ex) {
                Logger.getLogger(DurakClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      
    private void parseYour(String message) {
        m_cards.clear();
        int beginIndex = 6;
        message = message.substring(beginIndex);
        String delim = ",";
        String[] data = message.split(delim);
        for (String s : data)
            m_cards.add(Card.fromString(s));
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
        m_winTable.clearAll();
        int i = 6;
        String delim = ",";
        String[] data = message.substring(i).split(delim);
        for (String s : data)
            m_winTable.addWinner(s);
    }
    
    private void parseScore(String message) {
        m_score.clearAll();
        int beginIndex = 6;
        message = message.substring(beginIndex);
        String delim = ",";
        String[] data = message.split(delim);
        for (int i = 0; i < data.length; i +=2)
            m_score.add(
                    data[i], Integer.parseInt(data[i + 1]));
    }
    
    private void parseTrump(String message) {
        int i = 6;
        message = message.substring(i);
        m_trump = Card.fromString(message);
    }
    
    public void parseDeck(String message) {
        int i = 6;
        message = message.substring(i);
        String delim = ",";
        String[] data = message.split(delim);
        int amount = Integer.parseInt(data[0]);
        m_deck.setCurrentAmount(amount);
    }
    
    public void parseJustMessage(String message) {
        int i = 6;
        m_chat.add(message.substring(i));
    }

    @Override
    public String read() {
        return super.read();
    }
    
    public void makeTurn() throws IOException {
        System.out.println("Подкидывай!");
        String str = "";
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        str = reader.readLine();
        
        this.write(str);
    }
    
    public void responseTurn() throws IOException {
        System.out.println("Отбивайся!");
        String str = "";
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        str = reader.readLine();
        
        this.write(str);
    }
}
