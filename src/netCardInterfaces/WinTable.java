/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

import java.util.LinkedList;

/**
 * Экзепляр данного класса хранит в себе Список игроков и их очки, полученные в ходе игры
 * Использование данного класса:
 * 1. добавляем вышедших "одновременно" из игры игроков с помощью метода {@link addPlayer(InGamePlayer p)}
 * 2. вызываем метод {@link incScore()}, для увеличения очков у всех игроков.
 * @author Alexey
 */
public class WinTable {
    /**
     * Данный класс представлет одну запись табдицы
     */
    public class rec {
        private InGamePlayer sp;
        private int score;
        /**
         * Создание записи для игрока (изначально имеет счёт ноль)
         * @param p игрок, для которого создаётся запись
         */
        public rec(InGamePlayer p)
        {
            sp = p;
            score = 0;
        }
        /**
         * получитьс счёт данной записи
         * @return счёт
         */
        public int getScore()
        {
            return score;
        }
        /**
         * получить игрока данной записи
         * @return игрок
         */
        public InGamePlayer getPlayer()
        {
            return sp;
        }
        /**
         * увелисить счёт данной записи на 1
         */
        public void incScore()
        {
            score++;
        }
    }
    /**
     * список записей
     */
    public LinkedList<rec> records;
    /**
     * Конструктор таблицы
     */
    public WinTable()
    {
        records = new LinkedList<WinTable.rec>();
    }
    /**
     * Добавление игрока в таблицу
     * @param p 
     */
    public void addPlayer(InGamePlayer p)
    {
        records.add(new rec(p));
    }
    /**
     * Увеличение счёта всех записей на 1
     */
    public void incScore()
    {
        for (rec r : records)
        {
            r.incScore();
        }
    }
    /**
     * Золучение счёта определённого игрока
     * @param p игрок, для которого требуется узнать счёт
     * @return счёт игрока
     */
    public int getScore(InGamePlayer p)
    {
        int i;
        for (i = 0; i < records.size() && !records.get(i).getPlayer().equals(p); i++);
        return records.get(i).getScore();
    }
    
    public int indexOf(InGamePlayer p) {
        for (rec r : records) {
            if (r.getPlayer().equals(p))
                return records.indexOf(r);
        }
        return -1;
    }
    public String valueToString() {
        String ans = "";
        for(rec r : records)
        {
            ans = ans.concat(r.getPlayer().getName()).concat(",").concat(String.valueOf(r.getPlayer().getCurrentAmount())).concat(",");
        }
        return ans;
    }
    
    /**
     * Перевод таблицы в строковое представление (формат: wint/<Имя_игрока>,<Имя_игрока>,...
     * @return строковое представление таблицы (перечисление всех игроков в порядке выхода изигры)
     */
    @Override
    public String toString()
    {
        String ans = "wint/";
        for(rec r : records)
        {
            ans = ans.concat(r.getPlayer().getName()).concat(",");
        }
        ans = ans.concat("\n");
        return ans;
    }
}
