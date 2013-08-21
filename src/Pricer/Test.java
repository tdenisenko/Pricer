package Pricer;

import java.util.Scanner;

public class Test {
	
	public static void main(String[] args) {
		Scanner inp = new Scanner(System.in);
		String a;
		Pricer p = null;
		
		do {
			a = inp.nextLine();
			OrderBook o = new OrderBook(a, p);
			
		}
		while(inp.hasNextLine());
	}
}