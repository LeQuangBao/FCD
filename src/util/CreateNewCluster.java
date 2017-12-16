package util;

import java.io.File;
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
	private Channel sourceChannel;
	private Channel sinkChannel;
	private final String wsnPATH = "input\\WSN\\file-kwsn\\wsn.kwsn";
	private final String inPATH = "input\\WSN\\dense-cluster\\";
	private final String outPATH = "output\\";
	
	public void autoCreateNewCluster() {
		int index = 0;
		String fileName;
		File file;;
		
		while (true) {
			fileName = "cluster" + index + ".kwsn";
			file = new File(inPATH+fileName);
			System.out.println("Processing: " + inPATH + fileName);
			if (file.exists()) {
				try {
					action(fileName);
					System.out.println("\tProcessed Successfully!");
					index++;
				} catch(Exception ex) {
					System.out.println("\tProcess Failed!\t"+ex);
					break;
				}
			} else {
				break;
			}
		}
	}
	
	public void action(String fileName) {
		initializeData(inPATH + fileName);
		findSourceSink();
		findChannelForSourceSink();
		createWSNFile(fileName);
	}
	
	private void initializeData(String clusterPath) {
		ReadXMLFile reader = new ReadXMLFile();
		
		wsn = reader.readFile(wsnPATH);
		cluster = reader.readFile(clusterPath);
	}
		
	public void findSourceSink() {
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
			source.setsType(1);
			source.setInit(true);
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
			sink.setsType(2);
		}
	}
	
	private void findChannelForSourceSink() {
		HashSet<Channel> channels = wsn.getChannels();
		
		int max = Integer.MAX_VALUE;
		int min = Integer.MIN_VALUE;
		
		for (Channel channel : channels) {
			// For Source
			if (checkSensorExist(channel.getFirstSensor(), capableSources)) {
				if (channel.getMaxSendingRate() > min) {
					min = channel.getMaxSendingRate();
					sourceChannel = channel;
				}
			}
			
			// For Sink
			if (checkSensorExist(channel.getSecondSensor(), capableSinks)) {
				if (channel.getMaxSendingRate() < max) {
					max = channel.getMaxSendingRate();
					sinkChannel = channel;
				}
			}
		}
		sourceChannel.setFirstSensor(source);
		sinkChannel.setSecondSensor(sink);
	}
	
	private void createWSNFile(String fileName) {
		HashSet<Sensor> sensors = cluster.getSensors();
		sensors.add(source);
		sensors.add(sink);
		
		HashSet<Channel> channels = cluster.getChannels();
		channels.add(sourceChannel);
		channels.add(sinkChannel);
		
		cluster.setSensors(sensors);
		cluster.setChannels(channels);
		
		WriteXMLFile.write(cluster, outPATH + fileName);
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
	
	private boolean checkSensorExist(Sensor sensor, HashSet<Sensor> sensors) {
		String id = sensor.getId();
		for (Sensor ss : sensors) {
			if (id.equals(ss.getId())) {
				return true;
			}
		}
		return false;
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
