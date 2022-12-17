package DZ2;

import lombok.SneakyThrows;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class BuliderPacket implements Packet {

    private String iface;

    private int addCount;

    private byte[] ipDestinationBytes;
    private byte[] ipSourceBytes;

    private int totalLength;

    private byte[] platformDescBytes;

    private byte[] packet;
    private int dataLength;
    private int portToSend;

    public String parse(byte[] data){
        if (data.length < 14) return null;
        int offset = (iface.equals("\\Device\\NPF_Loopback") ? 4 /*local*/ : 14 /*ethernet*/) + 20 /*ipv4*/ + 8 /* udp */;

        byte[]dataByte = new byte[data.length-offset];
        System.arraycopy(data,offset,dataByte,0,dataByte.length);
        return new String(dataByte);
    }

    public BuliderPacket() {

        super();
    }

    @SneakyThrows
    @Override
    public Packet addHeader(String data, int portToSend) {
        this.ipSourceBytes = InetAddress.getByName("127.0.0.1").getAddress();
        this.ipDestinationBytes = InetAddress.getByName("255.255.255.255").getAddress();
        this.addCount = 0;
        this.platformDescBytes = data.getBytes();
        this.dataLength = platformDescBytes.length;
        this.totalLength = dataLength + 28;
        this.iface = "\\Device\\NPF_Loopback";
        this.portToSend = portToSend;
        this.packet =new byte[totalLength + (iface.equals("\\Device\\NPF_Loopback") ? 4 : 14)];
        return this;
    }

    @Override
    public Packet addUdpPart() {
        for (int i = 0, j = 7; i < 1; i++, j++) packet[i] = longToBytes(0x02)[j];

        //Header Length = 20 bytes
        for (int i = 4 + addCount, j = 7; i < 5 + addCount; i++, j++) packet[i] = longToBytes(69)[j];
        //Differentiated Services Field
        for (int i = 5 + addCount, j = 7; i < 6 + addCount; i++, j++) packet[i] = longToBytes(0x00)[j];
        //Total Length
        for (int i = 6 + addCount, j = 6; i < 8 + addCount; i++, j++) packet[i] = longToBytes(totalLength)[j];
        //Identification - for fragmented packages
        for (int i = 8 + addCount, j = 6; i < 10 + addCount; i++, j++) packet[i] = longToBytes(33500)[j];
        //Flag, Fragment Offset - for fragmented packages
        for (int i = 10 + addCount, j = 6; i < 12 + addCount; i++, j++) packet[i] = longToBytes(0x00)[j];
        //Time to Live - max limit for moving through the network
        for (int i = 12 + addCount, j = 7; i < 13 + addCount; i++, j++) packet[i] = longToBytes(128)[j];
        //Protocol - UDP
        for (int i = 13 + addCount, j = 7; i < 14 + addCount; i++, j++) packet[i] = longToBytes(17)[j];
        //Header Checksum, can be 0x00 if it is not calculated
        for (int i = 14 + addCount, j = 6; i < 16 + addCount; i++, j++) packet[i] = longToBytes(0x00)[j];
        return this;
    }

    @Override
    public Packet addPayload(int portToSend) {
        for (int i = 16 + addCount, j = 0; i < 20 + addCount; i++, j++) packet[i] = ipSourceBytes[j];
        for (int i = 20 + addCount, j = 0; i < 24 + addCount; i++, j++) packet[i] = ipDestinationBytes[j];
        //Source port
        for (int i = 24 + addCount, j = 6; i < 26 + addCount; i++, j++) packet[i] = longToBytes(portToSend)[j];
        //Destination port
        for (int i = 26 + addCount, j = 6; i < 28 + addCount; i++, j++) packet[i] = longToBytes(portToSend)[j];
        //Length
        int length = totalLength - 20;
        for (int i = 28 + addCount, j = 6; i < 30 + addCount; i++, j++) packet[i] = longToBytes(length)[j];
        //Checksum, can be 0x00 if it is not calculated
        for (int i = 30 + addCount, j = 6; i < 32 + addCount; i++, j++) packet[i] = longToBytes(0x0000)[j];
        //Data
        System.arraycopy(platformDescBytes, 0, packet, 32 + addCount, platformDescBytes.length);
        return this;
    }

    @Override
    public byte[] bulid() {
        return packet;
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}
