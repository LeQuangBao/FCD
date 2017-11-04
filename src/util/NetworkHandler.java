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
	
	public static Sensor gather(HashSet<Sensor> sensors) {
		String id = "";
		String name = "";
		boolean init = false;
		int sType = 0;;
		int maxSendingRate = 0;
		int maxProcessingRate = Integer.MAX_VALUE;
		int maxBufferSize = Integer.MAX_VALUE;
		int maxQueueSize = 0;
		float x = 0f;
		float y = 0f;
		float width = 0f;
		for (Sensor s : sensors) {
			id = s.getId();
			name = s.getName();
			init = s.isInit();
			if (sType != 1 && sType != 2) {
				if (s.getsType() == 1 || s.getsType() == 2) {
					sType = s.getsType();
				} else {
					sType = 3;
				}
			}
			maxSendingRate = maxSendingRate < s.getMaxSendingRate() ? s.getMaxSendingRate() : maxSendingRate;
			maxProcessingRate = maxProcessingRate > s.getMaxProcessingRate() ? s.getMaxProcessingRate() : maxProcessingRate;
			maxBufferSize = maxBufferSize > s.getMaxBufferSize() ? s.getMaxBufferSize() : maxBufferSize;
			x = s.getX();
			y = s.getY();
			width = s.getWidth();
		}
		Sensor sensor = new Sensor(id, name, init, sType, maxSendingRate, maxProcessingRate, maxBufferSize, maxQueueSize, x, y, width);
		return sensor;
	}
}
