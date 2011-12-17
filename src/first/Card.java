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
    public enum Value {c2, c3, c4, c5, c6, c7, c8, c9, c10, cJ, cQ, cK, cA, cjoker};
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
    public String getName()
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
