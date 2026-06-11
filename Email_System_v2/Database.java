package Email_Management.Email_System_v2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    static Connection connect() throws SQLException {

        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EmailSystem",
                "root",
                "Arin@2007"
        );
    }
}