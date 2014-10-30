/**
 * Schedule class. Main scheduling class. Loads slot and tutor data from JSON and performs matching
 * based on additional options.
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
import Swapper.Swapper;
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
      OptionSpec<String> solver =
          parser.acceptsAll(Arrays.asList("s", "solver"), "maxweight (default), lp")
              .withRequiredArg().ofType(String.class).describedAs("solution algorithm");
      // which weighting to use
      OptionSpec<String> weighting =
          parser
              .acceptsAll(Arrays.asList("w", "weighting"),
                  "Waiter (default), Butler, Chef, Gardener").withRequiredArg()
              .ofType(String.class).describedAs("name of Weighting");
      // optional file output
      OptionSpec<File> fileout =
          parser.acceptsAll(Arrays.asList("o", "output"), "file output").withRequiredArg()
              .ofType(File.class);
      // display help
      parser.acceptsAll(Arrays.asList("h", "help", "?"), "show this help message").forHelp();

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

      Weighting w = new Butler();
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
          Matcher.match(dat, w);
        } else if (solver.value(options).equals("lp")) {
          IntegerLP.match(dat, w);
        } else {
          System.err.println("Invalid solving algorithm");
          System.exit(1);
        }
      } else {
        Matcher.match(dat, w);
      }

      for (int i = 0; i < dat.tutors.length; i++) {
        int c = 0;
        for (int j = 0; j < dat.tutors[i].timeSlots.length; j++)
          if (dat.tutors[i].timeSlots[j] != 0)
            c++;
        System.out.println(dat.tutors[i].name + " " + c / 2);
      }

       double s = 0;
       {
       String assign = dat.readableFormattedAssignments();
       double[] r = Evaluator.evaluate (dat, w, false);
       double stdmin = r[0], maxhap = r[1];
       s = r[1];
       System.out.println(assign);
       System.out.println(stdmin + " " + maxhap);
       }

      System.out.println("Swapping");
      // now do some random swapping to make it stable
      Swapper.stabilize(dat, w);


      // int sum = 0;
      // for (int i = 0; i < dat.tutors.length; i++) {
      // System.out.println (dat.tutors[i].name);
      // int count = 0;
      // for (int j = 0; j < dat.tutors[i].timeSlots.length; j++) {
      // count += dat.tutors[i].timeSlots[j] > 0 ? 1 : 0;
      // }
      // System.out.println (count / 2);
      // sum += count / 2;
      // }
      //
      // System.out.println ((double)sum / dat.tutors.length);

      if (options.has("output")) {
        PrintWriter fout = new PrintWriter(fileout.value(options));
        fout.println(dat.formattedAssignments());
        fout.close();
      } else {
        // String assign = dat.formattedAssignments();
        // double[] r = Evaluator.evaluate (dat, w, true);
        // double stdmin = r[0], maxhap = r[1];
        // System.out.println(assign);
        // System.out.println(stdmin + " " + maxhap);
        // System.out.println (maxhap - s);
      }
      System.out.println("DONE");
      System.out.println(dat.readableFormattedAssignments());
      System.out.println (Arrays.toString(Evaluator.evaluate(dat, w, true)));
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
