package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.csvGenerators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class PensionerCsvGenerator {

    public static void main(String[] args) {
        String filename = "src/main/resources/test/testingCsv/invalidPensioners.csv";
        generateCSV(filename, 5000);
    }

    public static void generateCSV(String filename, int numberOfRows) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("type,id,firstName,lastName,peselNumber,height,weight,email,version,pensionValue,workYears\n");

            for (int i = 0; i < numberOfRows; i++) {
                String id = UUID.randomUUID().toString();
                String firstName = "Bartek";
                String lastName = "Maciejoszka";
                String peselNumber = String.valueOf(51 + i);
                double height = 170 + Math.random() * 30;
                double weight = 60 + Math.random() * 40;
                String email = "jankowal@gmail.com";
                int version = 1;
                double pensionValue = 2000;
                int workYears = 30;

                writer.write(String.format("pensioner,%s,%s,%s,%s,%.0f,%.0f,%s,%d,%s,%d\n",
                        id, firstName, lastName, peselNumber, height, weight, email, version, pensionValue, workYears));
            }

            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


