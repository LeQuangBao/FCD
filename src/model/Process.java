package model;

public class Process {
	String name;
	String parameter;
	int zoom;
	int stateCounter;

	public Process() {
	}

	public Process(String name, String parameter, int zoom, int stateCounter) {
		this.name = name;
		this.parameter = parameter;
		this.zoom = zoom;
		this.stateCounter = stateCounter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public int getstateCounter() {
		return stateCounter;
	}

	public void setstateCounter(int stateCounter) {
		this.stateCounter = stateCounter;
	}

}
