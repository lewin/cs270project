/*
 * Models the problem as a linear program and solves using lp_solve's ILP.
 */

import java.util.*;

import lpsolve.*;


public class IntegerLP {

    private static Tutor [] t; 
    private static Slot [] s; 
    private static Weighting w;
    private static int n;

    public static void match (Tutor [] tutors, Slot [] slots, Weighting waiter) {
        t = tutors; s = slots; w = waiter;
        n = t.length * s.length;

        try {
            // create the LP solver: 0 constraints (for now), 
            // the variables are bindings between tutors and slots
            LpSolve solver = LpSolve.makeLp(0, n);
            // set all variables to binary
            for (int i = 1; i < n+1; ++i) {
                solver.setBinary(i, true);
            }

            // add the objective function (weight of each assignment)
            String objective = "";
            for (int i = 0; i < t.length; ++i) {
                for (int j = 0; j < s.length; ++j) {
                    objective += String.valueOf(w.weight(t[i], s[j])) + " ";
                    
                }
            }
            System.out.println("objective: " + objective.trim());
            solver.strSetObjFn(objective.trim());

            // constraints: for each tutor, only assign to one slot
            String constraint;
            for (int i = 0; i < t.length; ++i) {
                // all 0's
                constraint = new String(new char[n]).replace("\0", "0 ").trim();
                // replace the ith block of s.length 0's with 1's
                constraint = constraint.substring(0, 2*i*s.length) +
                    new String(new char[s.length]).replace("\0", "1 ").trim() +
                    constraint.substring(2*i*s.length + s.length + 1);
                System.out.println("constraint " + i + ": " + constraint);
                solver.strAddConstraint(constraint, LpSolve.GE, 1);
            }

            // ilp solution
            solver.solve();

            // use solution to set the assignments of each tutor
            double[] var = solver.getPtrVariables();
            double k;
            for (int i = 0; i < t.length; ++i) {
                for (int j = 0; j < s.length; ++j) {
                    k = var[i*s.length + j];
                    if (k > 0) {
                        t[i].slot = s[j];
                        s[j].tutor = t[i];
                    }
                }
            }

            System.out.println("Value of obj function: " + solver.getObjective());
            for (int i = 0; i < var.length; i++) {
                System.out.println("Value of var[" + i + "] = " + var[i]);
            }

            // cleanup
            solver.deleteLp();
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

    private static boolean debug;

}
