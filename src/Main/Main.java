package Main;

import java.io.File;
import java.util.HashSet;

import Editor.DataImport;
import Editor.Verify;
import KWNS.Channel;
import KWNS.Sensor;
import KWNS.WSN;
import util.NetworkHandler;
import util.ReadXMLFile;
import util.WriteXMLFile;
public class Main 
{
	public static void main(String[] args) throws Exception 
	{	
		File aDirectory = new File("Input\\Test\\Cluster");

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
			System.out.println("Verifying file: "+i);
			Verify verify = new Verify();
			flag = verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    	if(flag) {
	    		break;
	    	}
	    }
	    if(!flag) {
	    	File aDirectory2 = new File("Input\\Test\\");
	    	ReadXMLFile readXMLFile = new ReadXMLFile();
	    	String patch = aDirectory2.getPath()+"\\WSN-topology.kwsn";
	    	WSN result = null;
	    	WSN original =readXMLFile.readFile(patch);
	    	
	    	String[] filesInDir2 = aDirectory.list();
		    for ( int i=0; i<filesInDir2.length; i++ )
		    {
		    	String patch2 = aDirectory2.getPath()+"\\Cluster\\"+filesInDir[i];
		    	WSN cluster = readXMLFile.readFile(patch2);
		    	result = gatherNetwork(original, cluster);
		    	original=result;
		    }
		    WriteXMLFile.write(result, "Input\\Test\\NewWSN\\result.kwsn");
		    
		    String patch3 = aDirectory2.getPath()+"\\NewWSN\\result.kwsn";
	    	DataImport readKwsn = new DataImport();
			readKwsn.Import(patch3);
			String pnmlFile = "temp/temp.pnml";
			String txtFile = "temp/temp.txt";
			String min_txtFile = "temp/temp_mini.txt";
			System.out.println("Verifying file new sensor network ");
			Verify verify = new Verify();
			verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    }
	}
	
	public static WSN gatherNetwork(WSN original, WSN cluster) {
		Sensor mainSensor = NetworkHandler.gather(cluster.getSensors());
		HashSet<Sensor> adjacentNode = NetworkHandler.getAdjacentNode(cluster, original);
		for (Sensor s : adjacentNode) {
			float outDelay = Float.MAX_VALUE;
			float inDelay = Float.MAX_VALUE;
			// out channel: from main to adjacent
			// in channel: from adjacent to main
			Channel outChannel = null;
			Channel inChannel = null;
			for (Channel c : s.getChannels()) {
				Sensor first = c.getFirstSensor();
				if (first.is(s)) {
					// in channel
					if (inDelay > c.getDelay()) {
						inDelay = c.getDelay();
						inChannel = c;
					}
				} else {
					// out channel
					if (outDelay > c.getDelay()) {
						outDelay = c.getDelay();
						outChannel = c;
					}
				}
			}
			if (inChannel != null) {
				inChannel.setSecondSensor(mainSensor);
				NetworkHandler.setChannelName(inChannel);
				mainSensor.addChannels(inChannel);
			}
			if (outChannel != null) {
				outChannel.setFirstSensor(mainSensor);
				NetworkHandler.setChannelName(outChannel);
				mainSensor.addChannels(outChannel);
			}
		}
		HashSet<Sensor> allSensor = new HashSet<>(original.getSensors());
		HashSet<Sensor> allClusterSensor = cluster.getSensors();
		HashSet<Sensor> allSensorToDelete = new HashSet<>();
		for (Sensor s : allSensor) {
			for (Sensor s2 : allClusterSensor) {
				if (s.is(s2)) {
					allSensorToDelete.add(s);
				}	
			}
		}
		for (Sensor  s : allSensorToDelete) {
			allSensor.remove(s);
		}
		allSensor.add(mainSensor);
//		
		HashSet<Channel> allChannel = new HashSet<>(original.getChannels());
		HashSet<Channel> allChannelToDelete = new HashSet<>();
		for (Channel c : allChannel) {
			for (Sensor s : allSensorToDelete) {
				if (c.getFirstSensor().is(s) || c.getSecondSensor().is(s)) {
					allChannelToDelete.add(c);
				}
			}
		}
		for (Channel c : allChannelToDelete) {
			allChannel.remove(c);
		}
		
//		HashSet<Channel> allClusterChannel = cluster.getChannels();
//		HashSet<Channel> allOriginalChannel = original.getChannels();
//		for (Channel c : allOriginalChannel) {
//			for (Channel c2 : allClusterChannel) {
//				if (!c2.getFirstSensor().is(c.getFirstSensor()) || !c2.getSecondSensor().is(c.getSecondSensor())) {
//					allChannel.add(c);
//				}	
//			}
//		}
		for (Channel c : mainSensor.getChannels()) {
			allChannel.add(c);
		}
		WSN result = new WSN (cluster.getNetwork(), cluster.getProcess(), allSensor, allChannel);
		
		
		return result;
	}
}

	 