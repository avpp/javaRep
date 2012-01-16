/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package durakServer;

import first.Admin;
import first.Card;
import first.Dealer;
import first.Deck;
import first.GamblingTable;
import first.GamePlayer;
import first.Turn;
import java.util.LinkedList;

/**
 *
 * @author Alexey
 */
public class DDealer extends Dealer{
    
    private Card.Color trump;
    private Card cardTrump;
    private Boolean endGame;
    
    public DDealer(Deck deck, Admin admin)
    {
        super(deck, admin);
        endGame = true;
    }
    
    private int compareCards(Card c1, Card c2)
    {
        if (c1 == c2)
            return 0;
        if (c1 == null)
            return -1;
        if (c2 == null)
            return 1;
        if (c1.getColor().compareTo(trump) == 0 && c2.getColor().compareTo(trump) != 0)
            return 1;
        if (c1.getColor().compareTo(trump) != 0 && c2.getColor().compareTo(trump) == 0)
            return -1;
        if (c1.getColor().compareTo(c2.getColor()) != 0)
            return 0;
        return c1.getValue().compareTo(c2.getValue());
    }
    private Boolean findCardOnTableWithEqualValue(GamblingTable gt, Card c)
    {
        Boolean ans = false;
        LinkedList<Card> allCards = gt.getAllCards();
        for (int i = 0; i < allCards.size() && !ans; i++)
        {
            ans = allCards.get(i).getValue().equals(c.getValue());
        }
        return ans;
    }
    
