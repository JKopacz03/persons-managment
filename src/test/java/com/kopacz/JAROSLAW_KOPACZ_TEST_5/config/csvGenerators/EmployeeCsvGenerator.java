package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.csvGenerators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class EmployeeCsvGenerator {

    public static void main(String[] args) {
        String filename = "src/main/resources/test/testingCsv/employees.csv";
        generateCSV(filename, 100000);
    }

    public static void generateCSV(String filename, int numberOfRows) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("type,id,firstName,lastName,peselNumber,height,weight,email,version,workStartDate,actualProfession,salary,numberOfProfessions\n");

            for (int i = 0; i < numberOfRows; i++) {
                String id = UUID.randomUUID().toString();
                String firstName = "John";
                String lastName = "Doe";
                String peselNumber = String.valueOf(8001012141L + i);
                double height = 175.0;
                double weight = 80.0;
                String email = "johndoe@example.com";
                String workStartDate = "20220101";
                String actualProfession = "Programmer";
                double salary = 5000.00;
                int numberOfProfessions = 1;
                int version = 1;

                String salaryFormatted = String.format(Locale.US, "%.2f", salary);

                writer.write(String.format("employee,%s,%s,%s,%s,%.0f,%.0f,%s,%d,%s,%s,%s,%d\n",
                        id, firstName, lastName, peselNumber, height, weight, email, version, workStartDate, actualProfession, salaryFormatted, numberOfProfessions));
            }

            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
