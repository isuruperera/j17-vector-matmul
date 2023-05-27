package org.example;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

public class Main {
    private static VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    private static final boolean USE_VECTOR_API = true;

    public static void main(String[] args) {
        int row1 = 8, col1 = 8, row2 = 8, col2 = 8;

        int A[][] = new int[row1][col1];
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                A[i][j] = i + 1;
            }
        }

        int B[][] = new int[row1][col1];
        for (int i = 0; i < row2; i++) {
            for (int j = 0; j < col2; j++) {
                B[i][j] = i + 1;
            }
        }

        multiplyMatrixSIMD(row1, col1, A, row2, col2, B);
    }

    // Function to print Matrix
    static void printMatrix(int M[][],
                            int rowSize,
                            int colSize) {
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
            int row2, int col2, int B[][]) {
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

        if (USE_VECTOR_API) {
            for (i = 0; i < row1; i++) {
                for (j = 0; j < col2; j++) {
                    int[] col2Arr = new int[row2];
                    int[] row1Arr = A[i];
                    int[] mulArr = new int[row1];
                    for (k = 0; k < row2; k++) {
                        col2Arr[k] = B[k][j];
                    }

                    var upperBound = SPECIES.loopBound(col2Arr.length);

                    l = 0;
                    for (; l < upperBound; l += SPECIES.length()) {
                        var va = IntVector.fromArray(SPECIES, col2Arr, l);
                        var vb = IntVector.fromArray(SPECIES, row1Arr, l);
                        var vc = va.mul(vb);
                        vc.intoArray(mulArr, l);
                    }

                    C[i][j] = Arrays.stream(mulArr).sum();
                }
            }
        } else {
            for (i = 0; i < row1; i++) {
                for (j = 0; j < col2; j++) {
                    for (k = 0; k < row2; k++)
                        C[i][j] += A[i][k] * B[k][j];
                }
            }
        }




        // Print the result
        System.out.println("\nResultant Matrix:");
        printMatrix(C, row1, col2);
    }
}