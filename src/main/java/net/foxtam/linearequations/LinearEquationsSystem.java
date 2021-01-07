package net.foxtam.linearequations;

import java.util.Arrays;

public class LinearEquationsSystem {

    private final double[][] equations;
    private ShellDoubleArray solution;

    private LinearEquationsSystem(double[][] equations) {
        this.equations = copyArray2D(equations);
    }

    private double[][] copyArray2D(double[][] origin) {
        double[][] copy = Arrays.copyOf(origin, origin.length);
        for (int i = 0; i < origin.length; i++) {
            copy[i] = Arrays.copyOf(origin[i], origin[i].length);
        }
        return copy;
    }

    public static LinearEquationsSystem fromMatrix(double[][] equationCoefficients) {
        return new LinearEquationsSystem(equationCoefficients);
    }

    public ShellDoubleArray getSolution() {
        if (solution == null) {
            solve();
        }
        return solution;
    }

    private void solve() {
        // TODO проверить отсутствие решений
        nullLowMatrix();
        nullHighMatrix();
        transformToIdentityMatrix();
        setSolution();
    }

    private void nullLowMatrix() {
        for (int column = 0; column < equations.length - 1; column++) {
            for (int row = column + 1; row < equations.length; row++) {
                reduceRow(column, row);
            }
        }
    }

    private void nullHighMatrix() {
        for (int column = equations.length - 1; column > 0; column--) {
            for (int row = column - 1; row >= 0; row--) {
                reduceRow(column, row);
            }
        }
    }

    private void transformToIdentityMatrix() {
        for (int i = 0; i < equations.length; i++) {
            double multiplier = equations[i][i];
            for (int j = 0; j < equations[0].length; j++) {
                equations[i][j] /= multiplier;
            }
        }
    }

    private void setSolution() {
        double[] result = new double[equations.length];
        for (int i = 0; i < equations.length; i++) {
            result[i] = equations[i][equations[0].length - 1];
        }
        solution = new ShellDoubleArray(result);
    }

    private void reduceRow(int column, int row) {
        double multiplier = equations[row][column] / equations[column][column];
        for (int i = 0; i < equations[0].length; i++) {
            equations[row][i] -= multiplier * equations[column][i];
        }
    }
}
