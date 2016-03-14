package arduinosensoranalyzer;

public class Feature {

    public int xMin;
    public int yMin;
    public int zMin;

    public int xMax;
    public int yMax;
    public int zMax;

    public int xAvg;
    public int yAvg;
    public int zAvg;

    public int xStandardDeviation;
    public int yStandardDeviation;
    public int zStandardDeviation;

    public boolean klass;

    public Feature() {
    }

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public int getzMin() {
        return zMin;
    }

    public void setzMin(int zMin) {
        this.zMin = zMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    public int getzMax() {
        return zMax;
    }

    public void setzMax(int zMax) {
        this.zMax = zMax;
    }

    public int getxAvg() {
        return xAvg;
    }

    public void setxAvg(int xAvg) {
        this.xAvg = xAvg;
    }

    public int getyAvg() {
        return yAvg;
    }

    public void setyAvg(int yAvg) {
        this.yAvg = yAvg;
    }

    public int getzAvg() {
        return zAvg;
    }

    public void setzAvg(int zAvg) {
        this.zAvg = zAvg;
    }

    public int getxStandardDeviation() {
        return xStandardDeviation;
    }

    public void setxStandardDeviation(int xStandardDeviation) {
        this.xStandardDeviation = xStandardDeviation;
    }

    public int getyStandardDeviations() {
        return yStandardDeviation;
    }

    public void setyStandardDeviations(int yStandardDeviations) {
        this.yStandardDeviation = yStandardDeviations;
    }

    public int getzStandardDeviations() {
        return zStandardDeviation;
    }

    public void setzStandardDeviations(int zStandardDeviations) {
        this.zStandardDeviation = zStandardDeviations;
    }

    public boolean isKlass() {
        return klass;
    }

    public void setKlass(boolean klass) {
        this.klass = klass;
    }
    
    
    public static String getCSVHeader() {
        return "X_MIN, Y_MIN, Z_MIN, X_MAX, Y_MAX, Z_MAX, X_AVG, Y_AVG, Z_AVG, X_SD, Y_SD, Z_SD, CLASS\n";
    }
    
    public String toCSV() {
        return "" + xMin + ", " + yMin + ", " + zMin + ", " + xMax + ", " + yMax + ", " + zMax + ", " + xAvg + ", " + yAvg + ", " + zAvg + ", " + xStandardDeviation + ", " + yStandardDeviation + ", " + zStandardDeviation + ", " + klass + "\n";
    }

    @Override
    public String toString() {
        return "Feature{" + "xMin=" + xMin + ", yMin=" + yMin + ", zMin=" + zMin + ", xMax=" + xMax + ", yMax=" + yMax + ", zMax=" + zMax + ", xAvg=" + xAvg + ", yAvg=" + yAvg + ", zAvg=" + zAvg + ", xStandardDeviation=" + xStandardDeviation + ", yStandardDeviation=" + yStandardDeviation + ", zStandardDeviation=" + zStandardDeviation + ", klass=" + klass + '}';
    }
}
