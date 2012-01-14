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
    
    public Player()
    {
        client = new Client();
    }
    protected class gameLoop implements Runnable{
        @Override
        public void run() {
            while (true) {
                String mesg = client.read();
                parseAllString(mesg);
            }
        }
    }
    
    protected String name;
    protected Client client;
    protected Thread thGame;
    
    public String getName() {
        return name;
    }

    /**
     * Осуществляет соединение по IP-адресу порту 
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
}