package arduinosensoranalyzer;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.TooManyListenersException;
import serialclient.SerialClient;
import serialclient.SerialClientException;

public class ArduinoSensorAnalyzer implements SensorParserListener {

    ArduinoSensorAnalyzer() {
    }
    
    public static void main(String[] args) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, TooManyListenersException, IOException, SerialClientException {
        final SerialClient serialClient = new SerialClient("/dev/ttyS99", 9600);
        serialClient.connect();
        final SensorParser parser = new SensorParser();
        final SensorReader reader = new SensorReader(serialClient, parser);
        parser.addEventListener(new ArduinoSensorAnalyzer());
    }

    @Override
    public void notifyFrameParsed(SensorFrame frame) {
        System.out.println(frame);
    }
}
