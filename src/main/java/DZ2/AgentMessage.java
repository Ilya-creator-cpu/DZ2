package DZ2;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AgentMessage {

    private String agentName;
    private long messageTime;

    public AgentMessage(String agentName, long time) {
        this.agentName = agentName;
        this.messageTime = time;

    }


  /*  public List<AgentMessage> change(List<AgentMessage> agentMessages, AgentMessage agentMessage) {

        for (AgentMessage now : agentMessages) {
            if (now.getAgentName().equals(agentMessage.getAgentName())) {
                agentMessages.remove(now);
                agentMessages.add(agentMessage);
            }
        }

        return agentMessages;
    } */

}