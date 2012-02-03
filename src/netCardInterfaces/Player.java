/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.ArrayList;

/**
 *
 * @author Andrew
 */
public abstract class Player {

    protected class gameLoop implements Runnable{
        @Override
        public void run() {
            while (true) {
                String mesg = getClient().read();
                parseAllString(mesg);
            }
        }
    }

    private String name;
    private Client client;
    protected Thread thGame;
    protected ArrayList<String> _messageNames;
    private ArrayList<MessageHandler> _messageHandlers;
    
    public Player(Client c) {
        client = c;
        fillMessageNames();
        _messageHandlers = new ArrayList<MessageHandler>();
        for (String mName : _messageNames)
            _messageHandlers.add(new MessageHandler(this, "onMessageHandler_", mName));
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    /**
     * 
     */
    public void startGame() {
        if (client == null || !client.isConnected())
            return;
        runGameLoop();
    }

    /**
     * Запускает цикл игры в отдельном потоке
     */
    private void runGameLoop() {
        thGame = new Thread(new gameLoop());
        thGame.setName("Game loop");
        thGame.start();
    }

    /**
     * Разбирает принятые сообщения
     * @param mesg 
     */
    public void parseAllString(String bigMesg) {
        System.out.println(bigMesg);
        String delim = "\n";
        String[] data = bigMesg.split(delim);
        for (String s : data)
            parseMessage(s);
        /*
        if (gameInterface != null) {
            gameInterface.drawAll();
        }*/
    }
    
    public void parseMessage(String s) {
        Message m = new Message(null, s);
        for (MessageHandler mh : _messageHandlers)
            if (mh.tryParseMessage(m))
                break;
    }
    
    /**
     * Отправка сообщения на сервер
     * @param mesg 
     */
    public void send(String mesg) {
        client.write(mesg);
    }
    
    private void fillMessageNames() {
        String messages[] = new String[] {};
        _messageNames = new ArrayList<String>();
        _messageNames.addAll(java.util.Arrays.asList(messages));
        fillAdditionalMessageNames();
    }
    
    protected void fillAdditionalMessageNames() {
        
    }
}