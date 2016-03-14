package arduinosensoranalyzer;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SensorDataParser {

    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        FileOutputStream fos = new FileOutputStream("/tmp/features_test.csv");
        fos.write(Feature.getCSVHeader().getBytes());
        SensorDataParser parser = new SensorDataParser();
        List<Feature> features = FeatureExtractor.extractFeatures(parser.parseObjects(), 20, 5);
        for (Feature feature : features) {
            fos.write(feature.toCSV().getBytes());
        }
        fos.close();
        System.out.println("features.size: " + features.size());
    }

    List<SensorFrame> parseObjects() throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("/home/dalmir/Desktop/samples_test.output");
        DataInputStream dis = new DataInputStream(fis);
        List<SensorFrame> frames = new ArrayList<>();
        while (dis.available() > 0) {
            if (dis.readByte() != (byte) 0xaa) {
                System.out.println("Initial mark mismatch.");
                break;
            }
            SensorFrame frame = new SensorFrame();
            frame.setX(dis.readShort());
            frame.setY(dis.readShort());
            frame.setZ(dis.readShort());
            frame.setFlags(dis.readByte());
            frame.setTime(dis.readInt());
            if (dis.readByte() != (byte) 0xbb) {
                System.out.println("Final mark mismatch.");
                break;
            }
            frames.add(frame);
        }
        System.out.println(frames.size());
        return frames;
    }
}
