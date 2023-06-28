import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class csvdata {

    public static Double[] PathFinder(List<String[]> r, String[] startArray, String[] destinationArray, Double total_distance, List<String> explored_paths, Double previous_distance){
        // iterating through the various paths
        double min = 2.00;
        int minIndex = 0;
        Double[] minArray = new Double[2];

        for(int i = 1; i< startArray.length; i++){
            double i_ = Double.parseDouble(startArray[i]);
            if (i_ < min && i_ != 0) {

                // if min, i < distance_of_previous_landmark from destination
                if (Double.parseDouble(r.get(i)[5]) < previous_distance){
                    min = Double.parseDouble(startArray[i]) + 0.1; //We can add some values to this to get different routes.// I added 0.1 so it will ignore routes which are very close to it.
                    minIndex = i;
                }

                // ignoring already visited landmarks.
                for (String location: explored_paths){
                    if (destinationArray[minIndex].contains(location)) {
                        min = 22.00;
                        break;
                    }
                }

            }
        }
        minArray[0] = min;
        minArray[1] = (double) minIndex;
        return minArray;
    }


    public static void main(String[] args) throws IOException, CsvException {
        Scanner in = new Scanner(System.in);
        String filename = "C:\\Users\\HP\\IdeaProjects\\untitled\\src\\Locations.csv";
        String starting_location = "Balme Library Legon";
        String stop_location = "Night Market Legon";

        // List to store csv lines
        List<String[]> r;
        // reading csv file
        try (CSVReader reader = new CSVReader(new FileReader(filename))){
            r = reader.readAll();
        }

        int row_start_index = 0;
        int column_end_index = 0;

        String[] locations_array = r.get(0);
        System.out.println("Select start and destinations from the list: ");
        for (int i = 0; i < locations_array.length; i++){
            if (i != 0) {System.out.println(i + ". " + locations_array[i]);}
        }

        System.out.println();
        System.out.print("Select start location: ");
        int start = in.nextInt();
        starting_location = locations_array[start];

        System.out.println();
        System.out.print("Select Destination: ");
        int end = in.nextInt();
        stop_location = locations_array[end];

        for(String[] array: r){

            // get index of destination from first row
            if (r.indexOf(array) == 0){
                for (int i = 0; i < array.length; i++){
                    if (array[i].contains(stop_location)){
                        column_end_index = i;
                    }
                }
            }


            // get index of source from first column
            if (array[0].contains(starting_location)) {
                int index = r.indexOf(array);
                row_start_index = index;
            }

        }

        System.out.println(row_start_index);
        System.out.println(column_end_index);

        // variables
        double actual_distance = 0;

        // Finding total distance
        actual_distance = Double.parseDouble(r.get(row_start_index)[column_end_index]);

        System.out.println();
        System.out.println("Start: " + starting_location);
        System.out.println("Destination: " + stop_location);

        String[] destinationArray = r.get(0);    // the first row in the csv
        String[] startArray = r.get(row_start_index);

        // routes
        Map<String, Double> routes = new LinkedHashMap<String, Double>();

        // explored landmarks
        List<String> exploredPathIndexes = new ArrayList<>();
        exploredPathIndexes.add(starting_location);

        double current_distance_to_destination = actual_distance;
        Double[] min_array = {};

        // loop to generate routes
        for (int j = 0; j< 5; j++){
            // main routes fxn
            min_array = PathFinder(r, startArray, destinationArray,  actual_distance, exploredPathIndexes, current_distance_to_destination);
            int minIndex = min_array[1].intValue();
            double min = min_array[0];

            //insert returned routes
            routes.put(destinationArray[minIndex], min);

            // add recently explored routes
            exploredPathIndexes.add(destinationArray[minIndex]);

            if (j == 4) {routes.put(stop_location, Double.parseDouble(startArray[column_end_index]));} // after 3 landmarks
            startArray = r.get(minIndex);
            current_distance_to_destination = Double.parseDouble(startArray[column_end_index]);
        }


        System.out.println(routes);

        // total distance
        double total_distance = 0;

        System.out.print(starting_location);
        for (String name: routes.keySet()) {
            String key = name.toString();
            total_distance += routes.get(name);
            System.out.print(" => " + key);
        }
        System.out.println();
        System.out.println("Total Distance: " + total_distance + "km");

        int average_car_speed = 50;
        double time_taken  = total_distance/average_car_speed;
        // time taken
        System.out.println("Time taken: " + time_taken + " hour(s)");
    }

}

