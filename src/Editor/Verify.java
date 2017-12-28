package Editor;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import AST.ASTGen;
import AST.PTNETLexer;
import AST.PTNETParser;
import AST.Program;
import AST.SearchStmt;
import Petrinet.Graph;
import Petrinet.Petrinet;
import Petrinet.Pnml;
import Petrinet.Vertex;
import PetrinetXML.PnmlImporter;

public class Verify 
{
	private static Program prog = null;
	
	private static Graph genGraph(String file, Petrinet pt) throws Exception {
		//Program prog = null;
		ANTLRFileStream source = new ANTLRFileStream(file); // in bin folder
		PTNETLexer lexer = new PTNETLexer(source);
		CommonTokenStream tokens = new CommonTokenStream(lexer);	
		PTNETParser parser = new PTNETParser(tokens);	
		prog = (Program)new ASTGen().visit(parser.program()); // program object	
		Graph g = pt.WSNGenerate(prog);
		return g;	
	}
	
	public Boolean getVeriInfo(String pnmlFile, String txtFile, String min_txtFile) throws Exception 
	{
		/*
		* Read pnml file
		*/
		Pnml pnml = new PnmlImporter().readFromFile(pnmlFile); // in bin folder
		Petrinet pt = pnml.getPetrinet();  // Petri net object
		
		Graph g = genGraph(txtFile, pt);
		//writeStringToFile(s);
    //StringBuilder sb = new StringBuilder();
		boolean flag =true;
    
    System.out.println("\nThe reachability graph has " + g.getSize() + " nodes.");
		if (g.getSize()>300000) {
			System.out.println(" (partial).\n");
		} else {
			System.out.println(" (full).\n");
		}
		SearchStmt stmt = (SearchStmt)prog.funcList.get("main").block.stmts.get(0);
		String congest = g.search(stmt, prog.constList);
    if (congest.isEmpty()) {
      System.out.println("No congestion is found");
      flag=false;
      return flag;
    } else 
    {

        Vertex congestState = g.searchState(stmt, prog.constList);
        System.out.println(congest + "\nCongest state is " + congestState.toString());
        return flag;
        // sb.append(congest).append("Congest state is ").append(congestState.toString()).append("\n");
  	           
    }
		
	}
}
