package app;

import java.util.HashSet;

import model.Channel;
import model.Sensor;
import model.WSN;
<<<<<<< HEAD
import util.CreateNewCluster;
import util.DataProvider;
=======
import util.NetworkHandler;
import util.ReadXMLFile;
>>>>>>> b465f921965fb3ad744602a8b2a2727ca0017367
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		step1();
	}
	
	public static void step1() {
		ReadXMLFile readXMLFile = new ReadXMLFile();
		WSN original = readXMLFile.readFile("input\\WSN\\original4.kwsn");
		WSN cluster = readXMLFile.readFile("input\\WSN\\cluster4.kwsn");
		
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
				System.out.println(inChannel.getId() + " -- in -- " + inChannel.getDelay());
			}
			if (outChannel != null) {
				outChannel.setFirstSensor(mainSensor);
				NetworkHandler.setChannelName(outChannel);
				mainSensor.addChannels(outChannel);
				System.out.println(outChannel.getId() + " -- out -- " + outChannel.getDelay());
			}
		}
		HashSet<Sensor> allNode = (HashSet<Sensor>) adjacentNode.clone();
		allNode.add(mainSensor);
		WSN result = new WSN (cluster.getNetwork(), cluster.getProcess(), allNode, mainSensor.getChannels());
		
		
		WriteXMLFile.write(cluster, "output\\cluster4.kwsn");
		WriteXMLFile.write(original, "output\\original4.kwsn");
		WriteXMLFile.write(result, "output\\result.kwsn");
	}
}
