package Main;

import java.io.File;

import Editor.DataImport;
import Editor.Verify;
public class Main 
{
	public static void main(String[] args) throws Exception 
	{	
		File aDirectory = new File("Input\\");

	    String[] filesInDir = aDirectory.list();

	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	boolean flag;
	    	String patch = aDirectory.getPath()+"\\"+filesInDir[i];
	    	DataImport readKwsn = new DataImport();
			readKwsn.Import(patch);
			String pnmlFile = "temp/temp.pnml";
			String txtFile = "temp/temp.txt";
			String min_txtFile = "temp/temp_mini.txt";
			//System.out.println("Import successful");
			System.out.println("Verifying file: "+i);
			Verify verify = new Verify();
			flag = verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    	if(flag) {
	    		break;
	    	}
	    }	
	}
}

	 