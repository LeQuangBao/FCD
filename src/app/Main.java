package app;

import model.WSN;
import util.CreateNewCluster;
import util.DataProvider;
import util.WriteXMLFile;

public class Main {
	public static void main(String[] args) {
		DataProvider.readFile();
		WSN wsn = DataProvider.getWsn();
		WriteXMLFile.write(wsn);
	}
}
