package net.foxtam.linearequations;

public class LinearEquationsSystem {

    private final Matrix coefficients;
    private ShellDoubleArray solution;

    private LinearEquationsSystem(Matrix coefficients) {
        this.coefficients = coefficients;
    }

    public static LinearEquationsSystem fromMatrix(Matrix equationCoefficients) {
        return new LinearEquationsSystem(equationCoefficients);
    }

    public static LinearEquationsSystem fromArray(double[][] equationCoefficients) {
        return new LinearEquationsSystem(new Matrix(equationCoefficients));
    }

    public ShellDoubleArray getSolution() {
        if (solution == null) {
            solve();
        }
        return solution;
    }

    private void solve() {
        nullLowMatrix();
        nullHighMatrix();
        coefficients.transformToIdentityMatrix();
        setSolution();
    }

    private void nullLowMatrix() {
        for (int columnIx = 0; columnIx < coefficients.width(); columnIx++) {
            for (int rowIx = columnIx + 1; rowIx < coefficients.height(); rowIx++) {
                reduceRow(rowIx, columnIx);
            }
        }
    }

    private void nullHighMatrix() {
        for (int columnIx = coefficients.width() - 2; columnIx > 0; columnIx--) {
            for (int rowIx = columnIx - 1; rowIx >= 0; rowIx--) {
                reduceRow(rowIx, columnIx);
            }
        }
    }

    private void setSolution() {
        double[] result = new double[coefficients.height()];
        Matrix.Column resultColumn = coefficients.viewColumn(coefficients.width() - 1);
        for (int i = 0; i < resultColumn.size(); i++) {
            result[i] = resultColumn.get(i);
        }
        solution = new ShellDoubleArray(result);
    }

    private void reduceRow(int rowIx, int columnIx) {
        double multiplier = coefficients.get(rowIx, columnIx) / coefficients.get(columnIx, columnIx);
        coefficients.viewRow(rowIx).addRow(coefficients.viewRow(columnIx), -multiplier);
    }
}
