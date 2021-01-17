package net.foxtam;

import net.foxtam.linearequations.LinearEquationsSystem;
import net.foxtam.linearequations.Solution;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class LinearEquationsSystemTest {

    static Random random = new Random();

    @Test
    public void simple_1() {
        double[][] equation = new double[][]{{1, 1, 2, 9}, {2, 4, -3, 1}, {3, 6, -5, 0}};
        double[] actual = LinearEquationsSystem.fromArray(equation).getSolution().getCoefficients();
        double[] expected = new double[]{1, 2, 3};

        Assert.assertArrayEquals(expected, actual, 1e-9);
    }

    @Test
    public void simple_2() {
        double[][] equation =
                new double[][]{
                        {1, -1, -5},
                        {2, 1, -7}};

        double[] actual = LinearEquationsSystem.fromArray(equation).getSolution().getCoefficients();
        double[] expected = new double[]{-4, 1};

        Assert.assertArrayEquals(expected, actual, 1e-9);
    }

    @Test
    public void simple_3() {
        double[][] equation =
                new double[][]{
                        {3, 2, -5, -1},
                        {2, -1, 3, 13},
                        {1, 2, -1, 9}};

        double[] actual = LinearEquationsSystem.fromArray(equation).getSolution().getCoefficients();
        double[] expected = new double[]{3, 5, 4};

        Assert.assertArrayEquals(expected, actual, 1e-9);
    }

    @Test
    public void simple_4() {
        double[][] equation =
                new double[][]{
                        {4, 2, -1, 1},
                        {5, 3, -2, 2},
                        {3, 2, -3, 0}};

        double[] actual = LinearEquationsSystem.fromArray(equation).getSolution().getCoefficients();
        double[] expected = new double[]{-1, 3, 1};

        Assert.assertArrayEquals(expected, actual, 1e-9);
    }

    @Test
    public void withZeroRows() {
        double[][] equation =
                new double[][]{
                        {1, 1, 2, 9},
                        {2, 4, -3, 1},
                        {3, 6, -5, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}};

        double[] actual = LinearEquationsSystem.fromArray(equation).getSolution().getCoefficients();
        double[] expected = new double[]{1, 2, 3};

        Assert.assertArrayEquals(expected, actual, 1e-9);
    }

    @Test
    public void infiniteSolutions_1() {
        double[][] equation =
                new double[][]{
                        {1, 3, 4, 5},
                        {0, 1, 5, 5}};

        String actual = LinearEquationsSystem.fromArray(equation).getSolution().toString();
        String expected = Solution.State.INFINITE_SOLUTIONS.getMessage();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void infiniteSolutions_2() {
        double[][] equation =
                new double[][]{
                        {1, 7, 0, 1},
                        {0, 1, 0, 5}};

        String actual = LinearEquationsSystem.fromArray(equation).getSolution().toString();
        String expected = Solution.State.INFINITE_SOLUTIONS.getMessage();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void infiniteSolutions_3() {
        double[][] equation =
                new double[][]{
                        {1, 7, 0, 1},
                        {0, 1, 0, 5},
                        {0, 0, 0, 0}};

        String actual = LinearEquationsSystem.fromArray(equation).getSolution().toString();
        String expected = Solution.State.INFINITE_SOLUTIONS.getMessage();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void noSolutions_1() {
        double[][] equation =
                new double[][]{
                        {0, 1, 2, 9},
                        {0, 1, 3, 1},
                        {1, 0, 6, 1},
                        {2, 0, 2, 0}};

        String actual = LinearEquationsSystem.fromArray(equation).getSolution().toString();
        String expected = Solution.State.NO_SOLUTION.getMessage();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void swapColumns() {
        double[][] equation = {
                {1, 1, 0, 1, 0},
                {0, 0, 1, 2, 0},
                {0, 0, 1, 3, 0},
                {0, 0, 0, 0, 0}};

        Solution.State actual = LinearEquationsSystem.fromArray(equation).getSolution().getState();
        Solution.State expected = Solution.State.NO_SOLUTION;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void randomGeneratedTests() {
        for (int variablesNumber = 2; variablesNumber < 10; variablesNumber++) {
            for (int i = 0; i < 100; i++) {
                nextTest(variablesNumber);
            }
        }
    }

    private void nextTest(int variablesNumber) {
        double[] variablesValues = getRandomValuesArray(variablesNumber);
        double[][] coefficients = getRandomCoefficientsMatrix(variablesNumber);
        setFreeTerms(coefficients, variablesValues);

        Solution solution = LinearEquationsSystem.fromArray(coefficients).getSolution();

        if (solution.getState() != Solution.State.ONE_SOLUTION) {
            LinearEquationsSystem.fromArray(coefficients).getSolution().getCoefficients();
        } else {
            double[] actual = solution.getCoefficients();
            double[] expected = variablesValues;
            Assert.assertArrayEquals(expected, actual, 1e-9);
        }
    }

    private double[] getRandomValuesArray(int variablesNumber) {
        double[] numbers = new double[variablesNumber];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(10);
        }
        return numbers;
    }

    private double[][] getRandomCoefficientsMatrix(int variablesNumber) {
        double[][] array = new double[variablesNumber][variablesNumber + 1];
        for (int rowIx = 0; rowIx < array.length; rowIx++) {
            array[rowIx] = getRandomValuesArray(variablesNumber + 1);
        }
        sureAllColumnHasNotZero(array);
        return array;
    }

    private void setFreeTerms(double[][] coefficients, double[] variablesValues) {
        for (int rowIx = 0; rowIx < coefficients.length; rowIx++) {
            coefficients[rowIx][coefficients[0].length - 1] = product(coefficients[rowIx], variablesValues);
        }
    }

    private void sureAllColumnHasNotZero(double[][] array) {
        for (int columnIx = 0; columnIx < array[0].length; columnIx++) {
            int randomRowIx = random.nextInt(array.length);
            array[randomRowIx][columnIx] = 1 + random.nextInt(9);
        }
    }

    private double product(double[] coefficient, double[] variablesValues) {
        double product = 0;
        for (int i = 0; i < variablesValues.length; i++) {
            product += coefficient[i] * variablesValues[i];
        }
        return product;
    }
}
