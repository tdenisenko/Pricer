package Pricer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * This class reads thousands of orders from the file "pricer.in" and adds them to the Order Book.
 */

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		String a;
		Pricer p = null;

		BufferedReader br = new BufferedReader(new FileReader("pricer.in"));
		try {
			while ((a = br.readLine()) != null) {
				//System.out.println(a + " ->");
				OrderBook o = new OrderBook(a, p);
				//Thread.sleep(200);
			}
		} finally {
			br.close();
		}
		System.out.println("Done!\nTotal orders processed: " + OrderBook.ORDERBOOK_COUNT);
	}
}