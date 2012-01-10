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
        thGame.start();
    }

    /**
     * Разбирает принятые сообщения
     * @param mesg 
     */
    public abstract void parseAllString(String mesg);
}