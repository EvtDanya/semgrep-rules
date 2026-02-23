import java.sql.DriverManager;
import java.sql.Connection;

public class MysqlJdbcTestCases {

    // TP: autoDeserialize=true with user-controlled host
    public Connection connectUnsafe(String host) throws Exception {
        // ruleid: mysql-jdbc-autodeserialize
        return DriverManager.getConnection(
            "jdbc:mysql://" + host + ":3306/mydb?autoDeserialize=true"
        );
    }

    // TN: no autoDeserialize
    public Connection connectSafe(String host) throws Exception {
        // ok: mysql-jdbc-autodeserialize
        return DriverManager.getConnection(
            "jdbc:mysql://" + host + ":3306/mydb?useSSL=true"
        );
    }
}