package Main;

import java.io.File;

import Editor.DataImport;
import Editor.Verify;
public class Main 
{
	public static void main(String[] args) throws Exception 
	{	
		File aDirectory = new File("C:\\Users\\Alviss Inugami\\Desktop\\20sensor");

	    String[] filesInDir = aDirectory.list();

	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	String patch = aDirectory.getPath()+"\\"+filesInDir[i];
	    	DataImport readKwsn = new DataImport();
			readKwsn.Import(patch);
			String pnmlFile = "temp/temp.pnml";
			String txtFile = "temp/temp.txt";
			String min_txtFile = "temp/temp_mini.txt";
			System.out.println("Import successful");
			System.out.println("Verifying.....");
			Verify verify = new Verify();
			verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    	
	    }	
	}
}

	 