package net.foxtam.linearequations;

import net.foxtam.utils.DoubleUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class LinearEquationsSystem {

    private final AugmentedSystemMatrix matrix;
    private final Deque<Runnable> columnSwapStack = new ArrayDeque<>();
    private Solution solution;

    private LinearEquationsSystem(AugmentedSystemMatrix matrix) {
        this.matrix = matrix;
    }

    public static LinearEquationsSystem fromMatrix(AugmentedSystemMatrix equationCoefficients) {
        return new LinearEquationsSystem(equationCoefficients);
    }

    public static LinearEquationsSystem fromArray(double[][] equationCoefficients) {
        return new LinearEquationsSystem(new AugmentedSystemMatrix(equationCoefficients));
    }

    public Solution getSolution() {
        if (solution == null) {
            solve();
        }
        return solution;
    }

    private void solve() {
        if (matrix.height() < matrix.shortWidth()) {
            solution = Solution.infiniteSolutions();
            return;
        }
        nullBottomLeftMatrixTriangle();
        if (hasNoSolutions()) {
            solution = Solution.noSolution();
        } else if (hasInfiniteSolutions()) {
            solution = Solution.infiniteSolutions();
        } else {
            nullTopRightMatrixTriangle();
            matrix.transformToIdentityMatrix();
            placeColumnsBack();
            solution = Solution.hasSolutions(pullOutResultCoefficients());
        }
    }

    private void nullBottomLeftMatrixTriangle() {
        for (int columnIx = 0; columnIx < matrix.shortWidth(); columnIx++) {
            if (DoubleUtil.isCloseToZero(matrix.get(columnIx, columnIx))) {
                if (hasNotZeroInRectangleAt(columnIx)) {
                    rearrangeMatrixAt(columnIx);
                } else return;
            }
            for (int rowIx = columnIx + 1; rowIx < matrix.height(); rowIx++) {
                reduceRow(rowIx, columnIx);
            }
        }
    }

    private boolean hasNoSolutions() {
        for (int i = 0; i < matrix.height(); i++) {
            if (matrix.viewRow(i).isBroken()) return true;
        }
        return false;
    }

    private boolean hasInfiniteSolutions() {
        int significantRowsNumber = countSignificantRowsNumber();
        int significantVariablesNumber = matrix.shortWidth();
        if (significantRowsNumber > significantVariablesNumber) {
            throw new IllegalStateException("Количество зниачимых уравнений не может быть больше количесва значимых переменных.");
        } else return significantRowsNumber < significantVariablesNumber;
    }

    private void nullTopRightMatrixTriangle() {
        for (int columnIx = matrix.shortWidth() - 1; columnIx > 0; columnIx--) {
            for (int rowIx = columnIx - 1; rowIx >= 0; rowIx--) {
                reduceRow(rowIx, columnIx);
            }
        }
    }

    private void placeColumnsBack() {
        while (!columnSwapStack.isEmpty()) {
            columnSwapStack.removeLast().run();
        }
    }

    private double[] pullOutResultCoefficients() {
        double[] doubles = new double[matrix.shortWidth()];
        AugmentedSystemMatrix.Column freeTermColumn = matrix.freeTermColumn();
        for (int columnIx = 0; columnIx < matrix.shortWidth(); columnIx++) {
            doubles[columnIx] = freeTermColumn.get(findNotZeroRow(columnIx));
        }
        return doubles;
    }

    private boolean hasNotZeroInRectangleAt(int originalIx) {
        for (int rowIx = originalIx; rowIx < matrix.height(); rowIx++) {
            for (int columnIx = originalIx; columnIx < matrix.shortWidth(); columnIx++) {
                if (!DoubleUtil.areClose(matrix.get(rowIx, columnIx), 0)) return true;
            }
        }
        return false;
    }

    private void rearrangeMatrixAt(int rowColumnIx) {
        if (trySwapRows(rowColumnIx)) return;
        if (trySwapColumns(rowColumnIx)) return;
        trySwapRowAndColumn(rowColumnIx);
    }

    private void reduceRow(int rowIx, int columnIx) {
        double multiplier = matrix.get(rowIx, columnIx) / matrix.get(columnIx, columnIx);
        matrix.viewRow(rowIx).addRow(matrix.viewRow(columnIx), -multiplier);
    }

    private int countSignificantRowsNumber() {
        int counter = 0;
        for (int i = 0; i < matrix.height(); i++) {
            if (matrix.viewRow(i).hasNotZero()) {
                counter++;
            }
        }
        return counter;
    }

    private int findNotZeroRow(int columnIx) {
        for (int rowIx = 0; rowIx < matrix.height(); rowIx++) {
            if (!DoubleUtil.isCloseToZero(matrix.get(rowIx, columnIx))) {
                return rowIx;
            }
        }
        throw new IllegalStateException("Матрица находится в некорректном состоянии");
    }

    private boolean trySwapRows(int rowColumnIx) {
        if (DoubleUtil.isCloseToZero(matrix.get(rowColumnIx, rowColumnIx))) {
            Optional<Integer> optRowIx = findAdequateRowUnder(rowColumnIx);
            if (optRowIx.isPresent()) {
                matrix.swapRows(rowColumnIx, optRowIx.get());
                return true;
            }
        }
        return false;
    }

    private boolean trySwapColumns(int rowColumnIx) {
        if (DoubleUtil.isCloseToZero(matrix.get(rowColumnIx, rowColumnIx))) {
            Optional<Integer> optColIx = findAdequateColumnAfter(rowColumnIx);
            if (optColIx.isPresent()) {
                matrix.swapColumns(rowColumnIx, optColIx.get());
                columnSwapStack.addLast(() -> matrix.swapColumns(rowColumnIx, optColIx.get()));
                return true;
            }
        }
        return false;
    }

    private void trySwapRowAndColumn(int rowColumnIx) {
        if (DoubleUtil.isCloseToZero(matrix.get(rowColumnIx, rowColumnIx))) {
            Optional<Point> optionalPoint = findNotZeroPointAfter(rowColumnIx);
            if (optionalPoint.isPresent()) {
                Point point = optionalPoint.get();
                matrix.swapRows(rowColumnIx, point.getRow());
                matrix.swapColumns(rowColumnIx, point.getColumn());
                columnSwapStack.addLast(() -> matrix.swapColumns(rowColumnIx, point.getColumn()));
            }
        }
    }

    private Optional<Integer> findAdequateRowUnder(int rowIx) {
        for (int i = rowIx; i < matrix.height(); i++) {
            if (!DoubleUtil.isCloseToZero(matrix.get(i, rowIx))) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findAdequateColumnAfter(int columnIx) {
        for (int j = columnIx; j < matrix.shortWidth(); j++) {
            if (!DoubleUtil.isCloseToZero(matrix.get(columnIx, j))) {
                return Optional.of(j);
            }
        }
        return Optional.empty();
    }

    private Optional<Point> findNotZeroPointAfter(int rowColumnIx) {
        for (int i = rowColumnIx; i < matrix.shortWidth(); i++) {
            for (int j = rowColumnIx; j < matrix.height(); j++) {
                if (!DoubleUtil.isCloseToZero(matrix.get(i, j))) {
                    return Optional.of(new Point(i, j));
                }
            }
        }
        return Optional.empty();
    }
}
