//Made by Timothy Denisenko.

package Pricer;

import java.util.Calendar;

public class Pricer extends Object {

	long timestamp; // Time passed since midnight (00:00) in milliseconds
	String message; // Type of the order
	static int NUMBER_OF_ORDERS = 0; // Number of orders created
	String orderID; // An ID which may include alphanumeric characters
	char side; // "S" for sell(ask) "B" for buy(bid)
	double price; // Price with a precision of .00
	int size; // Size of the order

	// Limit order, IOC, FOK, Hidden
	public Pricer(String message, char side, double price, int size) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.timestamp = now - c.getTimeInMillis();
		this.message = message;
		this.orderID = IntToLetter(Pricer.NUMBER_OF_ORDERS++);
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Reduce order
	public Pricer(String message, String orderID, int size) {
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

	// Cancel order
	public Pricer(String message, String orderID) {
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
	}

	// Market order
	public Pricer(String message, char side, int size) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.timestamp = now - c.getTimeInMillis();
		this.message = message;
		this.side = side;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
	}

	// Sequence generator
	public static String IntToLetter(int Int) {
		if (Int < 27) {
			return Character.toString((char) (Int + 97));
		} else {
			if (Int % 26 == 0) {
				return IntToLetter((Int / 26) - 1)
						+ IntToLetter((Int % 26) + 1);
			} else {
				return IntToLetter(Int / 26) + IntToLetter(Int % 26);
			}
		}
	}

	// Overridden equals method for comparison with orders' orderID
	public boolean equals(Object p) {
		if (this.orderID == ((Pricer) p).orderID) {
			return true;
		} else {
			return false;
		}
	}

	// toString method for optimized printing.
	// Format: timestamp ID buy/sell
	public String toString() {
		String s = "";
		s += this.timestamp + "\t";
		if (this.message.equals("A") && this.side == 'B') {
			s += this.orderID + "\t" + this.price + "\t" + this.size;
		} else if (this.message.equals("A") && this.side == 'S') {
			s += this.orderID + "\t\t\t\t" + this.price + "\t" + this.size;
		}
		return s;
	}
}