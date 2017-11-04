package app;

import model.WSN;
import util.ReadXMLFile;
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		step1();
	}
	
	public static void step1() {
		ReadXMLFile readXMLFile = new ReadXMLFile();
		WSN original = readXMLFile.readFile("input\\WSN\\original.kwsn");
		WSN cluster = readXMLFile.readFile("input\\WSN\\cluster1.kwsn");
		WriteXMLFile.write(cluster, "output\\cluster.kwsn");
		WriteXMLFile.write(original, "output\\original.kwsn");
	}
}
