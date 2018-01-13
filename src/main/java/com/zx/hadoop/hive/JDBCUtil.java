package com.zx.hadoop.hive;


import java.sql.*;
import java.util.Optional;

/**
 * author:ZhengXing
 * datetime:2017-12-31 15:35
 * 连接hive
 */
public class JDBCUtil {



    private static String driver = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://106.14.7.29:10000/default";

    /**
     * 注册驱动
     */
    static{
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url,"root","root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放连接
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                resultSet = null;
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                statement = null;
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                connection = null;
            }
        }
    }
}
