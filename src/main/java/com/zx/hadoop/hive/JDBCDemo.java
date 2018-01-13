package com.zx.hadoop.hive;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * author:ZhengXing
 * datetime:2017-12-31 15:48
 */
public class JDBCDemo {


    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from tt";
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);
                System.out.println("name:" + name + "/t" + "age:" + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(connection, statement, resultSet);
        }
    }
}
