package LPSolve;
/*
 * Models the problem as a linear program and solves using lp_solve's ILP.
 */


import java.util.*;

import Weighting.Weighting;

import Items.Slot;
import Items.Tutor;

import lpsolve.*;

public class IntegerLP {

    private static Tutor [] t; 
    private static Slot [] s; 
    private static Weighting w;
    private static int n; // number of variables = t.length * s.length

    // some structures for dealing with simultaneous slot constraints
    private static ArrayList<String> uniqueTimes;
    private static HashMap<String, ArrayList<Integer>> times;
    
    private static final int MAX_ASSIGNMENTS_PER_TUTOR = 2;

    public static void match (Tutor [] tutors, Slot [] slots, Weighting waiter) {
        t = tutors; s = slots; w = waiter;
        n = t.length * s.length;

        try {
            // create the LP solver: 0 constraints (for now), 
            // the variables are bindings between tutors and slots
            LpSolve solver = LpSolve.makeLp(0, n);
            // make lp_solve shut up about things that aren't important
            solver.setVerbose(3); 
            // set all variables to binary
            for (int i = 1; i < n+1; ++i) {
                solver.setBinary(i, true);
            }

            // OBJECTIVE FUNCTION (weight of each assignment)
            solver.setObjFn(getObjective());
            solver.setMaxim(); // sets the obj function to maximize

            // CONSTRAINTS
            // for each slot, assign exactly one (TODO modify for at least one) 
            for (int j = 0; j < s.length; ++j) {
                solver.addConstraint(
                    getConstraintSlotAtLeastOne(j), 
                    LpSolve.EQ, 
                    1);
            }
            // for each tutor, cannot assign more than twice
            for (int i = 0; i < t.length; ++i) {
                solver.addConstraint(
                    getConstraintTutorAtMostN(i), 
                    LpSolve.LE, 
                    MAX_ASSIGNMENTS_PER_TUTOR);
            }
            // for each tutor, cannot to assign to two slots that share time
            calculateSimultaneousSlots();
            for (int i = 0; i < t.length; ++i) {
                for (String time : uniqueTimes) {
                    solver.addConstraint(
                        getConstraintTutorNoSimultaneous(i, time),
                        LpSolve.LE, 
                        1);
                }
            }

            // ilp solution
            solver.solve();

            // use solution to set the assignments of each tutor
            setMatching(solver.getPtrVariables());

            // cleanup
            solver.deleteLp();

        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

    private static double[] getObjective() {
        double[] obj = new double[n];
        for (int i = 0; i < t.length; ++i) {
            for (int j = 0; j < s.length; ++j) {
                obj[(s.length * i) + j] = w.weight(t[i], s[j]);
            }
        }
        return obj;
    }

    private static double[] getConstraintTutorAtMostN(int tutindex) {
        double[] constraint = new double[n];
        Arrays.fill(constraint, 0);
        for (int j = 0; j < s.length; ++j) {
            constraint[(s.length * tutindex) + j] = 1;
        }
        return constraint;
    }

    private static void calculateSimultaneousSlots() {
        uniqueTimes = new ArrayList<String>();
        times = new HashMap<String, ArrayList<Integer>>();
        String key;
        ArrayList<Integer> idxs;
        for (int j = 0; j < s.length; ++j) {
            key = s[j].day + s[j].hour; // this is unique per time
            if (times.get(key) == null) {
                idxs = new ArrayList<Integer>();
                times.put(key, idxs);
                uniqueTimes.add(key);
            }
            times.get(key).add(j);
        }       
    }

    private static double[] getConstraintTutorNoSimultaneous(int tutindex, String time) {
        double[] constraint = new double[n];
        Arrays.fill(constraint, 0);
        for (Integer idx : times.get(time)) {
            constraint[(s.length * tutindex) + idx] = 1;
        }
        return constraint;
    }

    private static double[] getConstraintSlotAtLeastOne(int slotindex) {
        double[] constraint = new double[n];
        Arrays.fill(constraint, 0);
        for (int i = 0; i < t.length; ++i) {
            constraint[(s.length * i) + slotindex] = 1;
        }
        return constraint;
    }

    private static void setMatching(double[] results) {
        double k;
        for (int i = 0; i < t.length; ++i) {
            for (int j = 0; j < s.length; ++j) {
                k = results[(s.length * i) + j];
                if (k > 0) {
                    t[i].slot = s[j];
                    s[j].tutor = t[i];
                }
            }
        }
    }

}
