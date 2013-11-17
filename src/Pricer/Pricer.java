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
	double limit;
	int size; // Size of the order

	// temp constructor (for pricer.in)
	public Pricer(String message, String orderID, char side, double price,
			int size) {
		setCurrentTimestamp(this);
		this.message = message;
		this.orderID = orderID;
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Limit order, IOC, FOK, Hidden
	public Pricer(String message, char side, double price, int size) {
		setCurrentTimestamp(this);
		this.message = message;
		this.orderID = IntToLetter(Pricer.NUMBER_OF_ORDERS++);
		this.side = side;
		this.price = price;
		this.size = size;
	}
	
	//Peg orders
	public Pricer(String message, char side, double price, int size, double limit) {
		setCurrentTimestamp(this);
		this.message = message;
		this.orderID = IntToLetter(Pricer.NUMBER_OF_ORDERS++);
		this.side = side;
		this.price = price;
		this.limit = limit;
		this.size = size;
	}

	// Reduce order
	public Pricer(String message, String orderID, int size) {
		setCurrentTimestamp(this);
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
	}

	// Cancel order
	public Pricer(String message, String orderID) {
		setCurrentTimestamp(this);
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
	}

	// Market order
	public Pricer(String message, char side, int size) {
		setCurrentTimestamp(this);
		this.message = message;
		this.side = side;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
		if (this.side == 'S') {
			this.price = Double.MIN_NORMAL;
		} else if (this.side == 'B') {
			this.price = Double.MAX_VALUE;
		}
	}
	
	// Broadcasts
	public Pricer(String message, int size, double price) {
			setCurrentTimestamp(this);
			this.message = message;
			this.size = size;
			this.price = price;
		}
	
	public void setCurrentTimestamp(Pricer p) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		p.timestamp = now - c.getTimeInMillis();
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
		if (this.orderID.equals(((Pricer) p).orderID)) {
			return true;
		} else {
			return false;
		}
	}

	// toString method for optimized printing.
	// Format: timestamp ID buy/sell
	// log?
	public String toString() {
		//String s = "";
		String s = this.message;
		s += this.timestamp + "\t";
		if ((this.message.equals("L") || this.message.equals("T") || this.message.equals("S") || this.message.equals("P"))
				&& this.side == 'B') {
			s += this.orderID + "\t" + this.price + "\t" + this.size;
		} else if ((this.message.equals("L") || this.message.equals("T") || this.message.equals("S") || this.message.equals("P"))
				&& this.side == 'S') {
			s += this.orderID + "\t\t\t\t" + this.price + "\t" + this.size;
		} else if (this.message.equals("M") && this.side == 'B') {
			s += this.orderID + "\tMarket\t" + this.size;
		} else if (this.message.equals("M") && this.side == 'S') {
			s += this.orderID + "\t\t\t\tMarket\t" + this.size;
		} else if (this.message.equals("H") && this.side == 'B') {
			s += this.orderID + "\t" + this.price + "\t" + this.size
					+ "\t\t\t\t(HIDDEN)";
		} else if (this.message.equals("H") && this.side == 'S') {
			s += this.orderID + "\t\t\t\t" + this.price + "\t" + this.size
					+ "\t(HIDDEN)";
		}
		return s;
	}
}