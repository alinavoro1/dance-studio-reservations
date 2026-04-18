package main.util;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String citesteText(String mesaj){
        System.out.println(mesaj);
        return scanner.nextLine().trim();
    }

    public static int citesteInt(String mesaj){
        while(true){
            System.out.println(mesaj);
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e){
                System.out.println("Eroare: te rog sa introduci un numar");
            }
        }
    }
}
