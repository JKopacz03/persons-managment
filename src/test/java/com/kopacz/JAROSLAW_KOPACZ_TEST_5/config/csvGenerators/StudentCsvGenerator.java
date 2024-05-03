package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.csvGenerators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class StudentCsvGenerator {

    public static void main(String[] args) {
        String filename = "src/main/resources/test/testingCsv/students.csv";
        generateCSV(filename, 100000);
    }

    public static void generateCSV(String filename, int numberOfRows) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("type,id,firstName,lastName,peselNumber,height,weight,email,version,college,academicYear,scholarship\n");

            for (int i = 0; i < numberOfRows; i++) {
                String id = UUID.randomUUID().toString();
                String firstName = "Bartek";
                String lastName = "Maciejoszka";
                String peselNumber = String.valueOf(51 + i);
                double height = 170 + Math.random() * 30;
                double weight = 60 + Math.random() * 40;
                String email = "jankowal@gmail.com";
                int version = 1;
                String college = "janapawla";
                int academicYear = (int) (1 + Math.random() * 5);
                double scholarship = 20000 + Math.random() * 80000;

                String scholarshipFormatted = String.format(Locale.US, "%.2f", scholarship);

                writer.write(String.format("student,%s,%s,%s,%s,%.0f,%.0f,%s,%d,%s,%d,%s\n",
                        id, firstName, lastName, peselNumber, height, weight, email, version, college, academicYear, scholarshipFormatted));
            }

            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

