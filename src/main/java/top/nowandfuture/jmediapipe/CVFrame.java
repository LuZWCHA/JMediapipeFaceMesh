package top.nowandfuture.jmediapipe;

public class CVFrame {
    private final byte[] data;
    private final int width;
    private final int height;

    public CVFrame(byte[] data, int[] size){
        assert size.length == 2;
        this.data = data;
        this.width = size[1];
        this.height = size[0];
    }

    public byte[] getData() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
