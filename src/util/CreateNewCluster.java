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

import model.Sensor;

public class CreateNewCluster {
	
	HashSet<Sensor> sensors = DataProvider.getSensors();
	HashSet<Sensor> cluster = new HashSet<>();
	HashSet<Sensor> capableSources = new HashSet<>();
	HashSet<Sensor> capableSinks = new HashSet<>();
	
	public void createNewCluster() {
		
	}
	
	public void readFile() {
		try {
			// read xml file
				File inputFile = new File("input\\cluster2.txt");
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
	
	private String checkSourceExist(HashSet<Sensor> sensors) {
		for (Sensor sensor : sensors) {
			if (sensor.getsType() == 1) {
				return sensor.getId();
			}
		}
		return null;
	}
	
	private String checkSinkExist(HashSet<Sensor> sensors) {
		for (Sensor sensor : sensors) {
			if (sensor.getsType() == 1) {
				return sensor.getId();
			}			
		}
		return null;
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
