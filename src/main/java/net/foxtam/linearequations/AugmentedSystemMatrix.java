package net.foxtam.linearequations;

import net.foxtam.utils.DoubleUtil;

import java.util.Arrays;

public class AugmentedSystemMatrix {
    private final double[][] matrix;

    public AugmentedSystemMatrix(double[][] matrix) {
        this.matrix = copyArray2D(matrix);
    }

    private double[][] copyArray2D(double[][] origin) {
        double[][] copy = Arrays.copyOf(origin, origin.length);
        for (int i = 0; i < origin.length; i++) {
            if (origin[i].length != origin[0].length) {
                throw new IllegalArgumentException("Не приямоуголный массив");
            }
            copy[i] = Arrays.copyOf(origin[i], origin[i].length);
        }
        return copy;
    }

    public void transformToIdentityMatrix() {
        for (int rowIx = 0; rowIx < shortWidth(); rowIx++) {
            viewRow(rowIx).multiplyBy(1.0 / get(rowIx, rowIx));
        }
    }

    public int height() {
        return matrix.length;
    }

    public FullRow viewRow(int rowIndex) {
        return new FullRow(rowIndex);
    }

    public double get(int rowIndex, int columnIndex) {
        return matrix[rowIndex][columnIndex];
    }

    public int shortWidth() {
        return fullWidth() - 1;
    }

    public int fullWidth() {
        return matrix[0].length;
    }

    private void set(int rowIndex, int columnIndex, double value) {
        matrix[rowIndex][columnIndex] = value;
    }

    public Column freeTermColumn() {
        return viewColumn(fullWidth() - 1);
    }

    public Column viewColumn(int columnIndex) {
        return new Column(columnIndex);
    }

    public void swapRows(int rowA, int rowB) {
        double[] doubles = Arrays.copyOf(matrix[rowA], fullWidth());
        System.arraycopy(matrix[rowB], 0, matrix[rowA], 0, fullWidth());
        System.arraycopy(doubles, 0, matrix[rowB], 0, fullWidth());
    }

    public void swapColumns(int columnA, int columnB) {
        double[] doubles = new double[height()];
        for (int i = 0; i < height(); i++) {
            doubles[i] = matrix[i][columnA];
        }
        for (int i = 0; i < height(); i++) {
            matrix[i][columnA] = matrix[i][columnB];
        }
        for (int i = 0; i < height(); i++) {
            matrix[i][columnB] = doubles[i];
        }
    }

    public int height() {
        return matrix.length;
    }

    private abstract class Row {

        private final int rowIndex;

        public Row(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        protected int getRowIndex() {
            return rowIndex;
        }

        public void addRow(Row row) {
            addRow(row, 1.0);
        }

        public void addRow(Row row, double multiplier) {
            if (size() != row.size()) {
                throw new IllegalArgumentException("Строки не одинаковой длины");
            }
            for (int i = 0; i < size(); i++) {
                set(i, get(i) + multiplier * row.get(i));
            }
        }

        public abstract int size();

        public void set(int columnIndex, double value) {
            if (columnIndex >= size()) {
                throw new IndexOutOfBoundsException();
            }
            matrix[rowIndex][columnIndex] = value;
        }

        public double get(int columnIndex) {
            if (columnIndex >= size()) {
                throw new IndexOutOfBoundsException();
            }
            return matrix[rowIndex][columnIndex];
        }

        public void multiplyBy(double multiplier) {
            for (int i = 0; i < size(); i++) {
                set(i, get(i) * multiplier);
            }
        }

        public boolean hasNotZero() {
            for (int i = 0; i < size(); i++) {
                if (!DoubleUtil.isCloseToZero(get(i))) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasZero() {
            for (int i = 0; i < size(); i++) {
                if (DoubleUtil.isCloseToZero(get(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    public class FullRow extends Row {

        public FullRow(int rowIndex) {
            super(rowIndex);
        }

        public boolean isBroken() {
            return DoubleUtil.isCloseToZero(freeTerm()) && onlyCoefficients().hasNotZero()
                    || !DoubleUtil.isCloseToZero(freeTerm()) && onlyCoefficients().onlyZeros();
        }

        private double freeTerm() {
            return get(size() - 1);
        }

        private CoefficientRow onlyCoefficients() {
            return new CoefficientRow(getRowIndex());
        }

        public int size() {
            return matrix[0].length;
        }

    }

    public class CoefficientRow extends Row {

        public CoefficientRow(int rowIndex) {
            super(rowIndex);
        }

        public boolean onlyZeros() {
            for (int i = 0; i < size(); i++) {
                if (!DoubleUtil.isCloseToZero(get(i))) return false;
            }
            return true;
        }

        @Override
        public int size() {
            return matrix[0].length - 1;
        }
    }

    public class Column {

        private final int columnIndex;

        public Column(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public int size() {
            return matrix.length;
        }

        public void set(int rowIndex, double value) {
            matrix[rowIndex][columnIndex] = value;
        }

        public double get(int rowIndex) {
            return matrix[rowIndex][columnIndex];
        }

    }
}
