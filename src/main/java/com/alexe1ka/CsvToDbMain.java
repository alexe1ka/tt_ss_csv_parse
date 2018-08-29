package com.alexe1ka;

public class CsvToDbMain {

    public static void main(String[] args) {
        CsvDataReader csvReader = new CsvDataReader("data.csv");
        csvReader.getDataFromCsv();
        csvReader.printDataFromCsvToConsole();

    }
}
