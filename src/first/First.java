/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

//Dealer = тот, который следит за правилами игры. фактически он изменяет состояние игры.

import java.io.IOException;

//Player = тот, который делает выбор(как походить) в данной игровой ситуации.
////Server = тот, кто инициализирует игру.
//Каждый игрок локально имеет свой сервер/клиент. и имеет соединение с остольными участниками.
//Admin = тот, кто инициализирует игру/создаёт игровую комнату.
//
//Для каждой игры будет один диллер и три типа игроков(непосредственно игрок, игрок-бот, игрок на расстоянии)
//стоп. Синхронизация. какой-то диллер должен быть главным (раздача карт).
//С другой стороны, можно в самом начале передать колоду, её будет тасовать игрок с админскими правами. 
//=> Диллера надо "создавать" с уже готовой колодой или сделать возможность передачи новой колоды.
//
//Observer = тот, кто может просто просматривать состояния игры. по идеи им тоже надо отправлять всю информацию.
//
//Некоторые итоги:
//1. Вся игра проходит локально на каждой машине, общее - информация.
//2. Каждая машина сама отвечает за правильность интерпритации игровой ситуации.
//3. Должна существовать возможность связи машин через другие.

/**
 *
 * @author Alexey
 */
public class First {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Admin a = new DAdmin();
        a.StartServer();
        a.createDealer();
        int choice = 0;
        while (choice != 'n')
        {
            System.out.println("Players: ");
            for (String s : a.getPlayerList())
            {
                System.out.println(s);
            }
            do
            {
                choice = System.in.read();
            }
            while (choice == '\n');
            if (choice >= '0' && choice <= '9')
            {
                choice = choice - 48;
                a.addPlayer(choice);
            }
        }
        a.startGame();
    }
}
