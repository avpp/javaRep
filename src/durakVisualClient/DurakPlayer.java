/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durakVisualClient;

import java.util.LinkedList;
import first.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
/**
 *
 * @author Andrew
 */
public class DurakPlayer extends Player{
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
    
    //private 

    /**
     * @return the m_name
     */
    public String getM_name() {
        return m_name;
    }

    /**
     * @return the m_deck
     */
    public DurakDeck getM_deck() {
        return m_deck;
    }

    /**
     * @return the m_gambTable
     */
    public GamblingTable getM_gambTable() {
        return m_gambTable;
    }

    /**
     * @return the m_cards
     */
    public LinkedList<Card> getM_cards() {
        return m_cards;
    }

    /**
     * @return the m_adversaries
     */
    public LinkedList<DurakAdversary> getM_adversaries() {
        return m_adversaries;
    }

    /**
     * @return the m_winTable
     */
    public DurakWinTable getM_winTable() {
        return m_winTable;
    }

    /**
     * @return the m_active
     */
    public int getM_active() {
        return m_active;
    }

    /**
     * @return the m_passive
     */
    public int getM_passive() {
        return m_passive;
    }

    /**
     * @return the m_whoseTurn
     */
    public char getM_whoseTurn() {
        return m_whoseTurn;
    }

    /**
     * @return the m_me
     */
    public int getM_me() {
        return m_me;
    }

    /**
     * @return the m_score
     */
    public DurakScore getM_score() {
        return m_score;
    }

    /**
     * @return the m_trump
     */
    public Card getM_trump() {
        return m_trump;
    }

    /**
     * @return the m_chat
     */
    public LinkedList<String> getM_chat() {
        return m_chat;
    }
    
    private class setmakeTurn implements Runnable {
        public void run() {
            System.out.println("Подкидывай!");
            
            String str = "";
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            try {
                str = reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
             if (!("no".equals(str) || "".equals(str))) {
                int c = Integer.parseInt(str);
                Card ansCard = getM_cards().get(c - 1);
                str = ansCard.toString();
            }
            getClient().write("turn/".concat(str));
        }
    }
    
     private class setresponseTurn implements Runnable {
        public void run() {
            System.out.println("Отбивайся!");
            String str = "";
            try {
                System.in.read();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            try {
                str = reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
             if (!("no".equals(str))) {
                int c = Integer.parseInt(str);
                Card ansCard = getM_cards().get(c - 1);
                str = ansCard.toString();
            }
            getClient().write("turn/".concat(str));
        }
    }
    
    public DurakPlayer(Client c) {
        super(c);
        m_gambTable = new GamblingTable();
        m_cards = new LinkedList<Card>();
        m_adversaries = new LinkedList<DurakAdversary>();
        m_winTable = new DurakWinTable();
        m_score = new DurakScore();
        m_chat = new LinkedList<String>();
        m_deck = new DurakDeck();
    }
    
    @Override
    public void startGame(byte address[], int port) {
        try {
            super.startGame(address, port);
        } catch (Exception ex) {
            Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void parseAllString(String bigMesg) {
        System.out.println(bigMesg);
        String delim = "\n";
        String[] data = bigMesg.split(delim);
        for (String s : data)
            parseMessage(s);
    }
        
    public void parseMessage(String message) {
        String template[] = {"turn", "your", "gamt", 
                             "wint", "scor", "lspl",
                             "deck", "trmp", "mesg"};
        if ("name".equals(message)) {
            System.out.println("Приветик. Напиши пожалуйста свое имя!");
            try {
                System.in.read();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            String str = "";
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            try {
                str = reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            getClient().write(str);
            return;
        }
        
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
                    case 9: {
                        getClient().write(getName());
                    }
                }
            }
    }
    
    private void parseTurn(String message) {
        int i = 5;
        
        if (message.charAt(i) == '+') {
            try {
                makeTurn();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (message.charAt(i) == '-') {
            try {
                responseTurn();
            } catch (IOException ex) {
                Logger.getLogger(DurakPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
      
    private void parseYour(String message) {
        getM_cards().clear();
        int beginIndex = 5;
        message = message.substring(beginIndex);
        String delim = ",";
        String[] data = message.split(delim);
        for (String s : data)
            getM_cards().add(Card.fromString(s));
    }
    
    private void parseListPlayers(String message) {
        getM_adversaries().clear();
        
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
            getM_adversaries().add(
                    new DurakAdversary(name, amount));
        }
    }
        
    private void parseGamblingTable(String message) {
        m_gambTable = GamblingTable.fromString(message);
    }
    
    private void parseWinTable(String message) {
        getM_winTable().clearAll();
        int i = 5;
        String delim = ",";
        String[] data = message.substring(i).split(delim);
        for (String s : data)
            getM_winTable().addWinner(s);
    }
    
    private void parseScore(String message) {
        getM_score().clearAll();
        int beginIndex = 6;
        message = message.substring(beginIndex);
        String delim = ",";
        String[] data = message.split(delim);
        for (int i = 0; i < data.length; i +=2)
            getM_score().add(
                    data[i], Integer.parseInt(data[i + 1]));
    }
    
    private void parseTrump(String message) {
        int i = 5;
        message = message.substring(i);
        m_trump = Card.fromString(message);
    }
    
    public void parseDeck(String message) {
        int i = 5;
        message = message.substring(i);
        String delim = ",";
        String[] data = message.split(delim);
        int amount = Integer.parseInt(data[0]);
        getM_deck().setCurrentAmount(amount);
    }
    
    public void parseJustMessage(String message) {
        int i = 5;
        getM_chat().add(message.substring(i));
    }

    public void makeTurn() throws IOException {
        Thread th = new Thread(new setmakeTurn());
        th.start();
    }
    
    public void responseTurn() throws IOException {
        Thread th = new Thread(new setresponseTurn());
        th.start();
    }
}
