/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 * Экземпляр данного класса представляет игрока в игре
 * @author Alexey
 */
public class GamePlayer implements ICardSource, ICardDestination{
    /**
     * Набор карт данного игрока
     */
    private CardHeap cards;
    
    /**
     * имя данного игрока
     */
    public String getName() {
        return p.getName();
    }
    
    private ServPlayer p;
    
    /**
     * Конструктор данного класса связывает текущий объект с объектом класса {@link ServPlayer}
     * @param pl объект класса {@link ServPlayer}, с которым необходимо связать данный объект
     */
    public GamePlayer(ServPlayer pl)
    {
        p = pl;
        p.setGamePlayer(this);
        cards = new CardHeap();
    }
    
    /**
     * Сделать ход.
     * Данныый метод отсылает игровую ситуацию игроку и ждёт ответа, послечего возвращает его в виде строки
     * @param s игровая ситуация
     * @return ответ игрока
     */
    public String move(String s)
    {
        return p.sendAndWaitAnswer(s);
    }
    
    /**
     * Получить карты игрока в строковом виде
     * @return возвращает карты игрока в формате your/<карта>,<карта>,...
     */
    public String getCards()
    {
        String ans = "your/";
        for (Card c : cards.getCards())
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

    @Override
    public Card fetchCard(Card card, CardCoordinate coordinate) {
        if (cards.getCards().remove(card))
            return card;
        if (coordinate == null || coordinate.getCoordinates().length < 1)
            return null;
        return cards.getCards().remove(coordinate.getCoordinates()[0]);
    }

    @Override
    public void pushCard(Card card, CardCoordinate coordinate) {
        cards.getCards().add(card);
    }
    
    public CardCoordinate getCardCoordinate(Card c)
    {
        Card fcard = null;
        for (Card cc : cards.getCards())
        {
            if (Card.isEqual(cc, c))
            {
                fcard = cc;
                break;
            }
        }
        return new CardCoordinate(new int[] {cards.getCards().indexOf(fcard)});
    }

    @Override
    public int getCurrentAmount() {
        return cards.getCards().size();
    }
}
