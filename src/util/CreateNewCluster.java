package util;

import java.io.File;
import java.util.HashSet;

import KWNS.Channel;
import KWNS.Sensor;
import KWNS.WSN;

public class CreateNewCluster {
	
	private WSN wsn;
	private WSN cluster;
	private HashSet<Sensor> capableSources;
	private HashSet<Sensor> capableSinks;	
	private Sensor source;
	private Sensor sink;
	private Channel sourceChannel;
	private Channel sinkChannel;
	private final String IN_WSN_PATH = "Input\\WSN\\file-kwsn\\WSN-topology.kwsn";
	private final String IN_DENSE_PATH = "Input\\WSN\\dense-cluster\\";
	private final String IN_IMBALANCE_PATH = "Input\\WSN\\imbalanced-cluster\\";
	private final String OUT_DENSE_PATH = "Output\\WSN\\dense-cluster\\";
	private final String OUT_IMBALANCE_PATH = "Output\\WSN\\imbalanced-cluster\\";
	
	public void autoCreateNewCluster() {
		int index = 1;
		String fileName;
		File file;;
		
		// Auto Create for Dense Cluster
		while (true) {
			fileName = "Cluster" + index + ".kwsn";
			file = new File(IN_DENSE_PATH+fileName);
			if (file.exists()) {
				try {
					System.out.println("Processing: " + IN_DENSE_PATH + fileName);
					action(IN_DENSE_PATH , fileName);
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
		
		index = 1;
		
		// Auto Create for Imbalance Cluster
				while (true) {
					fileName = "Cluster" + index + ".kwsn";
					file = new File(IN_IMBALANCE_PATH+fileName);					
					if (file.exists()) {
						try {
							System.out.println("Processing: " + IN_IMBALANCE_PATH + fileName);
							action(IN_IMBALANCE_PATH , fileName);
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
	
	public void action(String path, String fileName) {
		initializeData(path, fileName);
		findSourceSink();
		findChannelForSourceSink();
		createWSNFile(path, fileName);
	}
	
	private void initializeData(String path, String fileName) {
		ReadXMLFile reader = new ReadXMLFile();
		
		wsn = reader.readFile(IN_WSN_PATH);
		cluster = reader.readFile(path + fileName);
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
		
		for (Sensor sensor : cluster.getSensors()) {
			sourceChannel.setSecondSensor(sensor);
			sinkChannel.setFirstSensor(sensor);
			
			break;
		}
		
		sourceChannel.setFirstSensor(source);
		sinkChannel.setSecondSensor(sink);
	}
	
	private void createWSNFile(String path, String fileName) {
		HashSet<Sensor> sensors = cluster.getSensors();
		sensors.add(source);
		sensors.add(sink);
		
		HashSet<Channel> channels = cluster.getChannels();
		channels.add(sourceChannel);
		channels.add(sinkChannel);
		
		cluster.setSensors(sensors);
		cluster.setChannels(channels);
		
		if (path.indexOf("dense") > 0) {
			WriteXMLFile.write(cluster, OUT_DENSE_PATH + fileName);
		} else {
			WriteXMLFile.write(cluster, OUT_IMBALANCE_PATH + fileName);
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
