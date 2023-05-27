package org.example;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

public class VectorToVectorMultiplication {
    private static VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    private static final boolean USE_VECTOR_API = true;
    private static final boolean USE_LOOPS = true;
    private static final boolean LOG_DEBUG_INFO = true;

    private static int vec1 = 16 * 1, vec2 = 16 * 1;

    public static void main(String[] args) {

        int A[] = new int[vec1];
        for (int i = 0; i < vec1; i++) {
            A[i] = i + 1;
        }

        int B[] = new int[vec2];
        for (int i = 0; i < vec2; i++) {
            B[i] = i + 1;
        }

        multiplyVector(vec1, A, vec2, B);
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
    static void multiplyVector(
            int vec1, int A[],
            int vec2, int B[]) {
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
                var vb = IntVector.fromArray(SPECIES, B, l);
                var vc = va.mul(vb);
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
                C[i] = A[i] * B[i];
            }
            long endTimeTime = System.currentTimeMillis();
            System.out.printf("Time taken for Vector Mul with Loops: %s ms\n", endTimeTime - startTime);
        }
        if(LOG_DEBUG_INFO) {
            printVector(C);
        }
    }
}