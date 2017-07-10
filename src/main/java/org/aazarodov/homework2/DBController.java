package org.aazarodov.homework2;

import java.sql.*;

public class DBController {
    private static Connection connection;
    private static Statement statement;

    public DBController() {}

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        statement = connection.createStatement();
    }

    public void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTableProducts() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS products (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    prodid TEXT,\n" +
                "    title TEXT,\n" +
                "    cost INTEGER\n" +
                ");");
    }

    public void dropTableProducts() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS products;");
    }

    public void fillTableProducts() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO products (prodid, title, cost) VALUES (?, ?, ?);");
        connection.setAutoCommit(false);
        for (int i = 1; i <= 10000; i++) {
            ps.setString(1, "id_товар" + i);
            ps.setString(2, "товар" + i);
            ps.setInt(3, i * 10);
            ps.addBatch();
        }
        ps.executeBatch();
        connection.setAutoCommit(true);
    }

    public void getCostByTitle(String title) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT cost FROM products WHERE title = ?");
        ps.setString(1, title);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        if (rs.next()) {
            System.out.println("Cost '" + title + "': " + rs.getInt(1));
        } else {
            System.out.println("Product not found!");
        }
    }

    public void changeCostByTitle(String title, int cost) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE products SET cost = ? WHERE title = ?");
        ps.setInt(1, cost);
        ps.setString(2, title);
        ps.execute();
    }

    public void getProductByIntervalId(int idStart, int idEnd) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT prodid, title, cost FROM products WHERE id BETWEEN ? AND ?");
        ps.setInt(1, idStart);
        ps.setInt(2, idEnd);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        System.out.println("prodid  title   cost");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "   " + rs.getInt(3));
        }
    }
}
