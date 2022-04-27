package com.bits.cmha;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class CMHAndWithSite {
    //Number of processes in the system
    static int no_processes;
    static int no_sites;

    //Deadlock flag - 0 for false, 1 for true
    static boolean deadlock_flag = false;
    static class PID {
        public int i;
        public int j;
        public int k;

        @Override
        public String toString() {
            return "{" +
                    "i=" + (i+1) +
                    ", j=" + (j+1) +
                    ", k=" + (k+1) +
                    '}';
        }

        public void setValues(int ii , int jj , int kk){
            i= ii;
            j= jj;
            k= kk;
        }
    }
    static PID pid = new PID();
    static SortedMap<Integer, Integer>  processSiteMap = new TreeMap<>();

    // The main - The program execution starts from here
    public static void main(String[] args) {

        //process ID of the process initiating the probe
        int pid_probe;

        //Enter the number of processes in the system
        System.out.println("Enter the number of processes  (Should be greater than 1)");
        Scanner in = new Scanner(System.in);
        no_processes = in.nextInt();
        System.out.println("Enter the number of Sites (Should be greater than 1)");
        no_sites = in.nextInt();
        SortedMap<Integer, List<Integer>>  siteProcess = new TreeMap<>();
        for (int from = 0; from < no_sites; from++)
        {
            System.out.println("Enter the Processes of Site S" + (from+1) +". Enter 999 to go to next site");
            List<Integer> processList = new ArrayList<>();
            while (true){
                int pnumber = in.nextInt() ;
                if(pnumber== 999)
                    break;
                processList.add(pnumber-1);
            }
            siteProcess.put(from,processList);
        }

        for (Map.Entry<Integer, List<Integer>> entry : siteProcess.entrySet()){
            for (Integer p : entry.getValue()){
                processSiteMap.put(p, entry.getKey());
            }
        }

        if (no_processes > 1)
        {
            //Enter the wait-graph. (nxn) matrix.
            System.out.println("Enter the wait for graph");

            Integer[][] wait_for_graph = new Integer[no_processes][no_processes];


            //Input the wait for graph values
            for (int from = 0; from < no_processes; from++)
            {

                for (int to = 0; to < no_processes; to++)
                {
                    System.out.println("Enter from" + " P" + (from+1) + " ->" + " P"+ (to+1));
                    int temp = in.nextInt();
                    wait_for_graph[from][to] = temp ;
                }
            }

            boolean[] dependent = new boolean[no_processes];
            System.out.println( "The wait-for graph is : ");

            //display wait for graph
            displayGraph(wait_for_graph);

            //Enter the process initiating the probe
            System.out.println("Enter the process initiating the probe (Between 1 and number of processes)");
            pid_probe = in.nextInt();
            pid_probe = pid_probe - 1;

            //Initializing the probe to detect deadlock
            System.out.println("Initiating the Probe.....");
            System.out.println("DIRECTION" + "\t\t" + "PROBE");

            //Detecting deadlock
            for (int col = 0; col < no_processes; col++)
            {
                if (wait_for_graph[pid_probe][col] == 1)
                {
                    if (!processSiteMap.get(pid_probe).equals(processSiteMap.get(col))){
                        pid.setValues(pid_probe,pid_probe,col);
                        System.out.println(" P" +(col+1) + " is in another Site. Sending probe " + pid);
                    }
                    System.out.println( " P" + (pid_probe + 1) + " --> P" + (col + 1) + "     (" + (pid_probe + 1) + "," + (pid_probe + 1) + "," + (col + 1) + ")" );
                    if (!detectDeadlock(wait_for_graph, pid_probe, col, dependent)){
                        System.out.println("Deadlock is not detected");
                    }
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
            System.out.print("P" + (j + 1) +"\t");
        }
        System.out.println();
        //Side column and values
        for (int i = 0; i<m; i++)
        {
            System.out.print("P" + (i + 1) +"\t");
            for (int j = 0; j<n; j++)
            {
                System.out.print( wait_for_graph[i][j] + "\t");
            }
            System.out.println();
        }
    }

    static boolean detectDeadlock(Integer[][] graph, int init, int dest, boolean[] dependent)
    {
        int end = no_processes;
        for (int col = 0; col < end; col++)
        {
            if (graph[dest][col] == 1)
            {
                if (init == col || dependent[col])
                {
                    System.out.println(" P" + (dest + 1) + " --> P" + (col + 1) +"     (" + (init + 1) + "," + (dest + 1) + "," + (col + 1) + ")" + " --------> DEADLOCK DETECTED HERE");
                    deadlock_flag = true;
                    break;
                }
                if (!processSiteMap.get(dest).equals(processSiteMap.get(col))){
                    pid.setValues(init,dest,col);
                    System.out.println(" P"+ (col+1) + " is in another Site.Sending probe " + pid);
                }
                System.out.println( " P" + (dest + 1) + " --> P" + (col + 1) + "     (" + (init + 1) + "," + (dest + 1) + "," + (col + 1) + ")");
                dependent[col] = true;
                detectDeadlock(graph, init, col, dependent);
            }
        }
        return deadlock_flag;
    }
}
