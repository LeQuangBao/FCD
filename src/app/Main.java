package app;

import model.WSN;
import util.ReadXMLFile;
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		ReadXMLFile.readFile();
		WSN wsn = ReadXMLFile.getWsn();
		WriteXMLFile.write(wsn);
	}
}
