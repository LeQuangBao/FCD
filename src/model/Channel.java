package model;

import java.util.concurrent.ThreadLocalRandom;

public class Channel {

	String id;
	String lType;
	String cType;
	float proabilityPathCongestion;
	int maxSendingRate;
	int maxBufferSize;
	Sensor firstSensor;
	Sensor secondSensor;

	public Channel() {
		super();
	}

	public Channel(String id, String lType, String cType, float proabilityPathCongestion, int maxSendingRate,
			Sensor firstSensor, Sensor secondSensor) {
		super();
		this.id = id;
		this.lType = lType;
		this.cType = cType;
		this.proabilityPathCongestion = proabilityPathCongestion;
		this.maxSendingRate = maxSendingRate;
		this.firstSensor = firstSensor;
		this.secondSensor = secondSensor;
		this.maxBufferSize = ThreadLocalRandom.current().nextInt(1, 7);
	}

	public float getDelay() {
		float f = 0.0f;
		f = (float) 1 / firstSensor.getMaxProcessingRate();
		f += (float) 1 / firstSensor.getMaxSendingRate();
		f += (float) 1 / maxSendingRate;
		return f;
	}

	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getlType() {
		return lType;
	}

	public void setlType(String lType) {
		this.lType = lType;
	}

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public float getProabilityPathCongestion() {
		return proabilityPathCongestion;
	}

	public void setProabilityPathCongestion(float proabilityPathCongestion) {
		this.proabilityPathCongestion = proabilityPathCongestion;
	}

	public int getMaxSendingRate() {
		return maxSendingRate;
	}

	public void setMaxSendingRate(int maxSendingRate) {
		this.maxSendingRate = maxSendingRate;
	}

	public Sensor getFirstSensor() {
		return firstSensor;
	}

	public void setFirstSensor(Sensor firstSensor) {
		this.firstSensor = firstSensor;
	}

	public Sensor getSecondSensor() {
		return secondSensor;
	}

	public void setSecondSensor(Sensor secondSensor) {
		this.secondSensor = secondSensor;
	}

}
