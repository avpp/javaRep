/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.util.LinkedList;

/**
 * Класс описывает абстрактного диллера (заведующего игрой)
 * Для каждой игры реализуется свой диллер, который следит за ходом игры и даёт право игрокам делать ходы. При этом все изменения диллер делает сам
 * @author Alexey
 */
public abstract class Dealer implements Runnable {
    /**
     * Список игроков, учавствующих в игре
     */
    public LinkedList<GamePlayer> players;
    /**
     * Таблица победителей в данной игре
     */
    public WinTable wtable;
    /**
     * Колода карт, используемая в данной игре
     */
    public Deck deck;
    /**
     * История игровых столов данной игры
     */
    public History history;
    /**
     * Администратор, который заведует взаимодействием между игроками
     */
    protected Admin admin;
    /**
     * Конструктор, который создаёт диллера
     * @param deck Колода, используемая в данной игре
     * @param admin Администратор, заведующий данной игрой
     */
    public Dealer(Deck deck, Admin admin)
    {
        this.admin = admin;
        this.deck = deck;
        players = new LinkedList<GamePlayer>();
        history = new History();
        wtable = new WinTable();
    }
    /**
     * Абстрактный метод, который должен проверять колоду на пригодность к данной игре
     * @param d колода
     * @return Возвращает true, если колода пригодна для игры
     */
    public abstract Boolean checkDeck(Deck d);
    /**
     * Абстрактный метод, который инициализирует игру
     * @return Возвращает true, если инициализация прошла хорошо и можно начинать игру, иначе false
     */
    public abstract Boolean initGame();
    /**
     * Тоже самое, что и {@link initGame()}, только с возможностью задать новую колоду
     * @param deck новая колода
     */
    public void initGame(Deck deck)
    {
        this.deck = deck;
        initGame();
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
