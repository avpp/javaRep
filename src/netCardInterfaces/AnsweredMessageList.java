/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexey
 */
public class AnsweredMessageList {
    private ArrayList<AnsweredMessage> _allMessages;
    private Semaphore _semAdd;

    public AnsweredMessageList() {
        _allMessages = new ArrayList<AnsweredMessage>();
        _semAdd = new Semaphore(1);
    }
    
    public AnsweredMessage addNew(AnsweredMessage am){
        if (am == null)
            return null;
        try {
            _semAdd.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(AnsweredMessageList.class.getName()).log(Level.SEVERE, null, ex);
        }
        _allMessages.add(am);
        _semAdd.release();
        return am;
    }
    
    public void remove(AnsweredMessage am) {
        try {
            _semAdd.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(AnsweredMessageList.class.getName()).log(Level.SEVERE, null, ex);
        }
        _allMessages.remove(am);
        _semAdd.release();
    }
    public boolean setAnswer(String s) {
        boolean answer = false;
        try {
            _semAdd.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < _allMessages.size(); i++)
            if (_allMessages.get(i).setAnswer(s)) {
                _allMessages.remove(i);
                answer = true;
                break;
            }
        _semAdd.release();
        return answer;
    }
    
    public boolean setAllAnswers(String s) {
        boolean answer = false;
        try {
            _semAdd.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<AnsweredMessage> delM = new ArrayList<AnsweredMessage>();
        for (int i = 0; i < _allMessages.size(); i++)
            if (_allMessages.get(i).setAnswer(s)) {
                delM.add(_allMessages.get(i));
                answer = true;
            }
        _allMessages.removeAll(delM);
        _semAdd.release();
        return answer;
    }
}
