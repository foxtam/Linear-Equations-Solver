package net.foxtam;

import java.io.*;
import java.util.Scanner;

public class FileIO {
    static double[][] readMatrixFromFile(File file) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            int linesCount = Integer.parseInt(scanner.nextLine());
            double[][] array = new double[linesCount][];
            for (int i = 0; i < linesCount; i++) {
                array[i] = parseDoubleArray(scanner.nextLine());
            }
            return array;
        }
    }

    private static double[] parseDoubleArray(String line) {
        String[] numbersAsStrings = line.split("\\s+");
        double[] numbers = new double[numbersAsStrings.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Double.parseDouble(numbersAsStrings[i]);
        }
        return numbers;
    }

    static void writeArrayToFile(File file, double[] array) throws IOException {
        try (PrintWriter fileWriter = new PrintWriter(file)) {
            for (double n : array) {
                fileWriter.println(n);
            }
        }
    }
}
