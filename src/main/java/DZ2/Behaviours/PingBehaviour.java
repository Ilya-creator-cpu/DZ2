package DZ2.Behaviours;

import DZ2.AgentDetector;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class PingBehaviour extends TickerBehaviour {


    private AgentDetector agentDetector;
@SneakyThrows
    public PingBehaviour(Agent a, long period) {
        super(a, period);
        this.agentDetector = new AgentDetector("\\Device\\NPF_Loopback",1340,this.myAgent.getAID());
        agentDetector.startDiscovering();
        agentDetector.sendPacket();
    }
    @SneakyThrows
    @Override
    protected void onTick() {

        List<AID> agents = new ArrayList<>(agentDetector.getActiveAgents());
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent("Ping");
        message.setProtocol(this.getAgent().getLocalName());
        agents.forEach(agent->{message.addReceiver(agent);
        log.info(this.getAgent().getLocalName() + " отправил Ping агенту " + agent.getLocalName());
        });
        this.getAgent().send(message);

        ACLMessage pong = getAgent().receive();

        if (pong!= null) {
            log.info("Агент " + this.getAgent().getLocalName() + " получил Pong от агента " + pong.getProtocol() );

        }




    }


    }

