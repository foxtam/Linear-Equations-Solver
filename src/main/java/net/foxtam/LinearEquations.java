package net.foxtam;

import java.util.Arrays;

public class LinearEquations {

    private final double[][] equations;
    private double[] solution;

    private LinearEquations(double[][] equations) {
        this.equations = copyArray2D(equations);
    }

    private double[][] copyArray2D(double[][] origin) {
        double[][] copy = Arrays.copyOf(origin, origin.length);
        for (int i = 0; i < origin.length; i++) {
            copy[i] = Arrays.copyOf(origin[i], origin[i].length);
        }
        return copy;
    }

    public static LinearEquations fromMatrix(double[][] equationCoefficients) {
        return new LinearEquations(equationCoefficients);
    }

    public double[] getSolution() {
        if (solution == null) {
            solve();
        }
        return Arrays.copyOf(solution, solution.length);
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
        solution = result;
    }

    private void reduceRow(int column, int row) {
        double multiplier = equations[row][column] / equations[column][column];
        for (int i = 0; i <= equations.length; i++) {
            equations[row][i] -= multiplier * equations[column][i];
        }
    }
}
