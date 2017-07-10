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

    public static void queryProcessing() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("Please, enter the command...");
        while (!(command = scanner.nextLine()).equals("exit")) {
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
                int idStart = Integer.parseInt(buffer.substring(0, buffer.indexOf(" ")));
                int idEnd = Integer.parseInt(buffer.substring(buffer.indexOf(" ") + 1).trim());
                dbController.getProductByIntervalId(idStart, idEnd);
            }
            System.out.println("Please, enter the command...");
        }
    }
}
