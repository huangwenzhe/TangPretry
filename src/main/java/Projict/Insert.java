package Projict;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class Insert {
    public static void main(String[] args) {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName("tangshi");
        dataSource.setPassword("123456789");
        dataSource.setUser("root");
        dataSource.setPort(3306);
        dataSource.setCharacterEncoding("UTF-8");
        dataSource.setUseSSL(false);

    }
}
