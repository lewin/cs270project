/**
 * Scheduler class.
 * Main scheduling class. 
 * Loads slot and tutor data from JSON and performs matching based on 
 * additional options.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;


public class Schedule {

    public static void main(String[] args) {

        // TODO parse command line arguments, do the right things
        // apache cli? any other recommendations? (I don't know java)
        String jsonfile = null;
        if (args.length > 0) {
            jsonfile = args[0];
        }

        Gson gson = new Gson();

        try {
            if (jsonfile == null) {
                System.exit(1);
            }

            BufferedReader br = new BufferedReader(
                new FileReader(jsonfile));
            Data dat = gson.fromJson(br,Data.class);
            dat.init();

            System.out.println(dat);

            System.out.println(dat.assignments());

            Waiter w = new Waiter();

            IntegerLP.match(dat.tutors, dat.slots, w);
            //Matcher.match(dat.tutors, dat.slots, w);

            System.out.println(dat.assignments());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
