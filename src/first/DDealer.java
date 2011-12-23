/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

/**
 *
 * @author Alexey
 */
public class DDealer extends Dealer{
    public DDealer(Deck deck)
    {
        super(deck);
    }
    @Override
    public void initGame()
    {
        /*
         * 1. Проверить колоду
         * 2. Перемешать колоду
         * 3. Раздать карты игрокам
         * 4. Переместить первого ходящего на нулевую позицию
         */
    }
    @Override
    public void play()
    {
        /*
         * 1. Все игроки набирают карты
         * 2. Создаётся новый игровой стол
         * 3. Играть текущий игровой стол
         * 4. Рассчитать номер ходящего следующего хода
         * 5. Перейти к пункту 5 если игра не завершена.
         * 
         * Уточнение "3. Играть текущий игровой стол"
         * а. выбртаь текущего ходящего
         * б. сделать ход
         * в. отбиться
         * г. повтор а., пока все + 1 человек подряд не откажутся от хода
         */
        for (ServPlayer sp : players)
        {
            System.out.print("From player ");
            System.out.print(sp.name);
            System.out.print(" motion: ");
            System.out.print(sp.move("situation"));
        }
    }
    
    
}
