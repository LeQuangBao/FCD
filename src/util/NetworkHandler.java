package util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import model.Channel;
import model.Sensor;
import model.WSN;

public class NetworkHandler {
	public static boolean hasAdjacentNode(WSN wsn, Sensor sensor) {
		HashSet<Channel> channels = wsn.getChannels();
		for (Channel c : channels) {
			if (c.getFirstSensor() == sensor) {
				return true;
			}
		}
		return false;
	}

	public static HashSet<Sensor> getAdjacentNode(WSN wsn, Sensor sensor) {
		HashSet<Channel> channels = wsn.getChannels();
		HashSet<Sensor> hashSensor = new HashSet<>();
		for (Channel c : channels) {
			if (c.getFirstSensor() == sensor) {
				hashSensor.add(c.getSecondSensor());
			}
		}
		return hashSensor;
	}
	
	public static HashSet<Sensor> getAdjacentNode(WSN cluster, WSN original) {
		HashSet<Channel> channels = original.getChannels();
		HashSet<Sensor> adjacentNodes = new HashSet<>();
		for (Channel c : channels) {
			if (cluster.hasSensor(c.getFirstSensor()) && !cluster.hasSensor(c.getSecondSensor())) {
				adjacentNodes.add(c.getSecondSensor());
			}
			if (!cluster.hasSensor(c.getFirstSensor()) && cluster.hasSensor(c.getSecondSensor())) {
				adjacentNodes.add(c.getFirstSensor());
			}
		}
		return adjacentNodes;
	}
	public static HashSet<Sensor> getImportantNode(WSN cluster, WSN original) {
		HashSet<Channel> channels = original.getChannels();
		HashSet<Sensor> importantNodes = new HashSet<>();
		for (Sensor s : cluster.getSensors()) {
			if (s.getsType() == 1 || s.getsType() == 2) {
				importantNodes.add(s);
			}
		}
		for (Channel c : channels) {
			if (cluster.hasSensor(c.getFirstSensor()) && !cluster.hasSensor(c.getSecondSensor())) {
				importantNodes.add(c.getSecondSensor());
			}
			if (!cluster.hasSensor(c.getFirstSensor()) && cluster.hasSensor(c.getSecondSensor())) {
				importantNodes.add(c.getFirstSensor());
			}
		}
		return importantNodes;
	}
	
	public static Sensor findSensorByName(String name, HashSet<Sensor> sensors) {
		for (Sensor s : sensors) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

}
