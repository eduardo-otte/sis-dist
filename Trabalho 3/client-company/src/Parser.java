import java.util.Scanner;

public class Parser {
    public static int parseIntegerInput(String message) {
        Scanner keyboard = new Scanner(System.in);
        boolean correctInputType = false;
        int result = -1;

        while (!correctInputType) {
            System.out.print(message);
            String input = keyboard.nextLine();
            try {
                result = Integer.parseInt(input);
                correctInputType = true;
            } catch (Exception e) {
                System.out.println("Insira um número!");
            }
        }

        return result;
    }

    public static float parseFloatInput(String message) {
        Scanner keyboard = new Scanner(System.in);
        boolean correctInputType = false;
        float result = -1;

        while (!correctInputType) {
            System.out.print(message);
            String input = keyboard.nextLine();
            try {
                result = Float.parseFloat(input);
                correctInputType = true;
            } catch (Exception e) {
                System.out.println("Insira um número!");
            }
        }

        return result;
    }
}
