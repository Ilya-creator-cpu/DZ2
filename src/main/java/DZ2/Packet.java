package DZ2;

public interface Packet {

    public Packet addHeader(String data, int portToSend);

    public Packet addUdpPart();

    public Packet addPayload(int portToSend);

    public  byte[] bulid();
}
