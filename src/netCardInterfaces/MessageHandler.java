/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class MessageHandler {
    private String _messageName;
    private Method _messageMethod;
    private Object _object;
    public MessageHandler(Object object, String prefix, String messageName) {
        _messageName = messageName;
        _object = object;
        _messageMethod = null;
        try {
            _messageMethod = object.getClass().getMethod(prefix.concat(messageName), Message.class);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean tryParseMessage(Message m) {
        if (_messageName != null && _messageName.equals(m.getName()))
            try {
            _messageMethod.invoke(_object, m);
            return true;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean isHandler(Object o) {
        return o != null && o.equals(_object);
    }
}
