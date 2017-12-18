package Editor;

import Petrinet.*;
import PetrinetXML.PnmlImporter;

import org.antlr.v4.runtime.*;
import AST.*;
public class Verify 
{
	private Program prog = null;
	
	private  Graph genGraph(String file, Petrinet pt) throws Exception {
		//Program prog = null;
		ANTLRFileStream source = new ANTLRFileStream(file); // in bin folder
		PTNETLexer lexer = new PTNETLexer(source);
		CommonTokenStream tokens = new CommonTokenStream(lexer);	
		PTNETParser parser = new PTNETParser(tokens);	
		prog = (Program)new ASTGen().visit(parser.program()); // program object	
		Graph g = pt.WSNGenerate(prog);
		return g;	
	}
	
	public String getVeriInfo(String pnmlFile, String txtFile, String min_txtFile) throws Exception 
	{
		/*
		* Read pnml file
		*/
		Pnml pnml = new PnmlImporter().readFromFile(pnmlFile); // in bin folder
		Petrinet pt = pnml.getPetrinet();  // Petri net object
		
		Graph g = genGraph(txtFile, pt);
		//writeStringToFile(s);
    StringBuilder sb = new StringBuilder();
    
    System.out.println("\nThe reachability graph has " + g.getSize() + " nodes.");

		SearchStmt stmt = (SearchStmt)prog.funcList.get("main").block.stmts.get(0);
		String congest = g.search(stmt, prog.constList);
    if (congest.isEmpty()) {
      System.out.println("No congestion is found");
      return sb.toString();
    } else 
    {

        Vertex congestState = g.searchState(stmt, prog.constList);
        System.out.println(congest + "\nCongest state is " + congestState.toString());
        return sb.toString();
        // sb.append(congest).append("Congest state is ").append(congestState.toString()).append("\n");
  			/*sb.append(congest);		
  			String Hcongest = g.newSearch(stmt, prog.constList);
  			if (Hcongest.isEmpty()) {
  				System.out.println("No congestion is found");
  				return sb.toString();
  			} else {
  				System.out.println(Hcongest);
  				
  				return sb.toString();
  			}*/
  	      
  	      
    }
		
	}
}
