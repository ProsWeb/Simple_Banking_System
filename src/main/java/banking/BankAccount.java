package banking;

import java.util.Scanner;

public class BankAccount {

    boolean enterAccount(final Scanner sc, final DataBase dataBase,
                      final String account) {

        if (account.length() == 0) {
            System.out.println("\nWrong card number or PIN!\n");
            return true;
        }

        System.out.println("\nYou have successfully logged in!");
        String accountMenu = "1. Balance\n"
                + "2. Add income\n"
                + "3. Do transfer\n"
                + "4. Close account\n"
                + "5. Log out\n"
                + "0. Exit";
        System.out.println(accountMenu);

        boolean logged = true;
        while (logged) {
            switch (sc.nextInt()) {
                case 1:
                    int balance = dataBase.getBalance(account);
                    System.out.println("\nBalance:\n" + balance + "\n");
                    System.out.println(accountMenu);
                    break;
                case 2:
                    addIncome(sc, dataBase, account);
                    System.out.println(accountMenu);
                    break;
                case 3:
                    transferMoney(sc, dataBase, account);
                    System.out.println("\n" + accountMenu);
                    break;
                case 4:
                    closeAccount(dataBase, account);
                    System.out.println("\nYour account removed successfully.\n");
                    logged = false;
                    break;
                case 5:
                    System.out.println("\nYou have successfully logged out!\n");
                    logged = false;
                    break;
                case 0:
                    logged = false;
                    System.out.println("\nBye!");
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        return false;
    }

    private void addIncome(final Scanner sc, final DataBase dataBase,
                           final String account) {

        System.out.println("\nEnter income:");
        int income = sc.nextInt();
        if (income < 0) {
            System.out.println("Can't add negative income\n");
            return;
        }

        dataBase.addMoney(account, income);
        System.out.println("Income were added\n");
    }

    private void transferMoney(final Scanner sc, final DataBase dataBase,
                               final String account) {

        System.out.println("\nEnter receiver's account number:");
        String receiverAccount = sc.next();

        if (receiverAccount.equals(account)) {
            System.out.println("You can't transfer money to the same account!");
        } else if (!checkCardForLuhnAlgorithm(receiverAccount)) {
            System.out.println("Probably you made mistake in card number."
                    + " Please try again!");
        } else if (!dataBase.accountExists(receiverAccount)) {
            System.out.println("Such a card does not exist.");
        }  else {
            System.out.println("How much money do you want to transfer?");
            int moneyToTransfer = sc.nextInt();
            int accountBalance = dataBase.getBalance(account);
            if (accountBalance < moneyToTransfer) {
                System.out.println("Not enough money to transfer");
            } else {
                dataBase.subtractMoney(account, moneyToTransfer);
                dataBase.addMoney(receiverAccount, moneyToTransfer);
            }
        }
    }

    private boolean checkCardForLuhnAlgorithm(final String receiverAccount) {

        String receiverAccountWithoutLastDigit =
                receiverAccount.substring(0, receiverAccount.length() - 1);

        Checker checker = new Checker(receiverAccountWithoutLastDigit);
        int sumOfDigits =
                checker.getSumOfDigits();

        int lastDigit = Integer
                .parseInt(receiverAccount.substring(receiverAccount.length() - 1));

        return (sumOfDigits + lastDigit) % 10 == 0;
    }

    private void closeAccount(final DataBase dataBase, final String account) {

        dataBase.deleteAccount(account);
    }
}
