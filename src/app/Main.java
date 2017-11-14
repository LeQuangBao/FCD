package app;

import java.util.HashSet;

import model.Channel;
import model.Sensor;
import model.WSN;
import util.CreateNewCluster;
import util.NetworkHandler;
import util.ReadXMLFile;
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		buildTopology();
//		step0();
	}
	
	public static void step0() {
		ReadXMLFile readXMLFile = new ReadXMLFile();
		WSN wsn = readXMLFile.readFile("input\\WSN\\BreadthFirstSearchTest.kwsn");
		HashSet<Sensor> sensors = wsn.getSensors();
		Sensor s1 = NetworkHandler.findSensorById("1", sensors);
		Sensor s2 = NetworkHandler.findSensorById("5", sensors);
		NetworkHandler.findPath(wsn, s1, s2);
	}
	
	public static void buildTopology() {
		
		ReadXMLFile readXMLFile = new ReadXMLFile();
		WSN original = readXMLFile.readFile("input\\WSN\\original.kwsn");
		WSN cluster = readXMLFile.readFile("input\\WSN\\cluster1.kwsn");
		WSN result = gatherNetwork(original, cluster);
		WriteXMLFile.write(result, "input\\WSN\\result.kwsn");
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
//		HashSet<Sensor> allOriginalSensor = original.getSensors();
//		HashSet<Sensor> allClusterSensor = cluster.getSensors();
		HashSet<Sensor> allSensor = new HashSet<>();
		allSensor = original.getSensors();
//		
//		for (Sensor s : allOriginalSensor) {
//			boolean same = false;
//			for (Sensor s2 : allClusterSensor) {
//				if (s.is(s2)) {
//					same = true;
//				}	
//			}
//			if (!same) {
//				allSensor.add(s);
//			}
//		}
//		allSensor.add(mainSensor);
//		
//		HashSet<Channel> allOriginalChannel = original.getChannels();
//		HashSet<Channel> allClusterChannel = cluster.getChannels();
		HashSet<Channel> allChannel = new HashSet<>(original.getChannels());
		
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
