/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

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
    
    public Player(Client c) {
        client = c;
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
     * Осуществляет соединение по IP-адресу, порту 
     * и начинает игру
     * @param address
     * @param port
     * @throws Exception 
     */
    public void startGame(byte address[], int port) throws Exception {
        String addr = "";
        for (int i = 0; i < address.length; i++)
        {
            addr = addr.concat(String.valueOf(address[i]));
            if (i != address.length - 1)
                addr = addr.concat(".");
        }
        if (client.tryConnectTo(addr, port)) 
            runGameLoop();
        else
            throw new Exception("Ошибка при подключении");
    }
    public void startGame(String address, int port) throws Exception {
        if (client.tryConnectTo(address, port))
            runGameLoop();
        else
            throw new Exception("Ошибка при подключении");
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
    public abstract void parseAllString(String mesg);
    
    /**
     * Отправка сообщения на сервер
     * @param mesg 
     */
    public void send(String mesg) {
        client.write(mesg);
    }
}