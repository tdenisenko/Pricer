package Pricer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

public class Pricer extends Object implements Comparator<Pricer> {

	long timestamp;
	String message;
	static int NUMBER_OF_ORDERS = 0;
	int orderID;
	String side;
	double price;
	int size;	

	// Add order with timestamp (5 param)
	public Pricer(long timestamp, String message, String side, double price,
			int size) {
		this.timestamp = timestamp;
		this.message = message;
		this.orderID = 100 + Pricer.NUMBER_OF_ORDERS;
		Pricer.NUMBER_OF_ORDERS++;
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Add order without timestamp (4 param)
	public Pricer(String message, String side, double price, int size) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.timestamp = now - c.getTimeInMillis();
		this.message = message;
		this.orderID = 100 + Pricer.NUMBER_OF_ORDERS;
		Pricer.NUMBER_OF_ORDERS++;
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Reduce order with timestamp (3 param)
	public Pricer(long timestamp, String message, int orderID, int size) {
		this.timestamp = timestamp;
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
	}

	// Reduce order without timestamp (2 param)
	public Pricer(String message, int orderID, int size) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.timestamp = now - c.getTimeInMillis();
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
	}
	
	public boolean equals(Object p){
		if(this.orderID == ((Pricer) p).orderID) {
			return true;
		}
		else {
			return false;
		}
	}
	public int compare(Pricer p1, Pricer p2) {
		return (int) ((p1.price - p2.price) * 100);
	}
	
	public String toString() {
		String s = "";
		if(this.message.equals("A") && this.side.equals("B")){
			
			s += this.orderID + "\t" + this.price + "\t" + this.size;
		}
		else if(this.message.equals("A") && this.side.equals("S")){
			s += this.orderID + "\t\t\t\t" + this.price + "\t" + this.size;
		}
		return s;
	}
}