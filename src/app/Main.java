package app;

import java.util.HashSet;

import model.Sensor;
import model.WSN;
import util.NetworkHandler;
import util.ReadXMLFile;
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		step1();
	}
	
	public static void step1() {
		ReadXMLFile readXMLFile = new ReadXMLFile();
		WSN original = readXMLFile.readFile("input\\WSN\\original4.kwsn");
		WSN cluster = readXMLFile.readFile("input\\WSN\\cluster4.kwsn");
		original.formConnection();
		cluster.formConnection();
		
		Sensor mainSensor = NetworkHandler.gather(cluster.getSensors());
		HashSet<Sensor> adjacentNode = NetworkHandler.getAdjacentNode(cluster, original);
		
		
		
		
		WriteXMLFile.write(cluster, "output\\cluster4.kwsn");
		WriteXMLFile.write(original, "output\\original4.kwsn");
	}
}
