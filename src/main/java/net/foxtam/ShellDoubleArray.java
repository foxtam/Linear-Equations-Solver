package net.foxtam;


public class ShellDoubleArray {
    private final double[] array;

    public ShellDoubleArray(double[] array) {
        this.array = array;
    }

    public double get(int i) {
        return array[i];
    }

    public int size() {
        return array.length;
    }
}
