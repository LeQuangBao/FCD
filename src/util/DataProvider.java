package util;

import java.io.File;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Channel;
import model.Network;
import model.Sensor;
import model.WSN;

public class DataProvider {

	private static HashSet<Sensor> sensors;
	private static HashSet<Channel> channels;
	private static Network network;
	private static model.Process process;
	private static WSN wsn;

	public static WSN getWsn() {
		return wsn;
	}

	public static void setWsn(WSN wsn) {
		DataProvider.wsn = wsn;
	}

	public static void readFile() {
		try {
			// read xml file
			File inputFile = new File("input\\wsn.kwsn");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// parse to NodeList
			NodeList networkList = doc.getElementsByTagName("Network");
			NodeList processList = doc.getElementsByTagName("Process");
			NodeList sensorList = doc.getElementsByTagName("Sensor");
			NodeList channelList = doc.getElementsByTagName("Link");
			NodeList positionList = doc.getElementsByTagName("Position");

			sensors = new HashSet<>();
			channels = new HashSet<>();

			for (int i = 0; i < networkList.getLength(); i++) {
				Node node = networkList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id = element.getAttribute("ID");
					int numberOfSensors = Integer.parseInt(element.getAttribute("NumberOfSensors"));
					int numberOfPackets = Integer.parseInt(element.getAttribute("NumberOfPackets"));
					int sensorMaxBufferSize = Integer.parseInt(element.getAttribute("SensorMaxBufferSize"));
					int sensorMaxQueueSize = Integer.parseInt(element.getAttribute("SensorMaxQueueSize"));
					int channelMaxBufferSize = Integer.parseInt(element.getAttribute("ChannelMaxBufferSize"));
					network = new Network(id, numberOfSensors, numberOfPackets, sensorMaxBufferSize, sensorMaxQueueSize, channelMaxBufferSize);
				}
			}
			for (int i = 0; i < processList.getLength(); i++) {
				Node node = processList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String name = element.getAttribute("Name");
					String parameter = element.getAttribute("Parameter");
					int zoom = Integer.parseInt(element.getAttribute("Zoom"));
					int stateCoutner = Integer.parseInt(element.getAttribute("StateCounter"));
					process = new model.Process(name, parameter, zoom, stateCoutner);
				}
			}
			for (int i = 0; i < sensorList.getLength(); i++) {
				Node node = sensorList.item(i);
				Node nodep = positionList.item(i*2);
				if (node.getNodeType() == Node.ELEMENT_NODE && nodep.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Element elementp = (Element) nodep;
					String id = element.getAttribute("id");
					String name = element.getAttribute("Name");
					boolean init = Boolean.parseBoolean(element.getAttribute("Init"));
					int sType = Integer.parseInt(element.getAttribute("SType"));
					int maxSendingRate = Integer.parseInt(element.getAttribute("MaxSendingRate"));
					int maxProcessingRate = Integer.parseInt(element.getAttribute("MaxProcessingRate"));
					float x = Float.parseFloat(elementp.getAttribute("X"));
					float y = Float.parseFloat(elementp.getAttribute("Y"));
					float width = Float.parseFloat(elementp.getAttribute("Width"));
					sensors.add(new Sensor(id, name, init, sType, maxSendingRate, maxProcessingRate, x, y, width));
				}
			}
			for (int i = 0; i < channelList.getLength(); i++) {
				Node node = channelList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id = element.getAttribute("id");
					String lType = element.getAttribute("LType");
					String cType = element.getAttribute("CType");
					int maxSendingRate = Integer.parseInt(element.getAttribute("MaxSendingRate"));
					float probabilityPathCongestion = Float
							.parseFloat(element.getAttribute("ProbabilityPathCongestion"));
					String from = element.getElementsByTagName("From").item(0).getTextContent();
					String to = element.getElementsByTagName("To").item(0).getTextContent();
					channels.add(new Channel(id, lType, cType, probabilityPathCongestion, maxSendingRate,
							findSensorByName(from), findSensorByName(to)));
				}
			}
			wsn = new WSN(network, process, sensors, channels);
			System.out.println("[Reading xml file completed]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Sensor findSensorByName(String name) {
		for (Sensor s : sensors) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

	public static HashSet<Sensor> getSensors() {
		return sensors;
	}

	public static void setSensors(HashSet<Sensor> sensors) {
		DataProvider.sensors = sensors;
	}

	public static HashSet<Channel> getChannels() {
		return channels;
	}

	public static void setChannels(HashSet<Channel> channels) {
		DataProvider.channels = channels;
	}

}
