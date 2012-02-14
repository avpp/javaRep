/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 * Класс, принимающий запросы на подключение
 * @author Alexey
 */
public abstract class Server implements Runnable{
    
    
    protected Admin _admin;
    
    /**
     * Конструктор, инициализирующий сервер
     * @param admin Администратор, управляющий работой сервера
     */
    public Server(Admin admin) {
        _admin = admin;
    }
    
    /**
     * Метод, вызываемый при запуске потока сервер (реализация класса {@link Runnable})
     */
    @Override
    public void run() {
        work();
    }
    
    protected abstract void work();
}
