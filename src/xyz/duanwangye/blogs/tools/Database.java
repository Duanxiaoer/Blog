package xyz.duanwangye.blogs.tools;

import java.sql.*;

import com.mysql.jdbc.Driver;
public class Database {
    private Connection connection;
    private Statement statement;

    public boolean connect_to_database(){
        String driver = "com.mysql.jdbc.Driver";
        String database_name = "duanwangye";
        String user_name = "root";
        String user_password = "dqf009.";
        String url = "jdbc:mysql://localhost/"+database_name+"?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";

        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url,user_name,user_password);

            System.out.println("database connected");
            return true;

        }catch (Exception e){
            System.out.println("database error");
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet select(String table_name,String condition,String values){
        try {
            statement = connection.createStatement();
            String sql = "SELECT "+values+" FROM "+table_name+" WHERE "+condition+" ;";

            System.out.println("query table :" + sql);
            ResultSet resultSet =  statement.executeQuery(sql);
            System.out.println("execute ok :" + sql);
            return  resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean insert(String table_name,String values){
        try {
            statement = connection.createStatement();
            String sql = "INSERT into "+table_name+" VALUES ("+values+") ;";

            System.out.println("insert data :" + sql);
            statement.executeUpdate(sql);
            System.out.println("execute ok :" + sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean update(String table_name,String values,String condition){
        try {
            statement = connection.createStatement();
            String sql = "UPDATE "+table_name+" SET "+values+" WHERE "+condition+";";
            System.out.println("update data :" + sql);
            statement.executeUpdate(sql);
            System.out.println("execute ok :" + sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }



}
