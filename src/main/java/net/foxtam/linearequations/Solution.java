package net.foxtam.linearequations;

public class Solution {
    private final State state;
    private double[] coefficients;

    private Solution(State state) {
        this.state = state;
    }

    public static Solution noSolution() {
        return new Solution(State.NO_SOLUTION);
    }

    public static Solution infiniteSolutions() {
        return new Solution(State.INFINITE_SOLUTIONS);
    }

    public static Solution hasSolutions(double[] coefficients) {
        Solution solution = new Solution(State.ONE_SOLUTION);
        solution.coefficients = coefficients;
        return solution;
    }

    public State getState() {
        return state;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    @Override
    public String toString() {
        switch (state) {
            case NO_SOLUTION:
                return "No solutions";
            case INFINITE_SOLUTIONS:
                return "Infinitely many solutions";
            case ONE_SOLUTION:
                StringBuilder builder = new StringBuilder();
                for (double c : coefficients) {
                    builder.append(c).append('\n');
                }
                return builder.toString();
        }
        throw new IllegalStateException();
    }

    public enum State {
        NO_SOLUTION, INFINITE_SOLUTIONS, ONE_SOLUTION
    }
}