    @Override
    public Boolean initGame()
    {
        /*
         * 1. Проверить колоду
         * 2. Перемешать колоду
         * 3. Раздать карты игрокам
         * 3,5. Послать всем игрокам их начальные состояния?????
         * 4. Переместить первого ходящего на нулевую позицию
         */
        if (!checkDeck(deck))
            return false;
        deck.Shuffle(20);
        cardTrump = deck.viewCard(Deck.SideType.low);
        trump = cardTrump.getColor();
        Card maxc = null;
        GamePlayer p = null; //Найдём игрока, который будет ходить
        for (int i = 0; i < 6; i++)
        {
            for (GamePlayer pl:players)
            {
                Card c = deck.getCard(Deck.SideType.top);
                pl.cards.add(c);
                if (compareCards(c, maxc) > 0)
                {
                    maxc = c;
                    p = pl;
                }
            }
        }
        GamePlayer tmpPl;
        //установим найденного игрока на последнюю позицию => отбивающийся игрок будет на позиции 0
        if (players.indexOf(p)*2 > players.size())
        {
            while (players.getLast() != p)
            {
                tmpPl = players.removeLast();
                players.addFirst(tmpPl);
            }
        }
        else
        {
            while (players.getLast() != p)
            {
                tmpPl = players.removeFirst();
                players.addLast(tmpPl);
            }
        }
        endGame = false;
        return true;
    }
    public Boolean lastRemoved, endThrow;
    public int curPlayerBounceIndex, curPlayerMoveIndex, counSeqNoMoves;
    @Override
    public void play()
    {
        /*
         * 1. Создаётся новый игровой стол
         * 2. Играть текущий игровой стол
         * 3. Все игроки набирают карты
         * 3.5 Если кто-то вышел из игры - переносим его в таблицу закончивших
         * 4. Рассчитать номер ходящего следующего хода и, соответственно следующего отбивающегося
         * 5. Перейти к пункту 1 если игра не завершена.
         * 
         * Уточнение "2. Играть текущий игровой стол"
         * (а). сделать ход
         * (б). отбиться
         * (в). повтор (а), пока все + 1 человек подряд не откажутся от хода
         * 
         * Для хода передать:
         * игровой стол
         * карты игрока
         * биться/подкидывать
         * 
         */
        GamblingTable curTable;
        curPlayerBounceIndex = 0;
        curPlayerMoveIndex = players.size() - 1;
        counSeqNoMoves = 0;
        String playerAnswer;
        Boolean endTable;
        admin.AddMessage(null, deck.getInfo());
        admin.AddMessage(null, getTrmp());
        admin.AddMessage(null, "your/");
        while (!endGame)
        {
            lastRemoved = false;
            endTable = false;
            counSeqNoMoves = 0;
            curTable = history.AddNew();  //добавили новый игровой стол.
            
            do
            {
                admin.AddMessage(null, curTable.toString());
                admin.AddMessage(null, getLsPl(curPlayerMoveIndex, curPlayerBounceIndex, (lastRemoved)?'t':'a'));
                Boolean repeatMove;
                // Подкидываем карту
                do
                {
                    repeatMove = false;
                    playerAnswer = players.get(curPlayerMoveIndex).move("turn/+\n");
                    if ("no".equals(playerAnswer))
                    {
                        if (curTable.getAllCards().size() == 0)
                        {
                            repeatMove = true;
                        }
                        else
                        {
                            counSeqNoMoves++;
                            do
                            {
                                curPlayerMoveIndex = (curPlayerMoveIndex + 1) % players.size();
                            } while(curPlayerMoveIndex == curPlayerBounceIndex);
                        }
                    }
                    else
                    {
                        Card c = Card.fromString(playerAnswer);
                        if (c == null)
                        {
                            repeatMove = true;
                        }
                        else
                        {
                            //Здесь проверяем правильность хода
                            Boolean check = false;
                            for (int i = 0; i < players.get(curPlayerMoveIndex).cards.size() && !check; i++)
                            {
                                Card tmpC = players.get(curPlayerMoveIndex).cards.get(i);
                                check = Card.isEqual(c, tmpC);
                                if (check)
                                {
                                    c = tmpC;
                                }
                            }
                            if (!check)
                            {
                                repeatMove = true;
                            }
                            else
                            {//Сюда мы попадём, если если у игрока была такая карта. Проверим, может ли он её положить
                                if (curTable.getAllCards().size() == 0 || findCardOnTableWithEqualValue(curTable, c))//curTable.getAllCards().contains(c))
                                {//Теперь, наконец-то, мы можем фактически сделать ход!!!
                                    curTable.AddTurn(new Turn(players.get(curPlayerMoveIndex), c), -1);
                                    players.get(curPlayerMoveIndex).cards.remove(c);
                                    players.get(curPlayerMoveIndex).sendCards();
                                    //!Надо сообщить всем.
                                }
                                else
                                {
                                    repeatMove = true;
                                }
                            }
                        }
                    }
                } while(repeatMove);
                //Отправляем всем всю информацию
                
                //Отбиваем карту в том случае, если перед этим не затянул и есть что отбивать.
                if (!lastRemoved && curTable.table.get(curTable.table.size() - 1).size() == 1)
                {
                    admin.AddMessage(null, getLsPl(curPlayerMoveIndex, curPlayerBounceIndex, 'p'));
                    admin.AddMessage(null, curTable.toString());
                    do
                    {
                        repeatMove = false;
                        playerAnswer = players.get(curPlayerBounceIndex).move("turn/-\n");
                        if ("no".equals(playerAnswer))
                        {
                            lastRemoved = true;
                        }
                        else
                        {
                            
                            Card c = Card.fromString(playerAnswer);
                            if (c == null)
                            {
                                repeatMove = true;
                            }
                            else
                            {
                                //Здесь проверяем правильность хода
                                Boolean check = false;
                                for (int i = 0; i < players.get(curPlayerBounceIndex).cards.size() && !check; i++)
                                {
                                    Card tmpC = players.get(curPlayerBounceIndex).cards.get(i);
                                    check = Card.isEqual(c, tmpC);
                                    if (check)
                                    {
                                        c = tmpC;
                                    }
                                }
                                if (!check)
                                {
                                    repeatMove = true;
                                }
                                else
                                {//Сюда мы попадём, если если у игрока была такая карта. Проверим, может ли он её положить
                                    Turn lastTurn = curTable.viewTurn(-1, -1);
                                    if (lastTurn != null && (compareCards(c, lastTurn.getCard()) > 0))
                                    {//Теперь, наконец-то, мы можем фактически сделать ход!!!
                                        curTable.AddTurn(new Turn(players.get(curPlayerBounceIndex), c), curTable.table.size() - 1);
                                        players.get(curPlayerBounceIndex).cards.remove(c);
                                        players.get(curPlayerBounceIndex).sendCards();
                                        //!Надо сообщить всем.
                                    }
                                    else
                                    {
                                        repeatMove = true;
                                    }
                                }
                            }
                        
                        }
                    } while(repeatMove);
                    //!Отправляем всем всю информацию
                    //admin.AddMessage(null, getLsPl(curPlayerMoveIndex, curPlayerBounceIndex, 'a'));
                    //admin.AddMessage(null, curTable.toString());
                }
                endTable = (curTable.table.size() == 6) || (counSeqNoMoves >= players.size() - 1) || (lastRemoved && curTable.table.size() == players.get(curPlayerBounceIndex).cards.size());
            } while(!endTable);
            //Закончили один игровой стол, пожалуй стоит раздать всем карты
            //Если отбивающийся снял, то скидываем ему все карты
            if (lastRemoved)
            {
                players.get(curPlayerBounceIndex).cards.addAll(curTable.getAllCards());
            }
            //Набирает тот, кто ходил первым
            curPlayerMoveIndex = (curPlayerBounceIndex + players.size() - 1) % players.size();
            while (deck.size() > 0 && players.get(curPlayerMoveIndex).cards.size() < 6)
            {
                players.get(curPlayerMoveIndex).cards.add(deck.getCard(Deck.SideType.top));
            }
            //Набирают все остальные подкидывающие
            int i = (curPlayerMoveIndex + 2) % players.size();
            while (i != curPlayerMoveIndex)
            {
                while (deck.size() > 0 && players.get(i).cards.size() < 6)
                {
                    players.get(i).cards.add(deck.getCard(Deck.SideType.top));
                }
                i++;
            }
            //Набирает тот, кто отбивался
            while (deck.size() > 0 && players.get(curPlayerBounceIndex).cards.size() < 6)
            {
                players.get(curPlayerBounceIndex).cards.add(deck.getCard(Deck.SideType.top));
            }
            //!Надо бы соощить всемих карты
            admin.AddMessage(null, "your/");
            admin.AddMessage(null, deck.getInfo());
            //Переведём в таблицу победителей тех, кто вышел из игры.
            LinkedList <GamePlayer> addToWinList = new LinkedList<GamePlayer>();
            for (GamePlayer tmpPl : players)
            {
                if (tmpPl.cards.size() == 0)
                    addToWinList.add(tmpPl);
            }
            if (addToWinList.size() > 0)
            {
                for (GamePlayer tmpPl : addToWinList)
                {
                    wtable.addPlayer(tmpPl);
                }
                wtable.incScore();
                players.removeAll(addToWinList);
                //! Надо бы сообщить всем
                //wint+lspl для всех
                admin.AddMessage(null, wtable.toString());
                //admin.AddMessage(null, getLsPl(-1, -1, 'a'));
            }
            //Рассчитаем номер следующего ходящего и отбивающегося
            curPlayerMoveIndex = curPlayerBounceIndex;
            if (lastRemoved)
            {
                curPlayerMoveIndex++;
            }
            curPlayerMoveIndex %= players.size();
            curPlayerBounceIndex = (curPlayerMoveIndex + 1) % players.size();
            //Проверим на конец игры
            endGame = players.size() <= 1;
        }
        //После окончания игры загоняем оставшихся игроков в таблицу победителей
        for (GamePlayer tmpPl : players)
        {
            wtable.addPlayer(tmpPl);
        }
        /*//Test play
        for (ServPlayer sp : players)
        {
            System.out.print("From player ");
            System.out.print(sp.name);
            System.out.print(" motion: ");
            System.out.print(sp.move("situation"));
        }
        */
    }

    @Override
    public Boolean checkDeck(Deck d) {
        if (d == null)
            return false;
        return true;
    }
//список игроков
//игровой стол
//карты игрока
//wint
//колода
//
    public String getLsPl(int indexTh, int indexBo, char modifer)
    {
        //создаём строчку "lspl/indexPl,indexTh,indexBo,modifer/"
        String ans = "lspl/%d,".concat(String.valueOf(indexTh)).concat(",").concat(String.valueOf(indexBo)).concat(",").concat(String.valueOf(modifer)).concat("/");
        for (GamePlayer p : players)
        {
            ans = ans.concat(p.name).concat(",").concat(String.valueOf(p.cards.size())).concat(",");
        }
        ans = ans.concat("\n");
        return ans;
    }
    public String getTrmp()
    {
        return "trmp/".concat(cardTrump.toString()).concat("\n");
    }
    
}
