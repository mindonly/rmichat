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

import compute.Compute;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputeLoop {

    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        int userSelection;
        Scanner choiceInput = new Scanner(System.in);
        userSelection = menu();

        while (userSelection != 3) switch (userSelection) {
            case 1:
                System.out.println("----------");
                System.out.println("Compute Pi");
                System.out.println("----------");

                int digits = 32;
                System.out.println("\nHow many digits of Pi?");
                try { digits = choiceInput.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nSorry, digits should be an Int.\n");
                    System.exit(-1);
                }

                System.out.println();
                ComputeLoop.runPi(args[0], digits);
                userSelection = menu();
                break;
            case 2:
                System.out.println("--------------");
                System.out.println("Compute Primes");
                System.out.println("--------------");

                int min = 2; int max = 100;
                System.out.println("\nPlease enter two Integers, a minimum and a maximum:");

                System.out.println("minimum:");
                try { min = choiceInput.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nSorry, minimum must be an Int.\n");
                    System.exit(-1);
                }

                System.out.println("maximum:");
                try { max = choiceInput.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nSorry, maximum must be an Int.\n");
                    System.exit(-1);
                }

                System.out.println();
                ComputeLoop.runPrimes(args[0], min, max);
                userSelection = menu();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("\nSorry, not a valid choice! Try again!");
                userSelection = menu();
                break;
        }
    }

     private static void runPi(String host, int digits) {
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(host);
            Compute comp = (Compute) registry.lookup(name);

            Pi task = new Pi(digits);

            BigDecimal pi = comp.executeTask(task);
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

    private static void runPrimes(String host, int min, int max) {
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(host);
            Compute comp = (Compute) registry.lookup(name);

            Primes task = new Primes(min, max);

            ArrayList primes = comp.executeTask(task);
            System.out.println(primes);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

    private static int menu() {
        int choice = 0;
        Scanner menuInput = new Scanner(System.in);

        System.out.println("\n------------------------");
        System.out.println("ComputeLoop v1.0 (F2017)");
        System.out.println("Please make a selection:");
        System.out.println("------------------------");
        System.out.println("1 - Compute Pi");
        System.out.println("2 - Compute Primes");
        System.out.println("3 - Exit\n");

        try { choice = menuInput.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Sorry, your choice must be an Int.\n");
            System.exit(-1);
        }

        return choice;
    }
}
