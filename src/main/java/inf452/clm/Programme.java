package inf452.clm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sat4j.core.VecInt;
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
        final int MAXVAR = 1000000;
        final int NBCLAUSES = 500000;

        ISolver solver = SolverFactory.newDefault();
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);

        // IMPLEMENTATION
        int nbLigne = 4;
        int nbColone = 4;

        try {
            // i = [1...nbLigne]
            // j = [1.. nbColone-2]
            // REGLE 1 == Chaque ligne ou colonne ne doit pas comporter plus de deux 0 ou deux 1 à la suite.

            // on place notre matrice de la forme ij == d'ou i*10 + j
            for(int i =1; i != nbLigne+1; i++){
                for(int j=1; j!= nbColone-2+1; j++){
                    int[] array = new int[3];
                    array[0] = i*10+j;     
                    array[1] = i*10+j+1;
                    array[2] = i*10+j+2;

                    System.out.println( (i*10+j) + " | " + (i*10+j+1) + " | "+ (i*10+j+2));
                    // premiere clause j ou j+1 ou j+2
                    
                    solver.addClause(new VecInt(array));

                    array[0] = (-(i*10+j));
                    array[1] = (-(i*10+j+1));
                    array[2] = (-(i*10+j+2));
                    // premiere clause -(j) ou -(j+1) ou -(j+2)

                    System.out.println( (-(i*10+j)) + " | " + (-(i*10+j+1)) + " | "+ (-(i*10+j+2)));
                    solver.addClause(new VecInt(array));
                }
            }

            for(int j =1; j != nbColone+1; j++){
                for(int i=1; i != nbLigne-2+1; i++){
                    System.out.println( (i*10+j) + " | " + ((i+1)*10+j) + " | "+ ((i+2)*10+j));
                    int[] array = new int[3];

                    array[0] = (i*10+j);
                    array[1] = ((i+1)*10+j);
                    array[2] = ((i+2)*10+j);
                    solver.addClause(new VecInt(array));

                    array[0] = (-(i*10+j));
                    array[1] = (-((i+1)*10+j));
                    array[2] = (-((i+2)*10+j));
                    solver.addClause(new VecInt(array));
                }
            }

            // REGLE 2 == PAS DE COLONE IDENTIQUE 


            for(int i1 =1; i1 != nbLigne+1; i1++){
                for(int i2=1; i2 != nbLigne+1; i2++){
                    if(i1 != i2){
                        String line = "";

                        for(int j=1; j != nbColone+1; j++){
                            line += "("+ (i1*10+j) + " or " + (i2*10+j) + ") and (" + -(i1*10+j) + " or " + -(i2*10+j) + ") or "; // (i1,j != i2,j) <=> i1j ou i2j et (-i1,j ou -12,j)
                        }

                        line = line.substring(0, line.length()-3);
                        
                        // On fait appel a notre api qui va distribuer les ou et simplifier au maximum
                        ArrayList<String> tmps = CNF_API.run(line);
                        System.out.println(tmps.get(tmps.size()-1));
                        
                        for(String a:tmps){
                            String[] l = a.split("\\s+");
                            int[] c = new int[nbColone*2];
                         
                            for(int i=0; i != l.length; i++){
                                c[i] = Integer.parseInt(l[i]);
                            }
                            solver.addClause(new VecInt(c));
                        }

                        System.out.println("FIN" );

                    }
                }
            }



    /*        int[] array = new int[1];
            array[0] = 14;
            solver.addClause(new VecInt(array));*/



        } catch (ContradictionException e1) {
            e1.printStackTrace();
        }
    
        IProblem problem = solver;

        try {
            if (problem.isSatisfiable()) {

                System.out.println("Satisfiable !");

                int[] mfinal = problem.model();

                for (int i = 0; i != mfinal.length; i++){
                    System.out.print(((mfinal[i] > 0)? "1" : "0") + " ");

                    if((i+1) % nbLigne == 0)
                        System.out.println();
                }

            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    
}
