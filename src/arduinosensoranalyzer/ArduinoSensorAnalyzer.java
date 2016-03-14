package arduinosensoranalyzer;

import ioexpander.IOExpander;
import ioexpander.Wire;
import gnu.io.PortInUseException;
import gnu.io.NoSuchPortException;
import gnu.io.UnsupportedCommOperationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialclient.SerialClient;
import serialclient.SerialClientException;

public class ArduinoSensorAnalyzer implements SensorParserListener {

    static {
        System.out.println("Loading libraries.");
        try {
            System.out.println("Loaded libraries.");
            System.loadLibrary("rxtxSerial");
            System.loadLibrary("wire");
        } catch (UnsatisfiedLinkError e) {
            String javaLibPath = System.getProperty("java.library.path");
            Map<String, String> envVars = System.getenv();
            System.out.println(e + "\n[paths: " + javaLibPath + "]");
            System.exit(1);
        }
    }

    private final IOExpander expander;
    private final FileOutputStream fileOutputStream;

    ArduinoSensorAnalyzer() throws FileNotFoundException, IOException {
        Wire wire = new Wire();
        wire.begin((byte) 0);
        expander = new IOExpander(wire);
        expander.begin((byte) 0);
        final File file = new File("/home/pi/samples.output");
        fileOutputStream = new FileOutputStream(file);
        FileChannel outChan = fileOutputStream.getChannel();
        outChan.truncate(0);
    }

    public static void main(String[] args) throws PortInUseException, NoSuchPortException, UnsupportedCommOperationException, TooManyListenersException, IOException, SerialClientException, InterruptedException {
        final SerialClient serialClient = new SerialClient("/dev/ttyS99", 9600);
        serialClient.connect();
        final SensorParser parser = new SensorParser();
        final SensorReader reader = new SensorReader(serialClient, parser);
        final ArduinoSensorAnalyzer arduinoSensorAnalyzer = new ArduinoSensorAnalyzer();
        parser.addEventListener(arduinoSensorAnalyzer);
        int i = 0;
        while (true) {
            arduinoSensorAnalyzer.fileOutputStream.flush();
            System.out.println("collecting...");
            Thread.sleep(1000);
        }

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
        try {
            fileOutputStream.write(frame.serialize());
        } catch (IOException ex) {
            Logger.getLogger(ArduinoSensorAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
