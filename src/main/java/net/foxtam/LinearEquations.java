package net.foxtam;

public class LinearEquations {
    public static double[] solve(double[][] equations) {
        // TODO: упорядочить строки так что бы в главной диоганали не было нулей

        nullLowMatrix(equations);
        nullHighMatrix(equations);
        transformToIdentityMatrix(equations);

        return getSolution(equations);
    }

    private static void nullLowMatrix(double[][] equations) {
        for (int column = 0; column < equations.length - 1; column++) {
            for (int row = column + 1; row < equations.length; row++) {
                reduceRow(equations, column, row);
            }
        }
    }

    private static void nullHighMatrix(double[][] equations) {
        for (int column = equations.length - 1; column > 0; column--) {
            for (int row = column - 1; row >= 0; row--) {
                reduceRow(equations, column, row);
            }
        }
    }

    private static void transformToIdentityMatrix(double[][] equations) {
        for (int i = 0; i < equations.length; i++) {
            double multiplier = equations[i][i];
            for (int j = 0; j < equations[0].length; j++) {
                equations[i][j] /= multiplier;
            }
        }
    }

    private static double[] getSolution(double[][] equations) {
        double[] result = new double[equations.length];
        for (int i = 0; i < equations.length; i++) {
            result[i] = equations[i][equations[0].length - 1];
        }
        return result;
    }

    private static void reduceRow(double[][] equations, int column, int row) {
        double multiplier = equations[row][column] / equations[column][column];
        for (int i = 0; i <= equations.length; i++) {
            equations[row][i] -= multiplier * equations[column][i];
        }
    }
}
