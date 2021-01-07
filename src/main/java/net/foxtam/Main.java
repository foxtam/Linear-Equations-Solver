package net.foxtam;

import net.foxtam.io.FileIO;
import net.foxtam.linearequations.LinearEquationsSystem;
import net.foxtam.linearequations.ShellDoubleArray;

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

        LinearEquationsSystem equations = LinearEquationsSystem.fromMatrix(coefficients);
        ShellDoubleArray result = equations.getSolution();

        FileIO.writeArrayToFile(outputFile, result);
    }

    private static String getArg(String key, String[] args) {
        int index = Arrays.asList(args).indexOf(key);
        assert index >= 0;
        return args[index + 1];
    }
}
