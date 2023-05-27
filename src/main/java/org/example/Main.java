package org.example;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

public class Main {
    private static VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    public static void main(String[] args) {
        int row1 = 4, col1 = 3, row2 = 3, col2 = 4;

        int A[][] = { { 1, 1, 1 },
                { 2, 2, 2 },
                { 3, 3, 3 },
                { 4, 4, 4 } };

        int B[][] = { { 1, 1, 1, 1 },
                { 2, 2, 2, 2 },
                { 3, 3, 3, 3 } };

        multiplyMatrixSIMD(row1, col1, A, row2, col2, B);
    }

    // Function to print Matrix
    static void printMatrix(int M[][],
                            int rowSize,
                            int colSize)
    {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++)
                System.out.print(M[i][j] + " ");
            System.out.println();
        }
    }

    // Function to multiply
    // two matrices A[][] and B[][]
    static void multiplyMatrixSIMD(
            int row1, int col1, int A[][],
            int row2, int col2, int B[][])
    {
        int i, j, k, l;

        // Print the matrices A and B
        System.out.println("\nMatrix A:");
        printMatrix(A, row1, col1);
        System.out.println("\nMatrix B:");
        printMatrix(B, row2, col2);

        // Check if multiplication is Possible
        if (row2 != col1) {
            System.out.println("\nMultiplication Not Possible");
            return;
        }

        // Matrix to store the result
        // The product matrix will
        // be of size row1 x col2
        int C[][] = new int[row1][col2];

        for (i = 0; i < row1; i++) {
            for (j = 0; j < col2; j++) {
                int[] col1Arr = new int[col2];
                int[] row2Arr = B[i];
                int[] mulArr = new int[row1];
                for (k = 0; k < col2; k++) {
                    col1Arr[k] = A[k][i];
                }

                var upperBound = SPECIES.loopBound(col1Arr.length);

                l = 0;
                for (; l < upperBound; l += SPECIES.length()) {
                    var va = IntVector.fromArray(SPECIES, col1Arr, l);
                    var vb = IntVector.fromArray(SPECIES, row2Arr, l);
                    var vc = va.mul(vb);
                    vc.intoArray(mulArr, l);
                }

                C[i][j] = Arrays.stream(mulArr).sum();
            }
        }

        // Print the result
        System.out.println("\nResultant Matrix:");
        printMatrix(C, row1, col2);
    }
}