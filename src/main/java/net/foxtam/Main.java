package net.foxtam;

import net.foxtam.io.FileIO;
import net.foxtam.linearequations.LinearEquationsSystem;
import net.foxtam.linearequations.Solution;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFileName = getArg("-in", args);
        String outputFileName = getArg("-out", args);

        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        double[][] coefficients = FileIO.readMatrixFromFile(inputFile);

        LinearEquationsSystem equations = LinearEquationsSystem.fromArray(coefficients);
        Solution result = equations.getSolution();

        FileIO.writeArrayToFile(outputFile, result);
    }

    private static String getArg(String key, String[] args) {
        int index = Arrays.asList(args).indexOf(key);
        if(index == -1) {
            throw new IllegalArgumentException("Key not found");
        }
        return args[index + 1];
    }
}
