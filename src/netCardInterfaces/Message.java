/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 * Класс Messge хранит в себе текстовое сообщение и отправителя
 * @author Alexey
 */
public class Message {
    
    private ServPlayer _source;
    private String _message, _name;
    
    public Message(ServPlayer source, String message)
    {
        _source = source;
        int index = message.indexOf("/");
        if (index == -1)
            _name = _message = message;
        else
        {
            _name = message.substring(0, index);
            _message = message.substring(index + 1);
        }
    }

    /**
     * @return the source
     */
    public ServPlayer getSource() {
        return _source;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return _message;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }
}
