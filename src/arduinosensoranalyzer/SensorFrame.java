package arduinosensoranalyzer;

public class SensorFrame {

    static final byte START = (byte) 0xaa;
    static final byte END = (byte) 0xbb;
   
    static final int MAX_FRAME_SIZE = 1 + 2 + 2 + 2 + 1 + 4 + 1;
    static final int X_SIZE = 2;
    static final int Y_SIZE = 2;
    static final int Z_SIZE = 2;
    static final int TIME_SIZE = 4;
    
    private int x;
    private int y;
    private int z;
    private byte flags;
    private long time;

    public SensorFrame() {
    }

    public SensorFrame(int x, int y, int z, byte flags, long time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.flags = flags;
        this.time = time;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SensorFrame{" + "x=" + x + ", y=" + y + ", z=" + z + ", flags=" + flags + ", time=" + time + '}';
    }
}
