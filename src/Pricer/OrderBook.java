package Pricer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * This class creates 2 ArrayLists, ListBuy (Bid orders) and ListSell (Ask orders)
 * And adds the order to a list when an object of it has created.
 * For ex. OrderBook o = new OrderBook(a, p);
 * Where "a" is the order String and "p" is the Pricer object that you want your order to be stored to and added to specified list.
 * See Pricer.java for order message examples, and Test.java for implementation.
 */

public class OrderBook {

	//Keeps the count of total orders given
	static int ORDERBOOK_COUNT = 0;

	//Comparator for sorting the orders by price (.00 precision) and by timestamp is prices are same.
	static Comparator<Pricer> comparator = new Comparator<Pricer>() {
		public int compare(Pricer p1, Pricer p2) {
			if ((int) ((p2.price - p1.price) * 1000) == 0) {
				return (int) (p1.timestamp - p2.timestamp);
			}
			return (int) ((p2.price - p1.price) * 1000);
		}
	};

	//The Ask List (always sorted and synchronized)
	static List<Pricer> listSell = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listSell, comparator);
					return true;
				}
			});

	//The Bid List (always sorted and synchronized)
	static List<Pricer> listBuy = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listBuy, comparator);
					return true;
				}
			});

	//The constructor. It calls init function once an object is created and prints the Order Book once every 100 orders has been submitted.
	public OrderBook(String a, Pricer p) {
		this.init(a, p);
		if(OrderBook.ORDERBOOK_COUNT++ % 100 == 0){
			System.out.println(this.toString());
		}
	}

	//The initiator function. It has 5 cases of recognizing different types of orders, explained below.
	public void init(String a, Pricer p) {
		long timestamp; // time passed since midnight (00:00) in miliseconds
		String message; // "A" for ask or bid, "R" for reduce
		String side; // "S" for sell(ask) "B" for buy(bid)
		double price; // price with a precision of .00
		String orderID; // an ID which may include alphanumeric characters.
		int size; // size of the order
		//Splits order by white spaces
		String[] order = a.split(" ");
		//Judging by the order's length (3 to 6 words)
		switch (order.length) {
		//Format: "Timestamp message orderID side price size"
		//Ex.: "28800538 A 32s S 44.26 100"
		case 6:
			timestamp = Long.valueOf(order[0]);
			message = order[1];
			orderID = order[2];
			side = order[3];
			price = Double.valueOf(order[4]);
			size = Integer.valueOf(order[5]);
			p = new Pricer(timestamp, message, orderID, side, price, size);
			add(p);
			break;
		//Format: "Timestamp message side price size" (ID is auto-generated)
		//Ex.: "28800538 A B 44.26 100"
		case 5:
			timestamp = Long.valueOf(order[0]);
			message = order[1];
			side = order[2];
			price = Double.valueOf(order[3]);
			size = Integer.valueOf(order[4]);
			p = new Pricer(timestamp, message, side, price, size);
			add(p);
			break;
		case 4:
			//Format: "message side price size" (No timestamp format, ID is auto-generated)
			//Ex.: "A B 44.26 100"
			if (order[0].length() <= 1) {
				message = order[0];
				side = order[1];
				price = Double.valueOf(order[2]);
				size = Integer.valueOf(order[3]);
				p = new Pricer(message, side, price, size);
				add(p);
			//Format: "Timestamp message orderID size" (reduce orders)
			//Ex.: "28800538 R 32s 100"
			} else {
				timestamp = Long.valueOf(order[0]);
				message = order[1];
				orderID = order[2];
				size = Integer.valueOf(order[3]);
				p = new Pricer(timestamp, message, orderID, size);
				reduce(p);
			}
			break;
		//Format: "message orderID size" (No timestamp format, reduce orders)
		//Ex.: "R 32s 50"
		case 3:
			message = order[0];
			orderID = order[1];
			size = Integer.valueOf(order[2]);
			p = new Pricer(message, orderID, size);
			reduce(p);
			break;
		}
	}

	//Adds order to the specified list
	public void add(Pricer p) {
		if (p.side.compareTo("S") == 0) {
			listSell.add(p);
			int i = 0;
			outerloop: while (i < listBuy.size()) {
				if (listBuy.get(i).price >= p.price) {
					//Case 1: Sell order with a smaller size than the highest buy order
					if (listBuy.get(i).size > p.size) {
						listBuy.get(i).size -= p.size;
						listSell.remove(listSell.indexOf(p));
						break outerloop;
					//Case 2: Sell order with the same size as the highest buy order
					} else if (listBuy.get(i).size == p.size) {
						listBuy.remove(i);
						listSell.remove(listSell.indexOf(p));
						break outerloop;
					//Case 3: Sell order with bigger size than the highest buy order (continues iteration)
					} else {
						p.size -= listBuy.get(i).size;
						listBuy.remove(i);
					}
				//Case 4: Sell order with a higher price than the highest buy order (just adds the order to list)
				} else {
					break;
				}
			}
		} else if (p.side.compareTo("B") == 0) {
			listBuy.add(p);
			int i = listSell.size() - 1;
			outerloop: while (i >= 0) {
				if (listSell.get(i).price <= p.price) {
					//Case 5: Buy order with a smaller size than the lowest sell order
					if (listSell.get(i).size > p.size) {
						listSell.get(i).size -= p.size;
						listBuy.remove(listBuy.indexOf(p));
						break outerloop;
					//Case 6: Buy order with the same size as the lowest sell order
					} else if (listSell.get(i).size == p.size) {
						listSell.remove(i);
						listBuy.remove(listBuy.indexOf(p));
						break outerloop;
					//Case 7: Buy order with a bigger size than the lowest sell order
					} else {
						p.size -= listSell.get(i).size;
						listSell.remove(i);
						i--;
					}
				//Case 8: Buy order with a lower price than the lowest sell order
				} else {
					break;
				}
			}
		}
	}

	//Reduces (or removes) the order with the specified orderID
	//Finds if the ID given is sell or buy order, removed the order from list if the reduction size is bigger than the order itself, reduces the order's size otherwise.
	public void reduce(Pricer p) {
		int i;
		for (i = 0; i < listSell.size(); i++) {
			if (p.orderID.compareTo(listSell.get(i).orderID) == 0) {
				p.side = "S";
				break;
			}
		}
		int j;
		for (j = 0; j < listBuy.size(); j++) {
			if (p.orderID.compareTo(listBuy.get(j).orderID) == 0) {
				p.side = "B";
				break;
			}
		}
		int res;
		if (p.side == null) {
			System.err.println("Wrong reduce ID!");
			return;
		//Case 9: Reduce order for sell orders
		} else if (p.side.compareTo("B") == 0) {
			res = listBuy.get(j).size - p.size;
			if (res <= 0) {
				listBuy.remove(j);
			} else {
				listBuy.get(j).size = res;
			}
		}
		//Case 10: Reduce order for buy orders
		else if (p.side.compareTo("S") == 0) {
			res = listSell.get(i).size - p.size;
			if (res <= 0) {
				listSell.remove(i);
			} else {
				listSell.get(i).size = res;
			}
		}

	}
	
	//Printing the Order Book with a limit of 10 for both Sell and Buy orders
	public String toString() {
		String s = "\t\t\tBuy Orders\t\tSell Orders\n";
		int i;
		if(listSell.size() >= 10){
			i = listSell.size() - 10;
		}
		else {
			i = 0;
		}
		while (i < listSell.size()) {
			s += listSell.get(i).toString();
			s += "\n";
			i++;
		}
		for (i = 0; i < listBuy.size() && i < 10; i++) {
			s += listBuy.get(i).toString();
			s += "\n";
		}
		return s;
	}
}