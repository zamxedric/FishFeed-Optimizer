package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private DBConnection(){}

    public static Connection getConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishfeed_optimizer", "root", "");

        return connection;
    }
}