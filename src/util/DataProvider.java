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
import model.Sensor;

public class DataProvider {

	private static HashSet<Sensor> sensors;
	private static HashSet<Channel> channels;

	public static void readFile() {
		try {
			// read xml file
			File inputFile = new File("input\\5-sensors.kwsn");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// parse to NodeList
			NodeList sensorList = doc.getElementsByTagName("Sensor");
			NodeList channelList = doc.getElementsByTagName("Link");

			sensors = new HashSet<>();
			channels = new HashSet<>();

			for (int i = 0; i < sensorList.getLength(); i++) {
				Node node = sensorList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id = element.getAttribute("id");
					String name = element.getAttribute("Name");
					boolean init = Boolean.parseBoolean(element.getAttribute("Init"));
					int sType = Integer.parseInt(element.getAttribute("SType"));
					int maxSendingRate = Integer.parseInt(element.getAttribute("MaxSendingRate"));
					int maxProcessingRate = Integer.parseInt(element.getAttribute("MaxProcessingRate"));
					sensors.add(new Sensor(id, name, init, sType, maxSendingRate, maxProcessingRate));
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
					float probabilityPathCongestion = Float.parseFloat(element.getAttribute("ProbabilityPathCongestion"));
					String from = element.getElementsByTagName("From").item(0).getTextContent();
					String to = element.getElementsByTagName("To").item(0).getTextContent();
					channels.add(new Channel(id, lType, cType, probabilityPathCongestion, maxSendingRate, findSensorByName(from), findSensorByName(to)));
				}
			}
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
