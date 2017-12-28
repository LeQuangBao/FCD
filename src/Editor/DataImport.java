package Editor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import Constants.TopologyConstants;
import Converter.BroadcastConverter;
import WSN.*;
import Editor.*;


public class DataImport {

	/**
	 * 
	 * @param filePath
	 * @param editor
	 */
	private Board board;
	public void Import (String filePath) 
	{	
		String folderPath = "temp\\";
		try {
			HashMap<String,Object> topologyData = KwsnFileReader.getInstance().readKwsn(filePath);
			InitializeData data = new InitializeData();
			data.setMinSensorSendingRate((String)topologyData.get(TopologyConstants.SENSORS_MIN_SENDING_RATE_KEY));
			data.setMinSensorProcessingRate((String)topologyData.get(TopologyConstants.SENSORS_MIN_PROCESSING_RATE_KEY));
			data.setMinChannelSendingRate((String)topologyData.get(TopologyConstants.CHANNEL_MIN_SENDING_RATE_KEY));
			data.setMaxSensorSendingRate((String)topologyData.get(TopologyConstants.SENSORS_MAX_SENDING_RATE_KEY));
			data.setMaxSensorSendingRate((String)topologyData.get(TopologyConstants.SENSORS_MAX_PROCESSING_RATE_KEY));
			data.setMaxChannelSendingRate((String)topologyData.get(TopologyConstants.CHANNEL_MAX_SENDING_RATE_KEY));
			data.setSensorMaxBufferSize((String)topologyData.get(TopologyConstants.SENSORS_MAX_BUFFER_SIZE_KEY));
			data.setSensorMaxQueueSize((String)topologyData.get(TopologyConstants.SENSORS_MAX_QUEUE_SIZE_KEY));
			data.setChannelMaxBufferSize((String)topologyData.get(TopologyConstants.CHANEL_MAX_BUFFER_SIZE_KEY));
			data.setNumberOfPackage((String)topologyData.get(TopologyConstants.NUMBER_OF_PACKAGE));
			
			
			List<WSN.Sensor> sensors =(List<WSN.Sensor>) topologyData.get(TopologyConstants.SENSORS_LIST_KEY);
			List<WSN.Link> links = (List<WSN.Link>) topologyData.get(TopologyConstants.CHANNEL_LIST_KEY);
			
			HashMap<String,List<Clip>> map = KwsnDataConvert.GetData(sensors, links);
			List<Clip> sensorClips = map.get(KwsnDataConvert.SENSOR_KEY);
			List<Clip> channelClips = map.get(KwsnDataConvert.CHANNEL_KEY);
			
			board = new Board();
			
			for(Clip c : sensorClips) {
				board.addClip(c);
			}
			
			for(Clip c : channelClips) {
				board.addClip(c);
			}
			
			//editor.updateBoard();
			
			//InitializeData data = new InitializeData();
			data.setSensorClip(board.getSensorClip());
			data.setChannelClip(board.getChannelClip());
			data.setOriginSensorClip(board.getPlaceClip());
			data.setOriginChannelClip(board.getChannelClip());
			
			BroadcastConverter converter = new BroadcastConverter(data.getTopologyData());
			converter.Convert(folderPath);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static boolean IsExtension (String filePath , String expectedExrension) {
		String[] pathArray = filePath.split("\\.");
		return pathArray[pathArray.length - 1].equals(expectedExrension);
	}
}
