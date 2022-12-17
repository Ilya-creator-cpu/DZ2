package DZ2.Behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PongBehaviour extends TickerBehaviour {

    private AID request;
    public PongBehaviour(Agent a, long period) {
        super(a, period);
    }
    @SneakyThrows
    @Override
    protected void onTick() {
        ACLMessage ping = getAgent().receive();
        if (ping!= null) {
            if (ping.getProtocol().equals(this.getAgent().getLocalName()))
                log.info("Yes");
            log.info("Агент " + this.getAgent().getLocalName() + "получил Ping от агента " + ping.getProtocol());
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setContent("Pong");
            message.setProtocol(this.getAgent().getLocalName());
            request = ping.getSender();
            message.addReceiver(ping.getSender());
            myAgent.send(message);
            //log.info("Агент " + this.getAgent().getLocalName() + "отправил Pong агенту "+ request.getName());
        }
        else {
            block();
        }

    }
}
