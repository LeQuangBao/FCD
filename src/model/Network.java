package model;

public class Network {
	String id;
	int numberOfSensors;
	int numberOfPackets;
	int sensorMaxBufferSize;
	int sensorMaxQueueSize;
	int channelMaxBufferSize;

	public Network() {
	}

	public Network(String id, int numberOfSensors, int numberOfPackets, int sensorMaxBufferSize, int sensorMaxQueueSize,
			int channelMaxBufferSize) {
		this.id = id;
		this.numberOfSensors = numberOfSensors;
		this.numberOfPackets = numberOfPackets;
		this.sensorMaxBufferSize = sensorMaxBufferSize;
		this.sensorMaxQueueSize = sensorMaxQueueSize;
		this.channelMaxBufferSize = channelMaxBufferSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumberOfSensors() {
		return numberOfSensors;
	}

	public void setNumberOfSensors(int numberOfSensors) {
		this.numberOfSensors = numberOfSensors;
	}

	public int getNumberOfPackets() {
		return numberOfPackets;
	}

	public void setNumberOfPackets(int numberOfPackets) {
		this.numberOfPackets = numberOfPackets;
	}

	public int getSensorMaxBufferSize() {
		return sensorMaxBufferSize;
	}

	public void setSensorMaxBufferSize(int sensorMaxBufferSize) {
		this.sensorMaxBufferSize = sensorMaxBufferSize;
	}

	public int getSensorMaxQueueSize() {
		return sensorMaxQueueSize;
	}

	public void setSensorMaxQueueSize(int sensorMaxQueueSize) {
		this.sensorMaxQueueSize = sensorMaxQueueSize;
	}

	public int getChannelMaxBufferSize() {
		return channelMaxBufferSize;
	}

	public void setChannelMaxBufferSize(int channelMaxBufferSize) {
		this.channelMaxBufferSize = channelMaxBufferSize;
	}
}
