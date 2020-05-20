package banking;

import java.security.SecureRandom;
import java.util.Scanner;

public class Bank {

    private boolean banking = true;

    public void open(final Scanner sc, final SecureRandom rand,
                     final DataBase dataBase) {

        String menu = "1. Create account\n"
                + "2. Log into account\n"
                + "0. Exit";
        System.out.println(menu);

        while (banking) {
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    generateCreditCardAndPinCode(rand, dataBase);
                    System.out.println(menu);
                    break;
                case 2:
                    String account = checkCardAndPin(sc, dataBase);
                    BankAccount bankAccount = new BankAccount();
                    boolean logOut = bankAccount
                            .enterAccount(sc, dataBase, account);
                    if (logOut) {
                        System.out.println(menu);
                    } else {
                        banking = false;
                    }
                    break;
                case 0:
                    banking = false;
                    System.out.println("\nBye!");
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private void generateCreditCardAndPinCode(final SecureRandom rand,
                                              final DataBase dataBase) {

        int identificationNumber = 400_000;
        int accountNumberLength = 9;
        int accountNumber = generateNumber(accountNumberLength, rand);
        String creditCardNumberWithoutCheckSum =
                "" + identificationNumber + accountNumber;

        Checker checker = new Checker();
        int checkSum = checker.getCheckSum(creditCardNumberWithoutCheckSum);

        String creditCardNumber = creditCardNumberWithoutCheckSum + checkSum;

        int pinLength = 4;
        String pinCode = String.valueOf(generateNumber(pinLength, rand));

        dataBase.createAccount(creditCardNumber, pinCode);

        System.out.println("\nYour card have been created");
        System.out.println("Your card number:\n" + creditCardNumber
                + "\nYour card PIN:\n" + pinCode + "\n");
    }

    private int generateNumber(final int numberLength,
                               final SecureRandom rand) {

        int m = (int) Math.pow(10, numberLength - 1.00);
        return m + rand.nextInt(9 * m);
    }

    private String checkCardAndPin(final Scanner sc,
                                   final DataBase dataBase) {

        System.out.println("\nEnter your card number:");
        String givenCardNumber = sc.next() + sc.nextLine();
        System.out.println("Enter your PIN:");
        String givenPinCode = sc.nextLine();

        boolean accountExists =
                dataBase.accountExists(givenCardNumber, givenPinCode);
        if (accountExists) {
            return givenCardNumber;
        }

        return "";
    }


}
