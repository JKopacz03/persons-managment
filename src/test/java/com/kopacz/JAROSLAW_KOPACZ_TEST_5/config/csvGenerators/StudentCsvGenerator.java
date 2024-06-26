package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.csvGenerators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class StudentCsvGenerator {

    public static void main(String[] args) {
//        String filename = "src/main/resources/test/testingCsv/students.csv";
        String filename = "src/main/resources/test/testingCsv/invalidStudents.csv";
        generateCSV(filename, 5000);
    }

    public static void generateCSV(String filename, int numberOfRows) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("type,id,firstName,lastName,peselNumber,height,weight,email,version,college,academicYear,scholarship\n");

            for (int i = 0; i < numberOfRows; i++) {
                int id = i+20;
                String firstName = "Jo";
                String lastName = "Do";
                String peselNumber = String.valueOf(51 + i);
                double height = 170 + Math.random() * 30;
                double weight = 60 + Math.random() * 40;
                String email = "j@j.pl";
                int version = 1;
                String college = "ITC";
                int academicYear = (int) (1 + Math.random() * 5);
                double scholarship = 20;

                String scholarshipFormatted = String.format(Locale.US, "%.2f", scholarship);

                writer.write(String.format("student,%d,%s,%s,%s,%.0f,%.0f,%s,%d,%s,%d,%s\n",
                        id, firstName, lastName, peselNumber, height, weight, email, version, college, academicYear, scholarshipFormatted));
            }

            System.out.println("CSV file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

