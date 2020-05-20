package banking;

import java.security.SecureRandom;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        dataBase.createTable();

        Scanner sc = new Scanner(System.in);
        SecureRandom rand = new SecureRandom();

        Bank bank = new Bank();
        bank.open(sc, rand, dataBase);
    }
}
