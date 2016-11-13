package arduinosensoranalyzer;

import ioexpander.IOExpander;
import ioexpander.Wire;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialclient.SerialClient;
import serialclient.SerialClientException;

public class ArduinoSensorAnalyzer implements SensorParserListener {

    final private IOExpander expander;
    
    ArduinoSensorAnalyzer() {
        Wire wire = new Wire();
        wire.begin((byte) 0);
        expander = new IOExpander(wire);
        expander.begin((byte) 0);
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
        byte flags = frame.getFlags();
        try {
            if (expander.digitalRead(IOExpander.Pin.PIN_A1)) {
                flags |= 0x40;
            }
            frame.setFlags(flags);
        } catch (IOException ex) {
            Logger.getLogger(ArduinoSensorAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(frame);
    }
}
