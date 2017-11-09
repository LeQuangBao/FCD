package util;

import java.util.HashSet;

import model.Channel;
import model.Sensor;
import model.WSN;

public class CreateNewCluster {
	
	private WSN wsn;
	private WSN cluster;
	private HashSet<Sensor> capableSources;
	private HashSet<Sensor> capableSinks;	
	private Sensor source;
	private Sensor sink;
	
	public void action() {
		initializeData();
		selectSourceSink();
		System.out.println("----------------------------");
		System.out.println("\t\tSOURCE");
		System.out.println(source.getName());
		System.out.println("Processing: " + source.getMaxProcessingRate());
		System.out.println("Sending: " + source.getMaxSendingRate());
		System.out.println("\n\n\t\tSINK");
		System.out.println(sink.getName());
		System.out.println("Processing: " + sink.getMaxProcessingRate());
		System.out.println("Sending: " + sink.getMaxSendingRate());
	}
	
	private void initializeData() {
		ReadXMLFile reader = new ReadXMLFile();
		
		wsn = reader.readFile("input\\wsn.kwsn");
		cluster = reader.readFile("input\\cluster.kwsn");
	}
	
//	private void readFile() {
//		try {
//			// read xml file
//			File inputFile = new File("input\\cluster.kwsn");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(inputFile);
//			doc.getDocumentElement().normalize();
//
//			// parse to NodeList
//			NodeList sensorList = doc.getElementsByTagName("Sensor");
//			for (int i = 0; i < sensorList.getLength(); i++) {
//				Node node = sensorList.item(i);
//				if (node.getNodeType() == Node.ELEMENT_NODE ) {
//					Element element = (Element) node;
//					String id = element.getAttribute("id");
//					cluster.add(findSensorById(id));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
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
		
		capableSources = new HashSet<>();
		capableSinks = new HashSet<>();
		
		if (sourceExist && sinkExist) {
			return;
		} else {	
			HashSet<Channel> channels = wsn.getChannels();
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
				
	private boolean checkExistInCluster(Sensor sensor) {
		String id = sensor.getId();
		HashSet<Sensor> sensors = cluster.getSensors();
		for (Sensor ss : sensors) {
			if (id.equals(ss.getId())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkSourceExist() {
		HashSet<Sensor> sensors = cluster.getSensors();
		for (Sensor sensor : sensors) {
			if (sensor.getsType() == 1) {
				source = sensor;
				return true;
			}
		}
		return false;
	}
	
	private boolean checkSinkExist() {
		HashSet<Sensor> sensors = cluster.getSensors();
		for (Sensor sensor : sensors) {
			if (sensor.getsType() == 2) {
				sink = sensor;
				return true;
			}			
		}
		return false;
	}
	
	private Sensor findSensorById(String id, HashSet<Sensor> sensors) {
		for (Sensor sensor : sensors) {
			if (id.equals(sensor.getId())){
				return sensor;
			}
		}
		return null;
	}
}
