/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durak;


/**
 *
 * @author Andrew
 */
//Локальное представление колоды клиентом
public class DurakDeck {
    private int m_currAmount;
    private int m_fullAmount;

    public DurakDeck(int amount) {
        m_currAmount = amount;
    }
    
    public int getCurrentAmount() {
        return m_currAmount;
    }
    
    public void setCurrentAmount(int amount) {
        m_currAmount = amount;
    }
}
