/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durak;

/**
 *
 * @author Andrew
 */
public class DurakAdversary {
    //Класс для клиента-дурака
    //для внутреннего (локального) представления клиентом игры.
    //Класс содержит имя противника и кол-во его карт,
    //но не хранит никакой информации о картах или соединеннии.
    private String m_name;
    private int m_amountOfCards;
    
    public DurakAdversary(String name, int amountOfCards) {
        m_name = name;
        m_amountOfCards = amountOfCards;
    }
    
    public String getName() {
        return m_name;
    }
    
    public void setName(String name) {
        m_name = name;
    }
    
    public int getAmountOfCards() {
        return m_amountOfCards;
    }
            
    public void setAmountOfCards(int amount) {
        m_amountOfCards = amount;
    }
}
