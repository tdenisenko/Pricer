package Pricer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AgentTest {

	public static void main(String[] args) throws IOException {
		// Order Book initialization

		int counter = 0;
		String a;

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Docs/pricer.in"));
			while ((a = br.readLine()) != null && counter++ < 5000) {
				// System.out.println(a + " ->");
				String[] order = a.split(" ");
				String b = "";
				if (order[1].equals("A")) {
					b += "T";
				} else if (order[1].equals("R")) {
					b += "R";
				}
				for (int i = 2; i < order.length; i++) {
					b += " " + order[i];
				}
				@SuppressWarnings("unused")
				OrderBook o = new OrderBook(b);
				// Thread.sleep(1);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			br.close();
		}

		// Agents
		Thread[] agentPool = new Thread[100];

		for (int i = 0; i < agentPool.length; i++) {
			agentPool[i] = new Thread(new Agent());
			agentPool[i].start();
		}
	}
}
