package hwz;

import java.sql.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBconfig {
    private  static  final  String host = "127.0.0.1";
    private  static  final  int port = 3306;
    private  static  final  String user = "root";
    private  static  final  String password = "123456789";
    private  static  final  String database = "db_tangshi";
    private static  DataSource datasource;
    static {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName(host);
        mysqlDataSource.setPort(port);
        mysqlDataSource.setUser(user);
        mysqlDataSource.setPassword(password);
        mysqlDataSource.setDatabaseName(database);
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");
        datasource = mysqlDataSource;
    }
    public static Connection getConnection () throws SQLException {
        return  datasource.getConnection();
    }
}
