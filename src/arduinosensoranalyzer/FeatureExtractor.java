package arduinosensoranalyzer;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor {

    public static List<Feature> extractFeatures(List<SensorFrame> frames, int windowSize, int windowShift) {
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i + windowShift < frames.size(); i += windowShift) {
            try {
                List<SensorFrame> window = frames.subList(i, i + windowSize);
                features.add(extractFeature(window));
            } catch(IndexOutOfBoundsException e) {
                break;
            }
        } 
        return features;
    }
    
    public static Feature extractFeature(List<SensorFrame> frames) {
        boolean klass = false;
        Feature feature = new Feature();
        feature.xMin = feature.yMin = feature.zMin = Integer.MAX_VALUE;
        feature.xMax = feature.yMax = feature.zMax = Integer.MIN_VALUE;
        for (SensorFrame frame : frames) {
            feature.xAvg += frame.getX();
            feature.yAvg += frame.getY();
            feature.zAvg += frame.getZ();
            if (feature.xMax < frame.getX()) {
                feature.xMax = frame.getX();
            }
            if (feature.xMin > frame.getX()) {
                feature.xMin = frame.getX();
            }
            if (feature.yMax < frame.getY()) {
                feature.yMax = frame.getY();
            }
            if (feature.yMin > frame.getY()) {
                feature.yMin = frame.getY();
            }
            if (feature.zMax < frame.getZ()) {
                feature.zMax = frame.getZ();
            }
            if (feature.zMin > frame.getZ()) {
                feature.zMin = frame.getZ();
            }
            if (!klass && frame.getFlags() > 0) {
                klass = true;
            }
        }
        feature.xAvg /= frames.size();
        feature.yAvg /= frames.size();
        feature.zAvg /= frames.size();
        feature.klass = klass;
        computeStandardDeviation(feature, frames);
        return feature;
    }
    
    static void computeStandardDeviation(Feature feature, List<SensorFrame> frames) {
        for (SensorFrame frame : frames) {
            feature.xStandardDeviation += Math.pow(frame.getX() - feature.getxAvg(), 2);
            feature.yStandardDeviation += Math.pow(frame.getY() - feature.getyAvg(), 2);
            feature.zStandardDeviation += Math.pow(frame.getZ() - feature.getzAvg(), 2);
        }
        feature.xStandardDeviation /= frames.size();
        feature.yStandardDeviation /= frames.size();
        feature.zStandardDeviation /= frames.size();
    }
}
