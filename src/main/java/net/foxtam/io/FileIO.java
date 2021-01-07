package net.foxtam.io;

import net.foxtam.linearequations.ShellDoubleArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileIO {
    public static double[][] readMatrixFromFile(File file) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            int linesCount = Integer.parseInt(scanner.nextLine());
            double[][] array = new double[linesCount][];
            for (int i = 0; i < linesCount; i++) {
                array[i] = parseDoubleArray(scanner.nextLine());
            }
            return array;
        }
    }

    public static double[] parseDoubleArray(String line) {
        String[] numbersAsStrings = line.split("\\s+");
        double[] numbers = new double[numbersAsStrings.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Double.parseDouble(numbersAsStrings[i]);
        }
        return numbers;
    }

    public static void writeArrayToFile(File file, ShellDoubleArray array) throws IOException {
        try (PrintWriter fileWriter = new PrintWriter(file)) {
            for (int i = 0; i < array.size(); i++) {
                fileWriter.println(array.get(i));
            }
        }
    }
}
