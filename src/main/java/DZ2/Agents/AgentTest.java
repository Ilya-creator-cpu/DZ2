package DZ2.Agents;

import DZ2.Behaviours.PingBehaviour;
import DZ2.Behaviours.PongBehaviour;
import jade.core.Agent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentTest extends Agent {
    @SneakyThrows
    public void setup() {

       addBehaviour(new PongBehaviour(this,2000));
       addBehaviour(new PingBehaviour(this,2000));

    }
}
