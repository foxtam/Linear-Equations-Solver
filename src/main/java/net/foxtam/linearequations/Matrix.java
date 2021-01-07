package net.foxtam.linearequations;

import java.util.Arrays;

public class Matrix {
    private final double[][] matrix;

    public Matrix(double[][] matrix) {
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

    public Column viewColumn(int columnIndex) {
        return new Column(columnIndex);
    }

    public void transformToIdentityMatrix() {
        for (int rowIx = 0; rowIx < height(); rowIx++) {
            viewRow(rowIx).multiplyBy(1 / get(rowIx, rowIx));
        }
    }

    public int height() {
        return matrix.length;
    }

    public Row viewRow(int rowIndex) {
        return new Row(rowIndex);
    }

    public double get(int rowIndex, int columnIndex) {
        return matrix[rowIndex][columnIndex];
    }

    public int width() {
        return matrix[0].length;
    }

    private void set(int rowIndex, int columnIndex, double value) {
        matrix[rowIndex][columnIndex] = value;
    }

    public class Row {
        private final int rowIndex;

        public Row(int rowIndex) {
            this.rowIndex = rowIndex;
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

        public int size() {
            return matrix[rowIndex].length;
        }

        public void set(int columnIndex, double value) {
            matrix[rowIndex][columnIndex] = value;
        }

        public double get(int columnIndex) {
            return matrix[rowIndex][columnIndex];
        }

        public void multiplyBy(double multiplier) {
            for (int i = 0; i < size(); i++) {
                set(i, get(i) * multiplier);
            }
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
