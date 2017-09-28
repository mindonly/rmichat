/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
        // 0 and 1 are not prime
        boolean[]isPrime = new boolean[max+1];
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
        int primeCount = 0;

        for (int i = 0; i <= max; i++) {
            if (isPrime[i]) {
                primes[primeCount] = i;
                primeCount++;
            }
        }

        ArrayList<Integer> pal = new ArrayList<>();
        for (int item : primes) {
            if (item != 0 && item >= min)
                pal.add(item);
        }

        System.out.println(pal.toString());

        return pal;
    }
}
