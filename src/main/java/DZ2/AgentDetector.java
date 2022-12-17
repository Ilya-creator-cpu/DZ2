package DZ2;
import jade.core.AID;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapNativeException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import org.pcap4j.packet.Packet;
import DZ2.Listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j

public class AgentDetector {
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
    private String interfaceName;

    private long timeToDie = 1400;
   @Setter
    private PcapHelper pcapHelper;
   @Setter
   private Map<AID,AgentMessage> hashmap = new HashMap<>();

   private List<Listener> listeners = new ArrayList<>();
   @Setter
   private Parser parser;

   private Listener l;

    private byte[] data;
    private String message;
    private boolean findAgent = false;
    private Packet packet;
    boolean published;
@Setter
    AID agent;


    private List<AID> agents = new ArrayList<AID>();


    private int port;

    private ScheduledFuture discoveringTask, publishingTask;

    public  AgentDetector(String interfaceName, int t, AID agent) throws PcapNativeException, UnknownHostException {
        this.interfaceName = interfaceName;
        port = t;
        pcapHelper = new PcapHelper(interfaceName,t);
        parser = new Parser(interfaceName);
        this.agent = agent;


    }
@SneakyThrows
    public void startDiscovering() {
        getPacket Packet = new getPacket();
        if (discoveringTask == null) {
            discoveringTask = pcapHelper.startPacketsCapturing(port,Packet
            ,ses);
        }
    }

    public void sendPacket() throws IOException, PcapNativeException {
        data = buliderPacket();
        if (publishingTask == null) {
            publishingTask = ses.scheduleWithFixedDelay(() -> pcapHelper.sendPacket(data), 0, 50, TimeUnit.MILLISECONDS);
        }
    }


    public void deadAgentRemoving() {

        List<AgentMessage> agentMessages = new CopyOnWriteArrayList<AgentMessage>(hashmap.values());
        //  log.info(agentMessages.toString());
        long T = System.currentTimeMillis();
        for (AgentMessage agentMessage : agentMessages) {
            if (T - agentMessage.getMessageTime() > timeToDie) {
                // log.info("Агент умер," + " " + agentMessage.getAgentName() +
                //  " время смерти:" + " " + Long.toString(T - agentMessage.getMessageTime()));
                agentMessages.remove(agentMessage);
                AID agentToRemove = new AID(agentMessage.getAgentName(), true);
                if (agents.contains(agentToRemove)) {
                    agents.remove(agentToRemove);
                    informListeners("Agent was removed" + agentToRemove.getName());
                }
            }

                else {
                    AID agentToPut = new AID(agentMessage.getAgentName(), true);
                    if (!agents.contains(agentToPut)) {
                        agents.add(agentToPut);
                        informListeners("Agent was added" + agentToPut.getName());

                    }
                }

                assert agents != null;

            }
        }


    public List<AID> getActiveAgents(){

        deadAgentRemoving();
        agents.remove(agent);//удаляем самого детектора

        return agents;
    }

    public void subscribeOnChange(Listener listener) {

        listeners.add(listener);

    }

    public void informListeners(String message) {
        for (Listener listener:listeners) {
            listener.informListeners();
        }
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }




    public void stopSending() {
            publishingTask.cancel(true);
            publishingTask = null;

            log.info("Stop send" + agent.getName());
    }

    private class getPacket implements PacketListener {

        @Override
        @SneakyThrows
        public void gotPacket(Packet packet) {
            if (packet != null) {

                String str = fromJson(parser.parse(packet.getRawData()));
                AgentMessage agentMessage = new AgentMessage(fromJson(parser.parse(packet.getRawData())), System.currentTimeMillis());

                AID receivedAID = new AID(agentMessage.getAgentName(), true);
                hashmap.put(receivedAID, agentMessage);
                deadAgentRemoving();

            }
        }

    }

    public String toJson(AID message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(message);
    }
    @SneakyThrows

    public byte[] buliderPacket() {
        return new BuliderPacket()
                .addHeader(toJson(agent),port)
                .addUdpPart()
                .addPayload(port)
                .bulid();

    }

    public String fromJson(String str) throws JsonProcessingException {
         String[] strings = str.split("\"");
        return strings[3];
    }


    }




