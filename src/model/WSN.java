package model;

import java.util.HashSet;

public class WSN {

	private Network network;
	private Process process;
	private HashSet<Sensor> sensors;
	private HashSet<Channel> channels;
	

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public WSN() {
	}

	public WSN(Network network, Process process, HashSet<Sensor> sensors, HashSet<Channel> channels) {
		super();
		this.network = network;
		this.process = process;
		this.sensors = sensors;
		this.channels = channels;
	}

	public WSN(HashSet<Sensor> sensors, HashSet<Channel> channels) {
		super();
		this.sensors = sensors;
		this.channels = channels;
	}

	public HashSet<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(HashSet<Sensor> sensors) {
		this.sensors = sensors;
	}

	public HashSet<Channel> getChannels() {
		return channels;
	}

	public void setChannels(HashSet<Channel> channels) {
		this.channels = channels;
	}

	/**
	 * Check if this WSN has that sensor
	 * @param sensor to be checked
	 * @return true if that sensor is in this WSN
	 */
	public boolean hasSensor(Sensor sensor) {
		for (Sensor s : sensors) {
			if (s.is(sensor)) {
				return true;
			}
		}
		return false;
	}
}
