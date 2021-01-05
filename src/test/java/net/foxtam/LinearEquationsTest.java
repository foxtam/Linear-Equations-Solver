package net.foxtam;

import org.junit.Assert;
import org.junit.Test;

public class LinearEquationsTest {

    @Test
    public void solve() {
        double[][] equation = new double[][]{{1, 1, 2, 9}, {2, 4, -3, 1}, {3, 6, -5, 0}};
        ShellDoubleArray actual = LinearEquations.fromMatrix(equation).getSolution();
        double[] expected = new double[]{1, 2, 3};

        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected[i], actual.get(i), 0.001);
        }
    }
}
