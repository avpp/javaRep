/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 * Класс Card хранит в себе определённую карту.
 * @author Alexey
 */
public class Card {
    /**
     * Данное перечесление представляет четыре возможных масти
     * clubs = крести (трефы)
     * spades = пики (вини)
     * hearts = червы
     * diamonds = буби
     */
    public enum Color {clubs, spades, hearts, diamonds};
    /**
     * Данное перечисление представляет возможные значения карт
     * c2..c9 = 2..9
     * c0 = 10
     * cJ = Валет
     * cQ = Дама
     * cK = Король
     * cA = Туз
     * cj = Джокер
     */
    public enum Value {c2, c3, c4, c5, c6, c7, c8, c9, c0, cJ, cQ, cK, cA, cj};
    private Color color;
    private Value value;
    /**
     * Возвращает масть данной карты
     * @return масть карты
     */
    public Color getColor()
    {
        return color;
    }
    /**
     * Возвращает значение данной карты
     * @return значение карты
     */
    public Value getValue()
    {
        return value;
    }
    /**
     * Конструктор класса Card
     * @param v значение создаваемой карты
     * @param c масть создаваемой карты
     */
    public Card(Value v, Color c)
    {
        value = v;
        color = c;
    }
    /**
     * Данный статический метод возвращает карту (объект класса Card) по имени.
     * Имя карты можно получить при помощи метода {@link toString()}
     * @param name строковая запись карты
     * @return объект класса Card, эквивалентный строковой записи либо null, если не корректное имя
     */
    public static Card fromString(String name)
    {
        Value v;
        try
        {
            String vStr = name.substring(0, name.length() - 1);
            v = Value.valueOf("c".concat(vStr));
        } catch (IllegalArgumentException ex)
        {
            return null;
        }
        Color c;
        switch (name.charAt(name.length() - 1))
        {
            case '♣' : {c = Color.clubs;}
                break;
            case '♦' : {c = Color.diamonds;}
                break;
            case '♥' : {c = Color.hearts;}
                break;
            case '♠' : {c = Color.spades;}
                break;
            default: {return null;}
        }
        return new Card(v, c);
    }
    /**
     * Данный метод сравнивает карты по значению
     * @param c1 первая карта
     * @param c2 вторая карта
     * @return Возвращает true, если масть и значение первой карты равны масти и значению второй карты, иначе возвращает false
     */
    public static Boolean isEqual(Card c1, Card c2)
    {
        return (c1.getColor().equals(c2.getColor())) && (c1.getValue().equals(c2.getValue()));
    }
    /**
     * Возвращает строковую интерпритацию карты. Возможно обратное преобразование с помощью {@link fromString(String name)}
     * @return строка, содержащие имя анной карты.
     */
    @Override
    public String toString()
    {
        String c = "";
        switch (color)
        {
            case clubs: {c = "♣";}
                break;
            case diamonds: {c = "♦";}
                break;
            case hearts: {c = "♥";}
                break;
            case spades: {c = "♠";}
                break;
        }
        return value.name().substring(1) + c;
    }
}
