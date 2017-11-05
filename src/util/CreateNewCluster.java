package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Channel;
import model.Sensor;

public class CreateNewCluster {
	
	private static HashSet<Sensor> sensors = DataProvider.getSensors();
	private static HashSet<Channel> channels = DataProvider.getChannels();
	private HashSet<Sensor> cluster = new HashSet<>();
	private HashSet<Sensor> capableSources = new HashSet<>();
	private HashSet<Sensor> capableSinks = new HashSet<>();	
	private Sensor source;
	private Sensor sink;
	
	public void action() {
		readFile();
		selectSourceSink();
		
		System.out.println("\t\tSOURCE");
		System.out.println(source.getName());
		System.out.println("Processing: " + source.getMaxProcessingRate());
		System.out.println("Sending: " + source.getMaxSendingRate());
		System.out.println("\n\n\t\tSINK");
		System.out.println(sink.getName());
		System.out.println("Processing: " + sink.getMaxProcessingRate());
		System.out.println("Sending: " + sink.getMaxSendingRate());
	}
	
	public void selectSourceSink() {
		selectCapableSourcesSinks();
		
		//Select Source
		if (source == null) {
			int processing = Integer.MAX_VALUE;
			int sending = -1;
			
			for (Sensor sensor : capableSources) {
				if (sensor.getMaxProcessingRate() < processing) {
					processing = sensor.getMaxProcessingRate();
				}
				if (sensor.getMaxSendingRate() > sending) {
					sending = sensor.getMaxSendingRate();
					source = sensor;
				}
			}
			
			source.setMaxProcessingRate(processing);
			source.setMaxSendingRate(sending);
		}
		
		//Select Sink
		if (sink == null) {
			int processing = Integer.MAX_VALUE;
			int sending = -1;
			
			for (Sensor sensor : capableSinks) {
				if (sensor.getMaxProcessingRate() < processing) {
					processing = sensor.getMaxProcessingRate();
				}
				if (sensor.getMaxSendingRate() > sending) {
					sending = sensor.getMaxSendingRate();
					sink = sensor;
				}
			}
			
			sink.setMaxProcessingRate(processing);
			sink.setMaxSendingRate(sending);
		}
	}
	
	private void selectCapableSourcesSinks() {
		boolean sourceExist = checkSourceExist();
		boolean sinkExist = checkSinkExist();
		
		if (sourceExist && sinkExist) {
			return;
		} else {		
			Sensor firstSensor;
			Sensor secondSensor;
			
			for(Channel channel : channels) {				
				firstSensor = channel.getFirstSensor();
				secondSensor = channel.getSecondSensor();
				
				//Add capable sensor become source into List
				//Condition: source doesn't exist, fistSensor is outside Cluster, second is in Cluster
				if (!sourceExist && !checkExistInCluster(firstSensor) && checkExistInCluster(secondSensor)) {
					capableSources.add(firstSensor);
				}
				
				//Add capable sensor become sink into List
				//Condition: source doesn't exist, fistSensor is outside Cluster, second is in Cluster
				if (!sinkExist && checkExistInCluster(firstSensor) && !checkExistInCluster(secondSensor)) {
					capableSinks.add(secondSensor);
				}
			}
		}
	}
	
	private void readFile() {
		try {
			// read xml file
			File inputFile = new File("input\\cluster.kwsn");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// parse to NodeList
			NodeList sensorList = doc.getElementsByTagName("Sensor");
			for (int i = 0; i < sensorList.getLength(); i++) {
				Node node = sensorList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE ) {
					Element element = (Element) node;
					String id = element.getAttribute("id");
					cluster.add(findSensorById(id));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
	private boolean checkExistInCluster(Sensor sensor) {
		String id = sensor.getId();
		for (Sensor ss : cluster) {
			if (id.equals(ss.getId())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkSourceExist() {
		for (Sensor sensor : cluster) {
			if (sensor.getsType() == 1) {
				source = sensor;
				return true;
			}
		}
		return false;
	}
	
	private boolean checkSinkExist() {
		for (Sensor sensor : cluster) {
			if (sensor.getsType() == 1) {
				sink = sensor;
				return true;
			}			
		}
		return false;
	}
	
	private Sensor findSensorById(String id) {
		for (Sensor sensor : sensors) {
			if (id.equals(sensor.getId())){
				return sensor;
			}
		}
		return null;
	}
}
