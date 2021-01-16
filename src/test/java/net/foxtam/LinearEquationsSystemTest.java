package net.foxtam;

import net.foxtam.linearequations.LinearEquationsSystem;
import org.junit.Assert;
import org.junit.Test;

public class LinearEquationsSystemTest {

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
        String expected = "Infinitely many solutions";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void infiniteSolutions_2() {
        double[][] equation =
                new double[][]{
                        {1, 7, 0, 1},
                        {0, 1, 0, 5}};

        String actual = LinearEquationsSystem.fromArray(equation).getSolution().toString();
        String expected = "Infinitely many solutions";

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
        String expected = "Infinitely many solutions";

        Assert.assertEquals(expected, actual);
    }
}
