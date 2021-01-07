package net.foxtam;

import net.foxtam.linearequations.LinearEquationsSystem;
import net.foxtam.linearequations.ShellDoubleArray;
import org.junit.Assert;
import org.junit.Test;

public class LinearEquationsSystemTest {

    @Test
    public void solve() {
        double[][] equation = new double[][]{{1, 1, 2, 9}, {2, 4, -3, 1}, {3, 6, -5, 0}};
        ShellDoubleArray actual = LinearEquationsSystem.fromArray(equation).getSolution();
        double[] expected = new double[]{1, 2, 3};

        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected[i], actual.get(i), 0.001);
        }
    }
}
