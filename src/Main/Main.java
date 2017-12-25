package Main;

import java.io.File;
import java.util.HashSet;

import Editor.DataImport;
import Editor.Verify;
import KWNS.Channel;
import KWNS.Sensor;
import KWNS.WSN;
import util.CreateNewCluster;
import util.NetworkHandler;
import util.ReadXMLFile;
import util.WriteXMLFile;
public class Main 
{
	public static void main(String[] args) throws Exception 
	{	
		boolean flag =VerifyInput();
	    if(!flag) {
	    	buildTopology();
		    String patch ="\\NewWSN\\result.kwsn";
	    	DataImport readKwsn = new DataImport();
			readKwsn.Import(patch);
			String pnmlFile = "temp/temp.pnml";
			String txtFile = "temp/temp.txt";
			String min_txtFile = "temp/temp_mini.txt";
			System.out.println("Verifying file new sensor network ");
			Verify verify = new Verify();
			verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    }
	}
	
	public static Boolean VerifyInput() throws Exception
	{
		CreateNewCluster creation = new CreateNewCluster();
		creation.autoCreateNewCluster();
		
		File Directory = new File("Output\\WSN\\");
		String DENSE_PATH = "dense-cluster";
		String IMBALANCE_PATH = "imbalanced-cluster";
		
		
	    String[] filesInDir = Directory.list();
	    boolean flag = true;
	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	
	    	String patch = Directory.getPath()+"\\"+DENSE_PATH+"\\"+filesInDir[i];
	    	DataImport readKwsn = new DataImport();
			readKwsn.Import(patch);
			String pnmlFile = "temp/temp.pnml";
			String txtFile = "temp/temp.txt";
			String min_txtFile = "temp/temp_mini.txt";
			System.out.println("Verifying file "+DENSE_PATH +": "+i);
			Verify verify = new Verify();
			flag = verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    	if(flag) {
	    		return true;
	    	}
	    }
	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	
	    	String patch = Directory.getPath()+"\\"+IMBALANCE_PATH+"\\"+filesInDir[i];
	    	DataImport readKwsn = new DataImport();
			readKwsn.Import(patch);
			String pnmlFile = "temp/temp.pnml";
			String txtFile = "temp/temp.txt";
			String min_txtFile = "temp/temp_mini.txt";
			System.out.println("Verifying file "+IMBALANCE_PATH +": "+i);
			Verify verify = new Verify();
			flag = verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);
	    	if(flag) {
	    		return true;
	    	}
	    }
		return false;
	}
	public static void buildTopology()
	{
		File Directory = new File("Intput\\WSN\\");
    	ReadXMLFile readXMLFile = new ReadXMLFile();
    	String originalPatch = Directory.getPath()+"\\file-kwsn\\WSN-topology.kwsn";
    	WSN original =readXMLFile.readFile(originalPatch);
    	
    	WSN result = null;
    	
    	 String DENSE_PATH = "dense-cluster";
    	 String IMBALANCE_PATH = "imbalanced-cluster";
    	 
    	String[] filesInDir = Directory.list();
	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	String clusterPatch = Directory.getPath()+"\\"+DENSE_PATH+"\\"+filesInDir[i];
	    	WSN cluster = readXMLFile.readFile(clusterPatch);
	    	result = gatherNetwork(original, cluster);
	    	original=result;
	    }
	    for ( int i=0; i<filesInDir.length; i++ )
	    {
	    	String clusterPatch = Directory.getPath()+"\\"+IMBALANCE_PATH+"\\"+filesInDir[i];
	    	WSN cluster = readXMLFile.readFile(clusterPatch);
	    	result = gatherNetwork(original, cluster);
	    	original=result;
	    }
	    WriteXMLFile.write(result, "NewKWNS\\New-WSN-topology.kwsn");
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

	 