/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class Card {
    public enum Color {clubs, spades, hearts, diamonds};
    public enum Value {c2, c3, c4, c5, c6, c7, c8, c9, c0, cJ, cQ, cK, cA, cj};
    private Color color;
    private Value value;
    public Color getColor()
    {
        return color;
    }
    public Value getValue()
    {
        return value;
    }
    public Card(Value v, Color c)
    {
        value = v;
        color = c;
    }
    
    public static Card fromString(String name)
    {
        Value v;
        try
        {
            v = Value.valueOf(name.substring(0, name.length() - 2));
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
    public static Boolean isEqual(Card c1, Card c2)
    {
        return (c1.getColor().equals(c2.getColor())) && (c1.getValue().equals(c2.getValue()));
    }
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
