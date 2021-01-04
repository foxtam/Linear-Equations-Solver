package net.foxtam;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFileName = getArg("-in", args);
        String outputFileName = getArg("-out", args);

        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        double[][] linearEquations = FileIO.readMatrixFromFile(inputFile);

        double[] result = LinearEquations.solve(linearEquations);

        FileIO.writeArrayToFile(outputFile, result);
    }

    private static String getArg(String key, String[] args) {
        int index = Arrays.asList(args).indexOf(key);
        assert index >= 0;
        return args[index + 1];
    }
}
