/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

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
    
    private class AnsweredMessagePlayer extends AnsweredMessage {
        
        public AnsweredMessagePlayer(NetPack pack) {
            super(pack);
        }

        @Override
        protected void sendNext(String message) {
            parseMessage(name);
        }
        
    }

    private String name;
    private IClient client;
    protected Thread thGame;
    protected String _messageHandlerPrefix;
    private ArrayList<MessageHandler> _messageHandlers;
    
    private AnsweredMessageList _answeredMessage;
    
    public Player(IClient c) {
        client = c;
        _messageHandlers = new ArrayList<MessageHandler>();
        _messageHandlerPrefix = "onMessageHandler_";
        for (String mName : getDeclaredMessageNames(_messageHandlerPrefix))
            _messageHandlers.add(new MessageHandler(this, _messageHandlerPrefix, mName));
        _answeredMessage = new AnsweredMessageList();
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
    public IClient getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(IClient client) {
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
    public void send(NetPack mesg) {
        if (!_answeredMessage.setAnswer(mesg.toString().substring(1, mesg.toString().length() - 1)))
            client.write(mesg);
    }
    
    public LinkedList<String> getDeclaredMessageNames(String prefix) {
        LinkedList<String> answer = new LinkedList<String>();
        for (Method m : this.getClass().getMethods()) {
            Class<?> params[] = m.getParameterTypes();                    
            if (params.length == 1 && params[0].equals(Message.class) && m.getName().startsWith(prefix))
                answer.add(m.getName().replace(prefix, ""));
        }
        return answer;
    }
    
//<editor-fold desc="Message handlers">
    public void onMessageHandler_answered(Message m) {
        final String mesg = m.getMessage();
        (new Thread(new Runnable() {

            @Override
            public void run() {
                send(new NetPack("answer/".concat(_answeredMessage.addNew(new AnsweredMessagePlayer(new NetPack(mesg))).getAnswer())));
            }
        }, "answered message ".concat(m.getName()))).start();
    }
//</editor-fold>
}