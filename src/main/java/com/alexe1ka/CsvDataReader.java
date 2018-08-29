package com.alexe1ka;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvDataReader {
    private String csvFilePath;
    private List<String[]> allDataFromCsv;

    public CsvDataReader(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }


    //читает именно ВЕСЬ файл.для больших файл так делать нельзя
    private void readDataFromCsv() {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(this.csvFilePath));
                //skip lines - чтобы не читать строку с названиями колонок
                //TODO но можно оставить и по первой строке генерить запрос на создание таблицы
//                CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
                CSVReader csvReader = new CSVReader(reader);
        ) {
            this.allDataFromCsv = csvReader.readAll();
        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public List<String[]> getAllDataFromCsv() {
        readDataFromCsv();
        return this.allDataFromCsv;
    }

    //вспомогательный метод для вывода данных csv файла в консоль
    public void printDataFromCsvToConsole() {
        if (this.allDataFromCsv == null) {
            throw new UnsupportedOperationException("Сначала необходимо считать данные из файла");
        }
        for (String[] record : this.allDataFromCsv) {
            System.out.println("0 : " + record[0]);
            System.out.println("1 : " + record[1]);
            System.out.println("2 : " + record[2]);
            System.out.println("3 : " + record[3]);
            System.out.println("---------------------------");
        }
    }
}
