package Pricer;

public class AgentTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread[] agentPool = new Thread[10];

		for (int i = 0; i < agentPool.length; i++) {
			agentPool[i] = new Thread(new Agent());
			agentPool[i].start();
		}
	}
}
