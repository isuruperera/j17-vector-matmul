package org.example;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

public class VectorToScalarMultiplication {
    private static VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    private static final boolean USE_VECTOR_API = true;
    private static final boolean USE_LOOPS = true;
    private static final boolean LOG_DEBUG_INFO = false;

    private static int vec = 16 * 100;
    private static int scalar1 = 10;
    private static int scalar2 = 2;

    public static void main(String[] args) {
        int A[] = new int[vec];
        for (int i = 0; i < vec; i++) {
            A[i] = i + 1;
        }
        multiplyVectorByScalar(vec, A);
    }

    // Function to print Vector
    static void printVector(int V[]) {
        for (int i = 0; i < V.length; i++) {
            System.out.print(V[i] + " ");
        }
        System.out.println();
    }

    // Function to multiply
    // two vectors A[] and B[]
    static void multiplyVectorByScalar(int vec1, int A[]) {
        int i, l;

        // Matrix to store the result
        // The product matrix will
        // be of size row1 x col2
        int C[] = new int[vec1];

        if (USE_VECTOR_API) {
            long startTime = System.currentTimeMillis();
            var upperBound = SPECIES.loopBound(A.length);
            l = 0;
            for (; l < upperBound; l += SPECIES.length()) {
                var va = IntVector.fromArray(SPECIES, A, l);
                var vc = va.mul(scalar1).add(scalar2);
                vc.intoArray(C, l);
            }
            long endTimeTime = System.currentTimeMillis();
            System.out.printf("Time taken for Vector Mul with VectorAPI: %s ms\n", endTimeTime - startTime);
        }
        if(LOG_DEBUG_INFO) {
            printVector(C);
        }

        C = new int[vec1];

        if (USE_LOOPS) {
            long startTime = System.currentTimeMillis();
            for (i = 0; i < vec1; i++) {
                C[i] = A[i] * scalar1 + scalar2;
            }
            long endTimeTime = System.currentTimeMillis();
            System.out.printf("Time taken for Vector Mul with Loops: %s ms\n", endTimeTime - startTime);
        }
        if(LOG_DEBUG_INFO) {
            printVector(C);
        }
    }
}