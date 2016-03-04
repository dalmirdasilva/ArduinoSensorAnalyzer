package arduinosensoranalyzer;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialclient.SerialClient;
import serialclient.SerialClientException;

public class SensorReader implements SerialPortEventListener {

    private final SerialClient client;
    private final SensorParser parser;

    public SensorReader(final SerialClient client, final SensorParser parser) {
        this.client = client;
        this.parser = parser;
        try {
            client.addSerialPortEventListener(this);
        } catch (TooManyListenersException | SerialClientException ex) {
            Logger.getLogger(SensorReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void serialEvent(final SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                processSensorData();
                break;
        }
    }

    private void processSensorData() {
        InputStream inputStream = null;
        try {
            inputStream = client.getInputStream();
            while (inputStream.available() > 0) {
                byte[] buffer = new byte[1024];
                int read = inputStream.read(buffer);
                byte[] buf = Arrays.copyOfRange(buffer, 0, read);
                parseSensorData(buf);
            }
        } catch (IOException | SerialClientException ex) {
            Logger.getLogger(SensorReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    private void parseSensorData(byte[] buf) {
        int parsed = parser.parse(buf);
        if (parsed < buf.length) {
            Logger.getLogger(SensorReader.class.getName()).log(Level.INFO, "Parsed less bytes than buffer contains.");
        }
    }
}
