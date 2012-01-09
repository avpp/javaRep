/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

//Dealer = тот, который следит за правилами игры. фактически он изменяет состояние игры.

import durakServer.DAdmin;
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

import durakVisualClient.*;
import java.io.*;

/**
 * 
 * @author Alexey
 */
public class First {

    /**
     * @param args the command line arguments
     * @throws IOException  
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //MainForm.main(args);/*
        System.out.println("Choose type\n1.Admin\n2.Client\n");
        int choice = System.in.read();
        if (choice=='1')
        {
            MainForm.main(args);
        }
        /*
        if (choice == '1')
        {
            Admin a = new DAdmin();
            a.StartServer();
            a.startGathering();
            a.createDealer();
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
            a.waitEndGame();
            System.out.println(a.dealer.wtable.toString());
            a.stopServer();
        }*/
        else if (choice == '2') {
            DurakPlayer dc = new DurakPlayer();
            
            //String str;
            byte address[] = new byte[4];
            //address[0] = 2;
            //address[1] = 95;
            //address[2] = -42;
            //address[3] = -5;
            address[0] = 127;
            address[1] = 0;
            address[2] = 0;
            address[3] = 1;
            int port = 15147;
            
            //System.out.println("Введи ip, через пробел порт");
            //InputStreamReader input = new InputStreamReader(System.in);
            //BufferedReader reader = new BufferedReader(input);
            
            //str = reader.readLine();
            //String[] data = str.split(",");
            //address = data[0];
            //port = Integer.parseInt(data[1]);
            
            System.out.println("Connecting to " + 
                    address + " " + String.valueOf(port));
            
            dc.runLoop(address, port);
        }/**/
    }
}
