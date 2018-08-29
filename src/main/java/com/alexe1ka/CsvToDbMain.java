package com.alexe1ka;

import java.sql.*;
import java.util.List;

public class CsvToDbMain {

    public static void main(String[] args) {
        CsvDataReader csvReader = new CsvDataReader("data.csv");
        List<String[]> dataList = csvReader.getAllDataFromCsv();
//        csvReader.printDataFromCsvToConsole();

        //write to database
        String url = "jdbc:postgresql://localhost:5433/test_database";
        String user = "test";
        String password = "test";


        Connection c = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Opened database successfully");

            statement = c.createStatement();

            //универсальный запрос создания таблицы.названия колонок в первой строке
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF not  EXISTS DATA (");
//            System.out.println("datalist[0].len = " + dataList.get(0).length);
            sql.append(dataList.get(0)[0]).append(" VARCHAR(100) Primary Key not null,");
            sql.append(dataList.get(0)[1]).append(" VARCHAR(100) not null,");
            sql.append(dataList.get(0)[2]).append(" int not null,");
            sql.append(dataList.get(0)[3]).append(" VARCHAR(100) not null)");
            System.out.println(sql);
            statement.executeUpdate(sql.toString());
            statement.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
