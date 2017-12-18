package Converter;

import Pnml.Pnml;
import WSN.Sensor;

public class SensorFactory {

    public BaseSensor getSensor (Pnml pnml, Sensor sensor) {
        switch (sensor.Type) {
            case 1:
                return new SourceSensor(pnml,sensor);
            case 2:
                return new SinkSensor(pnml,sensor);
            case 3:
                return new IntermediateSensor(pnml,sensor);
            default:
                return null;
        }
    }
}

