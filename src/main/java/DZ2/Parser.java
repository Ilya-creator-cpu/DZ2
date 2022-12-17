package DZ2;

public class Parser {

    private String iface = "\\Device\\NPF_Loopback";

    public Parser(String interfaceName) {
        this.iface = interfaceName;
    }

    /**
     * parses raw packet data into string value of payload
     * @param data - all data from UDP packet
     * @return string value of payload
     */
    public String parse(byte[] data){
        if (data.length < 14) return null;
        int offset = (iface.equals("\\Device\\NPF_Loopback") ? 4 /*local*/ : 14 /*ethernet*/) + 20 /*ipv4*/ + 8 /* udp */;

        byte[]dataByte = new byte[data.length-offset];
        System.arraycopy(data,offset,dataByte,0,dataByte.length);
        return new String(dataByte);
    }
}
