/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.LinkedList;

/**
 *
 * @author Alexey
 */
public class NetPack {
    private LinkedList<String> _messages;
    public NetPack() {
        _messages = new LinkedList<String>();
    }
    
    public NetPack(String mesg) {
        _messages = new LinkedList<String>();
        _messages.add(mesg);
    }
    
    public void addMessage(String mesg) {
        _messages.add(mesg);
    }
    
    public boolean isEmpty() {
        return _messages.isEmpty();
    }
    
    public String getMessage() {
        return isEmpty()?null:_messages.getFirst();
    }
    
    @Override
    public String toString() {
        String answer = "@\n";
        for (String s : _messages) {
            answer = answer.concat(s).concat("\n");
        }
        answer = answer.concat("\n@");
        
        return answer;
    }
    
    public static NetPack fromString(String s) {
        NetPack answer = new NetPack();
        if (s.matches("^@.*@$")){
            String messages[] = s.substring(1, s.lastIndexOf("@")).split("\n");
            for (String mesg : messages) {
                if (!"".equals(mesg)) {
                    answer.addMessage(mesg);
                }
            }
        }
        return answer;
    }
}
