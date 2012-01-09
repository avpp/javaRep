/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 * Класс, хранящий в себе один ход
 * @author Alexey
 */
public class Turn {
    /**
     * карта, которой походили
     */
    private Card card;
    /**
     * игрок, который кинул карту
     */
    private GamePlayer player;
    /**
     * Конструктор, который создаёт один ход
     * @param p - игрок, который походил
     * @param c - карта, которой игрок сделал ход
     */
    public Turn (GamePlayer p, Card c)
    {
        card = c;
        player = p;
    }
    /**
     * Возвращает карту, которая хранится в данном ходе
     * @return карта
     */
    public Card getCard()
    {
        return card;
    }
    /**
     * Возвращает игрока, который сделал данный ход
     * @return игрок
     */
    public GamePlayer getPlayer()
    {
        return player;
    }
}
