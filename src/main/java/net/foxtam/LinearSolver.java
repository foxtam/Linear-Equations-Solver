package net.foxtam;

public class LinearSolver {

    public static Solution solve(double a, double b, double c, double d, double e, double f) {
        double y = (f - c * d / a) / (e - b * d / a);
        double x = (c - b * y) / a;
        return new Solution(x, y);
    }
}
