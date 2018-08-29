package com.alexe1ka;

import jdk.nashorn.internal.ir.LiteralNode;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class CsvToDbMain {

    private static final String INSERT_QUERY = "INSERT INTO ${table}(${keys}) VALUES(${values})";
    private static final String TABLE_REGEX = "\\$\\{table\\}";
    private static final String KEYS_REGEX = "\\$\\{keys\\}";
    private static final String VALUES_REGEX = "\\$\\{values\\}";

    public static void main(String[] args) {
        CsvDataReader csvReader = new CsvDataReader("data.csv");
        List<String[]> dataList = csvReader.getAllDataFromCsv();
//        csvReader.printDataFromCsvToConsole();
        CsvToDbMain csvToDbMain = new CsvToDbMain();
        csvToDbMain.writeParseDataToDb(dataList);
    }

    public void writeParseDataToDb(List<String[]> dataList) {
        //write to database
        String url = "jdbc:postgresql://localhost:5433/test_database";
        String user = "test";
        String password = "test";


        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Opened database successfully");
            statement = connection.createStatement();
            //запрос для создания таблицы.названия колонок таблицы должны быть в первой строке csv файла
            //второй вариант - игнорировать первую строку с именами колонок в файле и в запросе непосредственно прописать названия колонок таблицы в бд
            StringBuilder sql = new StringBuilder();
            sql.append("drop table if exists data;");//TODO старая таблица УДАЛЯЕТСЯ
            sql.append("CREATE TABLE IF not  EXISTS DATA (");
            sql.append(dataList.get(0)[0]).append(" VARCHAR(100) Primary Key not null,");
            sql.append(dataList.get(0)[1]).append(" VARCHAR(100) not null,");
            sql.append(dataList.get(0)[2]).append(" int not null,");
            sql.append(dataList.get(0)[3]).append(" VARCHAR(100) not null)");
            statement.executeUpdate(sql.toString());
            System.out.println("Table created");

            //генерация универсального запроса для вставки
            String query = INSERT_QUERY.replaceFirst(TABLE_REGEX, "data");
            query = query.replaceFirst(KEYS_REGEX, StringUtils.join(dataList.get(0), ","));

            //c 1 потому что в 0 строке название столбцов
            for (int i = 1; i < dataList.size(); i++) {
                String values = createInsertBodyQuery(dataList.get(i));
                String insertQuery = query.replaceFirst(VALUES_REGEX, values);
                System.out.println("Query: " + insertQuery);
                statement.executeUpdate(insertQuery);
            }
//            statement.close();
//            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private String createInsertBodyQuery(String[] list) {
        StringBuilder insertBody = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (list[i].matches("[-+]?\\d+")) {
                insertBody.append(list[i]);
            } else {
                insertBody.append("'").append(list[i]).append("'");
            }
            if (i != list.length - 1) {
                insertBody.append(",");
            }
        }
        return insertBody.toString();
    }
}
