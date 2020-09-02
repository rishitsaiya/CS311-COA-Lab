import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// package assignment0;

public class Main {

    // Computes average of list of integers
    public static double average(int[] timeTaken) {
        // check to avoid dividing by zero
        assert timeTaken.length > 0 : "Can not take average of empty list";
        int sum = 0;

        for (double currentTime : timeTaken)
            sum += currentTime;

        return sum / timeTaken.length;
    }

    /**
     * Simulate the experiment multiple times for a given set of border parameters
     * and probability of biasness of coin, and take the average of results obtained
     * 
     * @param borderLength:       length of the border (the x-coordinate)
     * @param borderWidth:        Width of the border (the y-coordinate)
     * @param probabilityOfHeads: Probability of biasedness of the coin to be used
     *                            to decide if the cell is on/ off.
     * @return: the average time taken by the Infiltrator to reach the defending
     *          country
     */
    public static double simulate(int borderLength, int borderWidth, double probabilityOfHeads) {
        // repeat the experiment with same parameters 'numOfIteration' times and take
        // average
        int numOfIterations = 5;
        int[] timeTaken = new int[numOfIterations];

        for (int i = 0; i < numOfIterations; i += 1) {
            Infiltrator warrior = new Infiltrator(borderLength, borderWidth, probabilityOfHeads);
            Clock timer = new Clock(10);

            while (!warrior.hasReachedDefendingCountry()) {
                warrior.moveNext();
                timer.nextTimeStamp();
            }

            timeTaken[i] = timer.nextTimeStamp();
        }

        // return the average of time taken
        return average(timeTaken);
    }

    /**
     * The Main running point of our program
     * 
     * @param args
     */
    public static void main(String[] args) {
        String inputFileName = args[0];
        String outputFileName = args[1];

        System.out.println("\nCommand Line Args: " + inputFileName + " " + outputFileName);
        Scanner input;
        try {
            input = new Scanner(new File(inputFileName));
            
            // Write output to filename given by user
            PrintWriter writer = new PrintWriter(outputFileName);
            
            // read data about probabilities and border width from the file
            ArrayList<Double> probabilities = Stream.of(input.nextLine().split(" ")).map(i -> Double.valueOf(i)).collect(Collectors.toCollection(ArrayList:: new));
            ArrayList<Integer> widths = Stream.of(input.nextLine().split(" ")).map(i -> Integer.valueOf(i)).collect(Collectors.toCollection(ArrayList:: new));
            double[] timeTaken = new double[widths.size()*probabilities.size()];
            int borderLength = 1000;
            int k = 0;
            // Simulate the experiment for each inputs, and record the corresponding
            // time taken by the Infiltrator
            for (int i = 0; i < probabilities.size(); i += 1) {
            	for (int j = 0; j < widths.size(); j += 1) {
	                timeTaken[k] = simulate(borderLength, widths.get(j), probabilities.get(i));
	                
	                // Save prob, width and time in output file to plot graph
	                String data = String.valueOf(probabilities.get(i)) + ", " + String.valueOf(widths.get(j)) + ", " + String.valueOf(timeTaken[k]);
	                writer.println(data);
	                k += 1;
            	}
            }
            writer.close();
            System.out.println(Arrays.toString(timeTaken));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }    

}