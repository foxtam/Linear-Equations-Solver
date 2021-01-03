package net.foxtam;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearSolverTest {

    @Test
    public void solve() {
        Solution actual = LinearSolver.solve(4, 5, 7, 3, 9, 9);

        assertEquals(0.85714, actual.x, 0.001);
        assertEquals(0.71429, actual.y, 0.001);
    }
}