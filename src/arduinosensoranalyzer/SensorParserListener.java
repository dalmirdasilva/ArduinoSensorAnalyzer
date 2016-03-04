package arduinosensoranalyzer;

public interface SensorParserListener {
    
    void notifyFrameParsed(SensorFrame frame);
}
