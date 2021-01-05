package net.foxtam;

import org.junit.Assert;
import org.junit.Test;

public class LinearEquationsTest {

    @Test
    public void solve() {
        double[][] equation = new double[][]{{1, 1, 2, 9}, {2, 4, -3, 1}, {3, 6, -5, 0}};
        double[] actual = LinearEquations.solve(equation);
        double[] expected = new double[]{1, 2, 3};
        Assert.assertArrayEquals(expected, actual, 0.001);
    }
}