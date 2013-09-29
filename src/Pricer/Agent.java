package Pricer;

/*
 * 4.5 hours test run, price is stable.
 */

import java.text.DecimalFormat;
import java.util.Random;

public class Agent implements Runnable {

	static int AGENT_COUNT;
	static Random r = new Random();

	Agent() {
		AGENT_COUNT++;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String a = "";
			String message = "L";
			String side = "";
			// 50% chance for Bid and Ask
			if (Math.random() < 0.5) {
				side += "B";
			} else {
				side += "S";
			}
			double price;
			// mean = current price
			if (side.equals("B")) {
				price = OrderBook.listBuy.get(0).price;
			} else {
				price = OrderBook.listSell.get(OrderBook.listSell.size() - 1).price;
			}
			// std. dev. = 1
			price += r.nextGaussian();
			int temp = (int)(price * 100);
			price = temp / 100.0;
			// mean = 100
			int size = 100;
			// std. dev. = 25
			size += (int) (r.nextGaussian() * 25);
			if (size < 1) {
				size = 1;
			}
			a += message + " " + side + " " + price + " " + size;
			try {
				@SuppressWarnings("unused")
				OrderBook o = new OrderBook(a);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long millis = ((long) (r.nextGaussian() * 1000)) + 5000;
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
