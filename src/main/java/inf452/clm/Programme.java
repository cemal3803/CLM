package inf452.clm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
 
/**
 * Hello world!
 * 
 */
public class Programme 
{
    public static void main( String[] args )
    {
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
                       
       
       
        DimacsReader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out,true);
        // CNF filename is given on the command line 
        try {
            IProblem problem = reader.parseInstance("a.txt");
            
            // ADD CLAUTH

            if (problem.isSatisfiable()) {
                
                System.out.println("Satisfiable !");

                int[] mfinal = problem.model();
                for(int i=0; i!= mfinal.length; i++)
                System.out.print(mfinal[i]+ " ");

            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found !");

        } catch (ParseFormatException e) {
            System.out.println("Parse format exception!");

        } catch (IOException e) {
            System.out.println("IOException !");

        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");      
        }
    }
    
}
