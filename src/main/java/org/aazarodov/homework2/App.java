package org.aazarodov.homework2;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private static DBController dbController;

    public static void main(String[] args) {
        dbController = new DBController();
        try {
            dbController.connect();
            dbController.dropTableProducts();
            dbController.createTableProducts();
            dbController.fillTableProducts();
            queryProcessing();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbController.disconnect();
        }

    }

    /**
     * Метод выводит подсказку пользователю
     */
    public static void printTip() {
        System.out.println("Пожалуйста, введите команду:");
        System.out.println("- '/цена <title>'");
        System.out.println("- '/сменитьцену <title> <cost>'");
        System.out.println("- '/товарыпоцене <start cost> <end cost>'");
        System.out.println("- '/выход'");
        System.out.println("Ожидание ввода...");
    }

    /**
     * Консольное приложение, ждет команды от пользователя
     * @throws IOException
     * @throws SQLException
     */
    public static void queryProcessing() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        String command;
        printTip();
        while (!(command = scanner.nextLine()).equals("/выход")) {
            if (command.startsWith("/цена")) {
                String title = command.replace("/цена", "").trim();
                dbController.getCostByTitle(title);
            } else if (command.startsWith("/сменитьцену")) {
                String buffer = command.replace("/сменитьцену", "").trim();
                String title = buffer.substring(0, buffer.indexOf(" "));
                int cost = Integer.parseInt(buffer.replace(title, "").trim());
                dbController.changeCostByTitle(title, cost);
            } else if (command.startsWith("/товарыпоцене")) {
                String buffer = command.replace("/товарыпоцене", "").trim();
                int costStart = Integer.parseInt(buffer.substring(0, buffer.indexOf(" ")));
                int costEnd = Integer.parseInt(buffer.substring(buffer.indexOf(" ") + 1).trim());
                dbController.getProductByIntervalId(costStart, costEnd);
            }
            printTip();
        }
    }
}
