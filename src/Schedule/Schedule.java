/**
 * Schedule class.
 * Main scheduling class. 
 * Loads slot and tutor data from JSON and performs matching based on 
 * additional options.
 *
 * usage: Schedule [jsonfile]
 */

package Schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import Items.Data;
import LPSolve.IntegerLP;
import MaxWeightMatching.Matcher;
import Weighting.Butler;
import Weighting.Chef;
import Weighting.Evaluator;
import Weighting.Gardener;
import Weighting.Waiter;
import Weighting.Weighting;

import com.google.gson.Gson;

public class Schedule {

    public static void main(String[] args) {

        try {
            // command line parsing using jopt-simple
            OptionParser parser = new OptionParser();
            // which solver to use
            OptionSpec<String> solver = parser
                    .acceptsAll(Arrays.asList("s", "solver"),
                            "maxweight (default), lp").withRequiredArg()
                    .ofType(String.class).describedAs("solution algorithm");
            // which weighting to use
            OptionSpec<String> weighting = parser
                    .acceptsAll(Arrays.asList("w", "weighting"),
                            "Waiter (default), Butler, Chef, Gardener")
                    .withRequiredArg().ofType(String.class)
                    .describedAs("name of Weighting");
            // optional file output
            OptionSpec<File> fileout = parser
                    .acceptsAll(Arrays.asList("o", "output"), "file output")
                    .withRequiredArg().ofType(File.class);
            // display help
            parser.acceptsAll(Arrays.asList("h", "help", "?"),
                    "show this help message").forHelp();

            OptionSet options = parser.parse(args);

            // if help was asked for, only help, then exit
            if (options.has("help")) {
                parser.printHelpOn(System.out);
                System.exit(0);
            }

            String jsonfile = null;

            List<String> nonoptions = options.nonOptionArguments();
            if (nonoptions.size() != 1) {
                improperUsage();
                System.exit(1);
            } else {
                jsonfile = nonoptions.get(0);
            }

            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(jsonfile));
            Data dat = gson.fromJson(br, Data.class);
            dat.init();

            Weighting w = new Gardener();
            if (options.has("weighting")) {
                if (weighting.value(options).equals("Waiter")) {
                    w = new Waiter();
                } else if (weighting.value(options).equals("Butler")) {
                    w = new Butler();
                } else if (weighting.value(options).equals("Chef")) {
                    w = new Chef();
                } else if (weighting.value(options).equals("Gardener")) {
                    w = new Gardener();
                } else {
                    System.err.println("Invalid weight function");
                    System.exit(1);
                }
            }

            if (options.has("solver")) {
                if (solver.value(options).equals("maxweight")) {
                    Matcher.match(dat.tutors, dat.slots, w);
                } else if (solver.value(options).equals("lp")) {
                    IntegerLP.match(dat.tutors, dat.slots, w);
                } else {
                    System.err.println("Invalid solving algorithm");
                    System.exit(1);
                }
            } else {
                Matcher.match(dat.tutors, dat.slots, w);
            }

            if (options.has("output")) {
                PrintWriter fout = new PrintWriter(fileout.value(options));
                fout.println(dat.assignments());
                fout.close();
            } else {
                System.out.println(dat.assignments());
                System.out.println(Evaluator.evaluate(dat, w));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void improperUsage() {
        System.out.println("usage: Schedule <json file>");
    }
}
