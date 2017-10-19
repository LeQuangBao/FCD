package model;

public class Sensor {

	String id;
	String name;
	boolean init;
	int sType;
	int maxSendingRate;
	int maxProcessingRate;
	float x;
	float y;
	float width;
	float labelX;
	float labelY;
	float labelWidth;

	public String toString() {
		return "";
	}
	
	

	public Sensor(String id, String name, boolean init, int sType, int maxSendingRate, int maxProcessingRate) {
		super();
		this.id = id;
		this.name = name;
		this.init = init;
		this.sType = sType;
		this.maxSendingRate = maxSendingRate;
		this.maxProcessingRate = maxProcessingRate;
	}



	public Sensor() {
		super();
	}

	public Sensor(String id, String name, boolean init, int sType, int maxSendingRate, int maxProcessingRate, float x,
			float y, float width, float labelX, float labelY, float labelWidth) {
		super();
		this.id = id;
		this.name = name;
		this.init = init;
		this.sType = sType;
		this.maxSendingRate = maxSendingRate;
		this.maxProcessingRate = maxProcessingRate;
		this.x = x;
		this.y = y;
		this.width = width;
		this.labelX = labelX;
		this.labelY = labelY;
		this.labelWidth = labelWidth;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLabelX() {
		return labelX;
	}

	public void setLabelX(float labelX) {
		this.labelX = labelX;
	}

	public float getLabelY() {
		return labelY;
	}

	public void setLabelY(float labelY) {
		this.labelY = labelY;
	}

	public float getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(float labelWidth) {
		this.labelWidth = labelWidth;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public int getsType() {
		return sType;
	}

	public void setsType(int sType) {
		this.sType = sType;
	}

	public int getMaxSendingRate() {
		return maxSendingRate;
	}

	public void setMaxSendingRate(int maxSendingRate) {
		this.maxSendingRate = maxSendingRate;
	}

	public int getMaxProcessingRate() {
		return maxProcessingRate;
	}

	public void setMaxProcessingRate(int maxProcessingRate) {
		this.maxProcessingRate = maxProcessingRate;
	}

}
