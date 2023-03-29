package client;

import java.util.Scanner;

class UserInput {
    static int getInt() {
        Scanner sc = new Scanner(System.in);
        while (!sc.hasNextInt()) {
            System.out.println("Please, only numbers.");
            sc.nextLine();
        }
        return sc.nextInt();
    }
    static int getInt(int min, int max){
        Scanner scanner = new Scanner(System.in);
        int number;
        do{
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a number between " + min + " and " + max);
                scanner.nextLine();
            }
            number = scanner.nextInt();
        } while (min > number || max < number);
        return number;

    }
    static Float getFloat() {
        Scanner sc = new Scanner(System.in);
        while (!sc.hasNextFloat()) {
            System.out.println("Please, only numbers.");
            sc.nextLine();
        }
        return sc.nextFloat();
    }
    static String getString(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();

    }
    static String getName(){
        Scanner sc = new Scanner(System.in);
        String name;
        do {
            System.out.println("Please enter name. Max 16 characters.");
            name = sc.nextLine();
        } while ((name.length() > 16) || name.isBlank());
        return name;
    }

    static boolean getYesOrNo() {
        Scanner sc = new Scanner(System.in);
        String s;
        do {
            System.out.println("y/n");
            s = sc.nextLine();
            if (s.equalsIgnoreCase("y")) {
                return true;
            } else if (s.equalsIgnoreCase("n")) {
                return false;
            }
        } while (!s.equals("y") && !s.equals("n"));
        return true;
    }

    static char getChar() {
        Scanner sc = new Scanner(System.in);
        String guessedLetter;
        do {
            System.out.println("Please enter a letter.");
            guessedLetter= sc.nextLine();
        } while (guessedLetter.length() != 1 || !Character.isLetter(guessedLetter.charAt(0)));
        return guessedLetter.toLowerCase().charAt(0);
    }
}
