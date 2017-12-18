package Main;

import java.io.File;

import Editor.DataImport;
import Editor.Verify;
import KWNS.WSN;
import util.ReadXMLFile;
public class Main 
{
	public static void main(String[] args) throws Exception 
	{	
		File aDirectory = new File("Input\\");

	    String[] filesInDir = aDirectory.list();
	    boolean flag = true;
	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	
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
	    if(!flag) {
	    	ReadXMLFile readXMLFile = new ReadXMLFile();
	    	String patch = aDirectory.getPath();
	    	WSN wsn =readXMLFile.readFile(patch);
	    }
	}
}

	 