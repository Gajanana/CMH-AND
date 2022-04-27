/*
 * This code is sample code, provided as-is, and we make no
 * warranties as to its correctness or suitability for
 * any purpose.
 *
 * We hope that it's useful to you.  Enjoy.
 * Copyright LearningPatterns Inc.
 */
package com.bits.cmha;
import java.util.Scanner;

public class CMHAnd {
    //Number of proccesses in the system
    static int no_proccesses;

    //Deadlock flag - 0 for false, 1 for true
    static boolean deadlock_flag = false;

// The main - The program execution starts from here
    public static void main(String[] args) {

        //Proccess ID of the proccess initiating the probe
        int pid_probe;

        //Enter the number of proccesses in the system
        System.out.println("Enter the number of processes Should be greater than 1)");
        Scanner in = new Scanner(System.in);
        no_proccesses = in.nextInt();

        if (no_proccesses > 1)
        {
            //Enter the wait-graph. (nxn) matrix.
            System.out.println("Enter the wait for graph");

            Integer[][] wait_for_graph = new Integer[no_proccesses][no_proccesses];


            //Input the wait for graph values
            for (int from = 0; from < no_proccesses; from++)
            {

            for (int to = 0; to < no_proccesses; to++)
              {
                  System.out.println("Enter from" + " S" + (from+1) + " ->" + " S"+ (to+1));
                  int temp = in.nextInt();
                  wait_for_graph[from][to] = temp ;
              }
            }


            System.out.println( "The wait-for graph is : ");

            //display wait for graph
            displayGraph(wait_for_graph);

            //Enter the proccess initiating the probe
            System.out.println("Enter the proccess initiating the probe (Between 1 and number of processors)");
            pid_probe = in.nextInt();
            pid_probe = pid_probe - 1;

            //Initializing the probe to detect deadlock
              System.out.println("Initiating the Probe.....");
              System.out.println("DIRECTION" + "\t" + "PROBE");

            //Detecting deadlock
            for (int col = 0; col < no_proccesses; col++)
            {
                if (wait_for_graph[pid_probe][col] == 1)
                {
                     System.out.println( " S" + (pid_probe + 1) + " --> S" + (col + 1) + "     (" + (pid_probe + 1) + "," + (pid_probe + 1) + "," + (col + 1) + ")" );
                     detectDeadlock(wait_for_graph, pid_probe, col);
                }
            }
        }
        else {
            System.out.println("Error: No Process is running in the system .Please enter correct inputs");
        }

        }

        static void displayGraph(Integer[][] wait_for_graph)
        {
            int n = wait_for_graph[0].length;
            int m = wait_for_graph.length;

            //Top Column
            System.out.print("\t");
            for (int j = 0; j < m; j++) {
              System.out.print("S" + (j + 1) +"\t");
            }
            System.out.println();
            //Side column and values
            for (int i = 0; i<m; i++)
            {
              System.out.print("S" + (i + 1) +"\t");
              for (int j = 0; j<n; j++)
              {
                System.out.print( wait_for_graph[i][j] + "\t");
              }
                System.out.println();
              }
        }

        static void detectDeadlock(Integer[][] graph, int init, int dest)
        {
            int end = no_proccesses;
            for (int col = 0; col < end; col++)
            {
            if (graph[dest][col] == 1)
            {
            if (init == col)
            {
                System.out.println(" S" + (dest + 1) + " --> S" + (col + 1) +"     (" + (init + 1) + "," + (dest + 1) + "," + (col + 1) + ")" + " --------> DEADLOCK DETECTED HERE");
                deadlock_flag = true;
                break;
            }
              System.out.println( " S" + (dest + 1) + " --> S" + (col + 1) + "     (" + (init + 1) + "," + (dest + 1) + "," + (col + 1) + ")");
              detectDeadlock(graph, init, col);
            }
            }
        }
}
