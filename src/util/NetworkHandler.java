package util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import KWNS.Channel;
import KWNS.Sensor;
import KWNS.WSN;

public class NetworkHandler {
	
	public static HashSet<LinkedHashSet<Sensor>> paths = new HashSet<>();
	
	public static void findPath(WSN wsn, Sensor s1, Sensor s2) {
		pathFinding(wsn, s1, s2, new LinkedHashSet<Sensor>());
	}

	public static void pathFinding(WSN wsn, Sensor current, Sensor target, LinkedHashSet<Sensor> path) {
		if (!current.is(target)) {
			current.setVisited(true);
		}
		path.add(current);
		HashSet<Sensor> nextNode = NetworkHandler.getUnvisitedAdjacentNode(wsn, current);
		for (Sensor s : nextNode) {
			if (s.is(target)) {
				path.add(target);
				storePath(path);
				path.remove(target);
			} else {
				pathFinding(wsn, s, target, new LinkedHashSet<Sensor>(path));
			}
		}
	}

	public static void storePath(LinkedHashSet<Sensor> sensors) {
		paths.add(sensors);
		for (Sensor s : sensors) {
			System.out.print(s.getId() + " ");
		}
		System.out.println();
	}

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

	public static HashSet<Sensor> getUnvisitedAdjacentNode(WSN wsn, Sensor sensor) {
		HashSet<Channel> channels = wsn.getChannels();
		HashSet<Sensor> hashSensor = new HashSet<>();
		for (Channel c : channels) {
			if (c.getFirstSensor() == sensor && !c.getSecondSensor().isVisited()) {
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
				c.getSecondSensor().addChannels(c);
			}
			if (!cluster.hasSensor(c.getFirstSensor()) && cluster.hasSensor(c.getSecondSensor())) {
				adjacentNodes.add(c.getFirstSensor());
				c.getFirstSensor().addChannels(c);
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

	public static Sensor findSensorById(String id, HashSet<Sensor> sensors) {
		for (Sensor s : sensors) {
			if (s.getId().equals(id)) {
				return s;
			}
		}
		return null;
	}

	public static Sensor gather(HashSet<Sensor> sensors) {
		String id = "";
		String name = "";
		boolean init = false;
		int sType = 0;
		;
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
			maxProcessingRate = maxProcessingRate > s.getMaxProcessingRate() ? s.getMaxProcessingRate()
					: maxProcessingRate;
			maxBufferSize = maxBufferSize > s.getMaxBufferSize() ? s.getMaxBufferSize() : maxBufferSize;
			x = s.getX();
			y = s.getY();
			width = s.getWidth();
		}
		Sensor sensor = new Sensor(id, name, init, sType, maxSendingRate, maxProcessingRate, maxBufferSize,
				maxQueueSize, x, y, width);
		return sensor;
	}

	public static void setChannelName(Channel channel) {
		Sensor first = channel.getFirstSensor();
		Sensor second = channel.getSecondSensor();
		channel.setId(first.getId() + "_" + second.getId());
	}

	public static void printQueue(Queue<Sensor> queue) {
		Queue<Sensor> q = new LinkedList<Sensor>(queue);
		while (!q.isEmpty()) {
			Sensor s = (Sensor) q.remove();
			System.out.print(s.getId() + " ");
		}
		System.out.println();
	}

	public static void printHash(HashSet<Sensor> sensors) {
		for (Sensor s : sensors) {
			System.out.print(s.getId() + " ");
		}
		System.out.println();
	}

	public static void printList(List<Sensor> sensors) {
		for (Sensor s : sensors) {
			System.out.print(s.getId() + " ");
		}
		System.out.println();
	}

}
