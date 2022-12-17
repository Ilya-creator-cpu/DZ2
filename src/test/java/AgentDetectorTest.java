import DZ2.AgentDetector;
//import DZ2.Bulider;
import com.beust.ah.A;
import jade.core.AID;
import jade.domain.JADEAgentManagement.CreateAgent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.RepeatedTest;
import org.pcap4j.core.PcapNativeException;
import java.io.IOException;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
@Slf4j
public class AgentDetectorTest {
    AID agent1 = new AID("Agent1", true);
    AID agent2 = new AID("Agent2",true);
    AID agent3 = new AID("Agent3", true);
    AID agent4 = new AID("Agent4", true);

    @RepeatedTest(5)
    @SneakyThrows
    public void AgentDetectorTest1() {
        AgentDetector ad1 = new AgentDetector("\\Device\\NPF_Loopback",1250,agent1);
        AgentDetector ad2 = new AgentDetector("\\Device\\NPF_Loopback", 1250, agent2);
        ad1.sendPacket();
        ad1.startDiscovering();
        Thread.sleep(1000);
        ad2.sendPacket();
        ad2.startDiscovering();
        Thread.sleep(1000);
        ad1.stopSending();
        Thread.sleep(2000);
        ad2.stopSending();
        Thread.sleep(2000);
        Assertions.assertEquals(ad1.getActiveAgents().size(),0);
        Assertions.assertEquals(ad2.getActiveAgents().size(),0);
        ad2.sendPacket();
        Thread.sleep(1000);
        Assertions.assertEquals(ad1.getActiveAgents().size(),1);
        ad1.sendPacket();
        Thread.sleep(1000);
        Assertions.assertEquals(ad2.getActiveAgents().size(),1);
        ad1.stopSending();
        ad2.stopSending();
        Thread.sleep(2000);
    }

    @Test
    @SneakyThrows
    public void AgentDetectorTest2() {
        AgentDetector ad1 = new AgentDetector("\\Device\\NPF_Loopback", 1250, agent1);
         AgentDetector ad2 = new AgentDetector("\\Device\\NPF_Loopback",1250, agent2);
        AgentDetector ad3 = new AgentDetector("\\Device\\NPF_Loopback", 1250, agent3);
        AgentDetector ad4 = new AgentDetector("\\Device\\NPF_Loopback", 1250, agent4);
        ad1.sendPacket();
        ad1.startDiscovering();
        Thread.sleep(1000);
        ad2.sendPacket();
         ad2.startDiscovering();
        Thread.sleep(1000);
        ad3.sendPacket();
        ad3.startDiscovering();
        Thread.sleep(1000);
        ad4.sendPacket();
        ad4.startDiscovering();
        Thread.sleep(1000);
        Assertions.assertEquals(ad1.getActiveAgents().size(), 3);
         Assertions.assertEquals(ad2.getActiveAgents().size(),3);
        Assertions.assertEquals(ad3.getActiveAgents().size(), 3);
        Assertions.assertEquals(ad4.getActiveAgents().size(), 3);
        ad1.stopSending();
        ad2.stopSending();
        ad3.stopSending();
        ad4.stopSending();
        Thread.sleep(2000);
    }
    @Test
    @SneakyThrows
    public void AgentDetectorTest3() {

        AgentDetector ad1 = new AgentDetector("\\Device\\NPF_Loopback", 1250, agent1);
        AgentDetector ad3 = new AgentDetector("\\Device\\NPF_Loopback",1250, agent3);
        ad1.sendPacket();
        ad1.startDiscovering();
        Thread.sleep(1000);
        ad3.sendPacket();
        ad3.startDiscovering();
        Thread.sleep(1000);
        Assertions.assertEquals(1, ad1.getActiveAgents().size());
        Assertions.assertEquals(1,ad3.getActiveAgents().size());
        ad1.stopSending();
        ad3.stopSending();
        Thread.sleep(2000);

    }

    @Test
    @SneakyThrows
    public void AgentDetectorTest4() {
        AgentDetector ad1 = new AgentDetector("\\Device\\NPF_Loopback",1340,agent1);
        AgentDetector ad2 = new AgentDetector("\\Device\\NPF_Loopback", 1340, agent2);
        AgentDetector ad3 = new AgentDetector("\\Device\\NPF_Loopback", 1340, agent3);
        ad1.sendPacket();
        ad1.startDiscovering();
        Thread.sleep(1000);
        ad2.sendPacket();
        ad2.startDiscovering();
        Thread.sleep(1000);
        ad3.sendPacket();
        ad3.startDiscovering();
        Thread.sleep(1000);
        ad3.stopSending();
        Thread.sleep(2000);
        Assertions.assertEquals(ad1.getActiveAgents().size(),1);
        Assertions.assertEquals(ad2.getActiveAgents().size(),1);
        Assertions.assertEquals(ad3.getActiveAgents().size(),2);
        ad2.stopSending();
        ad1.stopSending();
        Thread.sleep(2000);
    }
    @Test
    @SneakyThrows
    public void AgentDetectorTest5() {
        AgentDetector ad1 = new AgentDetector("\\Device\\NPF_Loopback",1340,agent1);
        AgentDetector ad2 = new AgentDetector("\\Device\\NPF_Loopback", 1340, agent2);
        AgentDetector ad3 = new AgentDetector("\\Device\\NPF_Loopback", 1340, agent3);
        ad1.sendPacket();
        ad1.startDiscovering();
        Thread.sleep(1000);
        ad2.sendPacket();
        ad2.startDiscovering();
        Thread.sleep(1000);
        ad3.sendPacket();
        ad3.startDiscovering();
        Thread.sleep(1000);
        ad3.stopSending();
        Thread.sleep(2000);
        ad2.stopSending();
        Thread.sleep(4000);
        ad1.stopSending();
        Thread.sleep(2000);
        Assertions.assertEquals(ad1.getActiveAgents().size(),0);
        Assertions.assertEquals(ad2.getActiveAgents().size(),0);
        Assertions.assertEquals(ad3.getActiveAgents().size(),0);
    }


}


