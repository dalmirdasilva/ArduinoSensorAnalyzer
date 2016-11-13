package arduinosensoranalyzer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorParser {

    private static final int INITIAL = 0;
    private static final int SOF_PARSED = 1;
    private static final int X_PARSED = 2;
    private static final int Y_PARSED = 3;
    private static final int Z_PARSED = 4;
    private static final int FLAGS_PARSED = 5;
    private static final int TIME_PARSED = 6;
    private static final int EOF_PARSED = 7;

    private int state;
    private int pos;
    private byte[] buffer;
    private int bufferPointer;
    private final List<SensorParserListener> listeners;

    public SensorParser() {
        listeners = new ArrayList<>();
        state = INITIAL;
        pos = 0;
    }

    public void addEventListener(SensorParserListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(SensorFrame frame) {
        for (SensorParserListener listener : listeners) {
            listener.notifyFrameParsed(frame);
        }
    }

    public int parse(byte[] buffer) {
        int i;
        for (i = 0; i < buffer.length; i++) {
            if (!parse(buffer[i])) {
                break;
            }
        }
        return i;
    }

    private boolean parse(byte b) {
        boolean parsed = true;
        switch (state) {
            case EOF_PARSED:
            case INITIAL:
                if (b == SensorFrame.START) {
                    state = SOF_PARSED;
                    buffer = new byte[SensorFrame.MAX_FRAME_SIZE];
                    bufferPointer = 0;
                } else {
                    parsed = false;
                }
                pos = 0;
                break;
            case SOF_PARSED:
                if (++pos >= SensorFrame.X_SIZE) {
                    state = X_PARSED;
                    pos = 0;
                }
                break;
            case X_PARSED:
                if (++pos >= SensorFrame.Y_SIZE) {
                    state = Y_PARSED;
                    pos = 0;
                }
                break;
            case Y_PARSED:
                if (++pos >= SensorFrame.Z_SIZE) {
                    state = Z_PARSED;
                    pos = 0;
                }
                break;
            case Z_PARSED:
                state = FLAGS_PARSED;
                pos = 0;
                break;
            case FLAGS_PARSED:
                if (++pos >= SensorFrame.TIME_SIZE) {
                    state = TIME_PARSED;
                    pos = 0;
                }
                break;
            case TIME_PARSED:
                if (b == SensorFrame.END) {
                    state = EOF_PARSED;
                    notifyFrameParsed();
                } else {
                    parsed = false;
                }
                break;
        }
        if (parsed) {
            buffer[bufferPointer++] = b;
        }
        return parsed;
    }

    private void notifyFrameParsed() {
        SensorFrame frame = null;
        try {
            frame = makeFrame();
        } catch (IOException ex) {
            Logger.getLogger(SensorParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyListeners(frame);
    }

    private SensorFrame makeFrame() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));
        SensorFrame frame = new SensorFrame();
        dis.readByte();
        frame.setX((int) dis.readShort());
        frame.setY((int) dis.readShort());
        frame.setZ((int) dis.readShort());
        frame.setFlags(dis.readByte());
        int t = dis.readInt();
        long time = 0x00000000ffffffff & Integer.reverseBytes(t);
        frame.setTime(time);
        return frame;
    }
}
