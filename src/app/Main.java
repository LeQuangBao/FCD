package app;

import java.util.HashSet;
import java.util.Iterator;

import model.Channel;
import model.Sensor;
import model.WSN;
import util.CreateNewCluster;
import util.NetworkHandler;
import util.ReadXMLFile;
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		CreateNewCluster creation = new CreateNewCluster();
		creation.autoCreateNewCluster();
		buildTopology();
		step0();
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
		
		WSN original = readXMLFile.readFile("input\\WSN\\file-kwsn\\WSN-topology.kwsn");
		WSN cluster = readXMLFile.readFile("input\\WSN\\dense-cluster\\Cluster1.kwsn");
		WSN result = gatherNetwork(original, cluster);
		cluster = readXMLFile.readFile("input\\WSN\\dense-cluster\\Cluster2.kwsn");
		result = gatherNetwork(result, cluster);
		
		WriteXMLFile.write(result, "output\\WSN\\result.kwsn");
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
