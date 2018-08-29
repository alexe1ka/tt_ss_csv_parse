package com.alexe1ka;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvDataReader {
    private String csvFilePath;
    private List<String[]> dataFromCsv;

    public CsvDataReader(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    private void readDataFromCsv(){
        try (
                Reader reader = Files.newBufferedReader(Paths.get(this.csvFilePath));
                CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
                //skip lines - чтобы не читать строку с названиями колонок
        ) {
            this.dataFromCsv = csvReader.readAll();
        } catch (IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public List<String[]> getDataFromCsv() {
        readDataFromCsv();
        return this.dataFromCsv;
    }

    //вспомогательный метод для вывода данных csv файла в консоль
    public void printDataFromCsvToConsole() {
        for (String[] record : this.dataFromCsv) {
            System.out.println("FIO: " + record[0]);
            System.out.println("CITY : " + record[1]);
            System.out.println("AGE : " + record[2]);
            System.out.println("POSITION : " + record[3]);
            System.out.println("---------------------------");
        }
    }
}
