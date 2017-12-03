/*
 * Lab 02
 * Rob Sanchez
 * CIS 656, F2017
 */
package client;

import compute.Task;
import java.io.Serializable;
import java.util.ArrayList;

public class Primes implements Task<ArrayList>, Serializable {

    private static final long serialVersionUID = 144L;

    /** min and max boundaries for finding primes (inclusive) */
    private final int min, max;

    /**
     * Construct a task to find primes between the
     * specified boundaries (inclusive).
     */
    public Primes(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Find primes between min and max (inclusive).
     */
    public ArrayList execute() {
        return findPrimes(min, max);
    }

    /**
     * Find primes between the min and max boundaries (inclusive).
     * Adapted from http://introcs.cs.princeton.edu/java/14array/PrimeSieve.java.html
     */
    public static ArrayList findPrimes(int min, int max) {

        // initially assume all integers are prime
        // 0 and 1 are not
        boolean[] isPrime = new boolean[max+1];
        for (int i = 2; i <= max; i++) {
            isPrime[i] = true;
        }

        // mark non-primes <= max using Sieve of Eratosthenes
        for (int factor = 2; factor*factor <= max; factor++) {

            // if factor is prime, mark multiples of factor as non-prime
            if (isPrime[factor]) {
                for (int j = factor; factor*j <= max; j++)
                    isPrime[factor*j] = false;
            }
        }

        int[] primes = new int[max];
        int primeIdx = 0;

        for (int i = 0; i <= max; i++) {
            if (isPrime[i]) {
                primes[primeIdx] = i;
                primeIdx++;
            }
        }

        ArrayList<Integer> pal = new ArrayList<>();
        for (int item : primes) {
            if (item != 0 && item >= min)
                pal.add(item);
        }

//        System.out.println(primeIdx + " primes found.\n");

        return pal;
    }
}
