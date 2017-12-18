package util;

import java.io.File;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import KWNS.Channel;
import KWNS.Network;
import KWNS.Sensor;
import KWNS.WSN;

public class WriteXMLFile {

	public static void write(WSN wsn, String path) {
		HashSet<Sensor> sensors = wsn.getSensors();
		HashSet<Channel> channels = wsn.getChannels();
		Network network = wsn.getNetwork();
		KWNS.Process process = wsn.getProcess();
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("WSN");
			doc.appendChild(rootElement);

			// Network
			Element eNetwork = doc.createElement("Network");
			rootElement.appendChild(eNetwork);

			
			Attr attr = doc.createAttribute("ID");
			Attr attr1 = doc.createAttribute("NumberOfSensors");
			Attr attr2 = doc.createAttribute("NumberOfPackets");
			Attr attr3 = doc.createAttribute("SensorMaxBufferSize");
			Attr attr4 = doc.createAttribute("SensorMaxQueueSize");
			Attr attr5 = doc.createAttribute("ChannelMaxBufferSize");
			
			attr.setValue(network.getId());			
			attr1.setValue(Integer.toString(network.getNumberOfSensors()));
			attr2.setValue(Integer.toString(network.getNumberOfPackets()));
			attr3.setValue(Integer.toString(network.getSensorMaxBufferSize()));
			attr4.setValue(Integer.toString(network.getSensorMaxQueueSize()));
			attr5.setValue(Integer.toString(network.getChannelMaxBufferSize()));
			eNetwork.setAttributeNode(attr);
			eNetwork.setAttributeNode(attr1);
			eNetwork.setAttributeNode(attr2);
			eNetwork.setAttributeNode(attr3);
			eNetwork.setAttributeNode(attr4);
			eNetwork.setAttributeNode(attr5);
			
			// Process
			Element eProcess = doc.createElement("Process");
			eNetwork.appendChild(eProcess);
			
			attr = doc.createAttribute("Name");
			attr1 = doc.createAttribute("Parameter");
			attr2 = doc.createAttribute("Zoom");
			attr3 = doc.createAttribute("StateCounter");
			attr.setValue(process.getName());			
			attr1.setValue(process.getParameter());
			attr2.setValue(Integer.toString(process.getZoom()));
			attr3.setValue(Integer.toString(process.getstateCounter()));
			eProcess.setAttributeNode(attr);
			eProcess.setAttributeNode(attr1);
			eProcess.setAttributeNode(attr2);
			eProcess.setAttributeNode(attr3);

			Element eSensors = doc.createElement("Sensors");
			eProcess.appendChild(eSensors);
			
			Element eChannels = doc.createElement("Links");
			eProcess.appendChild(eChannels);
			
			for (Sensor s : sensors) {
				Element eSensor = doc.createElement("Sensor");
				attr = doc.createAttribute("Name");
				attr1 = doc.createAttribute("Init");
				attr2 = doc.createAttribute("SType");
				attr3 = doc.createAttribute("id");
				attr4 = doc.createAttribute("MaxSendingRate");
				attr5 = doc.createAttribute("MaxProcessingRate");
				attr.setValue(s.getName());			
				attr1.setValue(Boolean.toString(s.isInit()));
				attr2.setValue(Integer.toString(s.getsType()));
				attr3.setValue(s.getId());
				attr4.setValue(Integer.toString(s.getMaxSendingRate()));
				attr5.setValue(Integer.toString(s.getMaxProcessingRate()));
				eSensor.setAttributeNode(attr);
				eSensor.setAttributeNode(attr1);
				eSensor.setAttributeNode(attr2);
				eSensor.setAttributeNode(attr3);
				eSensor.setAttributeNode(attr4);
				eSensor.setAttributeNode(attr5);
				eSensors.appendChild(eSensor);
				
				Element ePosition = doc.createElement("Position");
				attr = doc.createAttribute("X");
				attr1 = doc.createAttribute("Y");
				attr2 = doc.createAttribute("Width");
				attr.setValue(Float.toString(s.getX()));			
				attr1.setValue(Float.toString(s.getY()));			
				attr2.setValue(Float.toString(s.getWidth()));
				ePosition.setAttributeNode(attr);
				ePosition.setAttributeNode(attr1);
				ePosition.setAttributeNode(attr2);
				eSensor.appendChild(ePosition);
			}
			for (Channel c : channels) {
				Element eChannel = doc.createElement("Link");
				attr = doc.createAttribute("LType");
				attr1 = doc.createAttribute("CType");
				attr2 = doc.createAttribute("MaxSendingRate");
				attr4 = doc.createAttribute("ProbabilityPathCongestion");
				attr5 = doc.createAttribute("id");
				attr.setValue(c.getlType());			
				attr1.setValue(c.getcType());
				attr2.setValue(Integer.toString(c.getMaxSendingRate()));
				attr4.setValue(Float.toString(c.getProabilityPathCongestion()));
				attr5.setValue(c.getId());
				eChannel.setAttributeNode(attr);
				eChannel.setAttributeNode(attr1);
				eChannel.setAttributeNode(attr2);
				eChannel.setAttributeNode(attr4);
				eChannel.setAttributeNode(attr5);
				eChannels.appendChild(eChannel);
				
				Element eFrom = doc.createElement("From");
				Element eTo = doc.createElement("To");
				eFrom.appendChild(doc.createTextNode(c.getFirstSensor().getName()));
				eTo.appendChild(doc.createTextNode(c.getSecondSensor().getName()));
				eChannel.appendChild(eFrom);
				eChannel.appendChild(eTo);
			}


			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = null;
			if (path == "") {
				result = new StreamResult(new File("output\\file.kwsn"));
			} else {
				result = new StreamResult(new File(path));
			}

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("Writing: [" + path + "] completed");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}