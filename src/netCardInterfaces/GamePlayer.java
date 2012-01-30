/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.LinkedList;

/**
 * Экземпляр данного класса представляет игрока в игре
 * @author Alexey
 */
public class GamePlayer {
    /**
     * Набор карт данного игрока
     */
    public LinkedList<Card> cards;
    
    /**
     * имя данного игрока
     */
    public String name;
    
    private ServPlayer p;
    
    /**
     * Конструктор данного класса связывает текущий объект с объектом класса {@link ServPlayer}
     * @param pl объект класса {@link ServPlayer}, с которым необходимо связать данный объект
     */
    public GamePlayer(ServPlayer pl)
    {
        p = pl;
        p.setGamePlayer(this);
        name = pl.name;
        cards = new LinkedList<Card>();
    }
    
    /**
     * Сделать ход.
     * Данныый метод отсылает игровую ситуацию игроку и ждёт ответа, послечего возвращает его в виде строки
     * @param s игровая ситуация
     * @return ответ игрока
     */
    public String move(String s)
    {
        return p.move(s);
    }
    
    /**
     * Получить карты игрока в строковом виде
     * @return возвращает карты игрока в формате your/<карта>,<карта>,...
     */
    public String getCards()
    {
        String ans = "your/";
        for (Card c : cards)
        {
            ans = ans.concat(c.toString()).concat(",");
        }
        ans = ans.concat("\n");
        return ans;
    }
    
    /**
     * Отправить карты игрока конкретному инроку
     */
    public void sendCards()
    {
        p.sendMessage(getCards());
    }
}
