package com.dormitory.util;

import java.util.Scanner;

public class ConsoleUtil {
    private static Scanner scanner=new Scanner(System.in);

    public static String readString(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt){
        while(true){
            try{
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("请输入数字：");
            }
        }
    }

    public static Long readLong(String prompt){
        while(true){
            try{
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("请输入数字：");
            }
        }
    }

    public static String readPassword(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readName(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readPhone(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static boolean confirm(String prompt){
        System.out.print(prompt+"{y/n):");
        String input=scanner.nextLine().trim().toLowerCase();
        return input.equals("y")||input.equals("yes");
    }
}
