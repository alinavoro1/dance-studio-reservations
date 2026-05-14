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
    public static int citesteIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = citesteInt(prompt);
            if (val >= min && val <= max) return val;
            System.out.println("Valoare invalida. Te rog introdu un numar intre " + min + " si " + max + ".");
        }
    }

    public static String citesteNonEmptyString(String prompt) {
        while (true) {
            String s = citesteText(prompt);
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Valoare invalida. Nu poate fi gol. Reincearca.");
        }
    }
}
