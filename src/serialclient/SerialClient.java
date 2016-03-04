package serialclient;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class SerialClient {

    private final String portName;
    private final int speed;
    
    public SerialClient(final String portName, final int speed) {
        this.portName = portName;
        this.speed = speed;
    }

    private SerialPort serialPort;

    public void connect() throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException,
            TooManyListenersException,
            IOException,
            SerialClientException {

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

        if (portIdentifier.isCurrentlyOwned()) {
            throw new SerialClientException("Port is currently in use");
        } else {

            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {

                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.notifyOnDataAvailable(true);
            } else {
                throw new SerialClientException("Only serial ports are handled by this example.");
            }
        }
    }

    public void addSerialPortEventListener(final SerialPortEventListener serialPortEventListener) throws TooManyListenersException, SerialClientException {
        if (serialPort == null) {            
            throw new SerialClientException("Serial port is not ready"); 
        }
        this.serialPort.addEventListener(serialPortEventListener);
    }

    public InputStream getInputStream() throws IOException, SerialClientException {
        if (serialPort == null) {            
            throw new SerialClientException("Serial port is not ready"); 
        }
        return serialPort.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException, SerialClientException {
        if (serialPort == null) {            
            throw new SerialClientException("Serial port is not ready"); 
        }
        return serialPort.getOutputStream();
    }
}
