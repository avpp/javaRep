/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Класс описывает абстрактного дилера (заведующего игрой)
 * Для каждой игры реализуется свой диллер, который следит за ходом игры и даёт право игрокам делать ходы. При этом все изменения диллер делает сам
 * @author Alexey
 */
public abstract class Dealer implements Runnable {
    
    /**
     * Список игроков, участвующих в игре
     */
    public LinkedList<InGamePlayer> players;
    
    /**
     * Таблица победителей в данной игре
     */
    public WinTable wtable;
    
    /**
     * Колода карт, используемая в данной игре
     */
    public Deck _deck;
    
    /**
     * История игровых столов данной игры
     */
    public History history;
    
    /**
     * Администратор, который заведует взаимодействием между игроками
     */
    protected Admin _admin;
    
    public LinkedList<String> getDeclaredMessageNames(String prefix) {
        LinkedList<String> answer = new LinkedList<String>();
        for (Method m : this.getClass().getMethods()) {
            Class<?> params[] = m.getParameterTypes();                    
            if (params.length == 1 && params[0].equals(Message.class) && m.getName().startsWith(prefix))
                answer.add(m.getName().replace(prefix, ""));
        }
        return answer;
    }
    
    /**
     * Конструктор, который создаёт диллера
     * @param deck Колода, используемая в данной игре
     * @param _admin Администратор, заведующий данной игрой
     */
    public Dealer(Admin admin)
    {
        this._admin = admin;
        players = new LinkedList<InGamePlayer>();
        history = new History();
        wtable = new WinTable();
    }
    
// <editor-fold desc="Message handlers">
    public void onMessageHandler_lspl(Message m) {
        InGamePlayer gp = m.getSource().getGamePlayer();
        int num = -1;
        if (gp != null) {
            num = players.indexOf(gp);
        }
        String answer = "lspl/".concat(String.valueOf(num)).concat("/");
        for (InGamePlayer g : players) {
            answer = answer.concat(g.getName()).concat(",").concat(String.valueOf(g.getCurrentAmount())).concat(",");
        }
        answer = answer.concat("\nwint/");
        if (gp != null) {
            num = wtable.indexOf(gp);
        }
        answer = answer.concat(String.valueOf(num)).concat(wtable.valueToString());
        m.getSource().sendMessage(new NetPack(answer));
    }
    public void onMessageHandler_deck(Message m) {
        if (_deck == null)
            return;
        m.getSource().sendMessage(new NetPack(_deck.toString()));
    }
    public void onMessageHandler_gamt(Message m) {
        if (history == null)
            return;
        m.getSource().sendMessage(new NetPack(history.getLastTable().toString()));
    }
// </editor-fold>
    public abstract String getOptions();
    public abstract void setOptions(String options);
    public abstract boolean checkPlayer(String s);
    
    protected abstract Deck initDeck();
    protected abstract boolean preparationGame();
    
    public boolean initGame() {
        _deck = initDeck();
        return preparationGame();
    }
    
    /**
     * метод проведения самой игры. Именно в нём реализуется логика игры
     */
    public abstract void play();
    /**
     * метод, который вызывается при создании отдельного потока
     */
    @Override
    public void run() {
        play();
    }
}
