//Made by Timothy Denisenko

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

	// Keeps the count of total orders given
	// static int ORDERBOOK_COUNT = 0;

	// Comparator for sorting the orders by price (.00 precision) and by
	// timestamp if prices are same.
	static Comparator<Pricer> comparator = new Comparator<Pricer>() {
		public int compare(Pricer p1, Pricer p2) {
			if ((int) ((p2.price - p1.price) * 1000) == 0) {
				if((p1.message.compareTo("H") != 0 && p2.message.compareTo("H") != 0) || (p1.message.compareTo("H") == 0 && p2.message.compareTo("H") == 0)) {
					return (int) (p1.timestamp - p2.timestamp);
				}
				else if(p1.message.compareTo("H") != 0 && p2.message.compareTo("H") == 0) {
					return -1;
				}
				else if(p1.message.compareTo("H") == 0 && p2.message.compareTo("H") != 0) {
					return 1;
				}
			}
			return (int) ((p2.price - p1.price) * 1000);
		}
	};

	// The Ask List (always sorted and synchronized)
	static List<Pricer> listSell = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listSell, comparator);
					return true;
				}
			});

	// The Bid List (always sorted and synchronized)
	static List<Pricer> listBuy = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listBuy, comparator);
					return true;
				}
			});
	
	// The Temporary List (always sorted and synchronized)
	static List<Pricer> listTemp = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listBuy, comparator);
					return true;
				}
			});

	// The constructor. It calls init function once an object is created and
	// prints the Order Book once every 100 orders has been submitted.
	public OrderBook(String a) throws InterruptedException {
		this.init(a);
		if (Pricer.NUMBER_OF_ORDERS % 100 == 0) {
			System.out.println(this.toString());
			// Thread.sleep(500); //Activate if you want to watch it stream
		}
	}

	/*
	 * Renewed init function Time stamps and IDs are auto-generated. Formats:
	 * Limit order: "message side price size" Ex.: "L B 32.20 100" (Buy 100
	 * stocks for 32.20) Reduce order: "message orderID size" Ex.: "R qwe 150"
	 * (Reduce order qwe by 150) Cancel order: "message orderID" Ex.: "C cfh"
	 * (Cancel order cfh) Market order: "message side size" Ex.: "M S 200" (Sell
	 * 200 stocks at the highest available price) IOC: "message side price size"
	 * Ex.: "I B 40.00 100" (Buy up to 100 stocks at price 40.00 or lower and
	 * cancel remaining order immediately) FOK: "message side price size" Ex.:
	 * "F S 38.10 50" (Sell 50 orders at 38.10 or higher price, if less than 50
	 * available, cancel the order completely without any trades) Hidden order:
	 * "message side price size" Ex.: "H S 45.40 1000" (Sell 1000 stocks at
	 * 45.40 or lower price but the priority of this order is last compared to
	 * other orders at same price)
	 */
	public void init(String a) {
		Pricer p = null;
		String message;
		char side;
		double price;
		String orderID;
		int size;
		// Splits order by white spaces
		String[] order = a.split(" ");
		message = order[0];
		switch (message) {
		case "L":
			side = order[1].charAt(0);
			price = Double.valueOf(order[2]);
			size = Integer.valueOf(order[3]);
			p = new Pricer(message, side, price, size);
			add(p);
			break;
		case "R":
			orderID = order[1];
			size = Integer.valueOf(order[2]);
			p = new Pricer(message, orderID, size);
			reduce(p);
			break;
		case "C":
			orderID = order[1];
			p = new Pricer(message, orderID);
			cancel(p);
			break;
		case "M":
			side = order[1].charAt(0);
			size = Integer.valueOf(order[2]);
			p = new Pricer(message, side, size);
			if(p.side == 'S') {
				p.price = Double.MIN_NORMAL;
			}
			else if(p.side == 'B') {
				p.price = Double.MAX_VALUE;
			}
			add(p);
			break;
		case "I":
			side = order[1].charAt(0);
			price = Double.valueOf(order[2]);
			size = Integer.valueOf(order[3]);
			p = new Pricer(message, side, price, size);
			ioc(p);
			break;
		case "F":
			side = order[1].charAt(0);
			price = Double.valueOf(order[2]);
			size = Integer.valueOf(order[3]);
			p = new Pricer(message, side, price, size);
			fok(p);
			break;
		case "H":
			side = order[1].charAt(0);
			price = Double.valueOf(order[2]);
			size = Integer.valueOf(order[3]);
			p = new Pricer(message, side, price, size);
			add(p);
			break;
		default:
			System.err.println("Wrong order type!");
		}
	}

	// Processes add orders
	public void add(Pricer p) {
		if (p.side == 'S') {
			listSell.add(p);
			int i = 0;
			outerloop: while (i < listBuy.size()) {
				if (listBuy.get(i).price >= p.price) {
					// Case 1: Sell order with a smaller size than the highest
					// buy order
					if (listBuy.get(i).size > p.size) {
						listBuy.get(i).size -= p.size;
						listSell.remove(listSell.indexOf(p));
						break outerloop;
						// Case 2: Sell order with the same size as the highest
						// buy order
					} else if (listBuy.get(i).size == p.size) {
						listBuy.remove(i);
						listSell.remove(listSell.indexOf(p));
						break outerloop;
						// Case 3: Sell order with bigger size than the highest
						// buy order (continues iteration)
					} else {
						p.size -= listBuy.get(i).size;
						listBuy.remove(i);
					}
					// Case 4: Sell order with a higher price than the highest
					// buy order (just adds the order to list)
				} else {
					break;
				}
			}
		} else if (p.side == 'B') {
			listBuy.add(p);
			int i = listSell.size() - 1;
			outerloop: while (i >= 0) {
				if (listSell.get(i).price <= p.price) {
					// Case 5: Buy order with a smaller size than the lowest
					// sell order
					if (listSell.get(i).size > p.size) {
						listSell.get(i).size -= p.size;
						listBuy.remove(listBuy.indexOf(p));
						break outerloop;
						// Case 6: Buy order with the same size as the lowest
						// sell order
					} else if (listSell.get(i).size == p.size) {
						listSell.remove(i);
						listBuy.remove(listBuy.indexOf(p));
						break outerloop;
						// Case 7: Buy order with a bigger size than the lowest
						// sell order
					} else {
						p.size -= listSell.get(i).size;
						listSell.remove(i);
						i--;
					}
					// Case 8: Buy order with a lower price than the lowest sell
					// order
				} else {
					break;
				}
			}
		}
	}
	
	public void ioc(Pricer p) {
		if (p.side == 'S') {
			int i = 0;
			outerloop: while (i < listBuy.size()) {
				if (listBuy.get(i).price >= p.price) {
					// Case 1: Sell order with a smaller size than the highest
					// buy order
					if (listBuy.get(i).size > p.size) {
						listBuy.get(i).size -= p.size;
						break outerloop;
						// Case 2: Sell order with the same size as the highest
						// buy order
					} else if (listBuy.get(i).size == p.size) {
						listBuy.remove(i);
						break outerloop;
						// Case 3: Sell order with bigger size than the highest
						// buy order (continues iteration)
					} else {
						p.size -= listBuy.get(i).size;
						listBuy.remove(i);
					}
					// Case 4: Sell order with a higher price than the highest
					// buy order (just adds the order to list)
				} else {
					break;
				}
			}
		} else if (p.side == 'B') {
			int i = listSell.size() - 1;
			outerloop: while (i >= 0) {
				if (listSell.get(i).price <= p.price) {
					// Case 5: Buy order with a smaller size than the lowest
					// sell order
					if (listSell.get(i).size > p.size) {
						listSell.get(i).size -= p.size;
						break outerloop;
						// Case 6: Buy order with the same size as the lowest
						// sell order
					} else if (listSell.get(i).size == p.size) {
						listSell.remove(i);
						break outerloop;
						// Case 7: Buy order with a bigger size than the lowest
						// sell order
					} else {
						p.size -= listSell.get(i).size;
						listSell.remove(i);
						i--;
					}
					// Case 8: Buy order with a lower price than the lowest sell
					// order
				} else {
					break;
				}
			}
		}
	}
	
	public void fok(Pricer p) {
		if (p.side == 'S') {
			int i = 0;
			outerloop: while (i < listBuy.size()) {
				if (listBuy.get(i).price >= p.price) {
					// Case 1: Sell order with a smaller size than the highest
					// buy order
					if (listBuy.get(i).size > p.size) {
						listBuy.get(i).size -= p.size;
						break outerloop;
						// Case 2: Sell order with the same size as the highest
						// buy order
					} else if (listBuy.get(i).size == p.size) {
						listBuy.remove(i);
						break outerloop;
						// Case 3: Sell order with bigger size than the highest
						// buy order (continues iteration)
					} else {
						p.size -= listBuy.get(i).size;
						listTemp.add(listBuy.remove(i));
					}
					// Case 4: Sell order with a higher price than the highest
					// buy order (just adds the order to list)
				} else {
					listBuy.addAll(listTemp);
					break;
				}
			}
		} else if (p.side == 'B') {
			int i = listSell.size() - 1;
			outerloop: while (i >= 0) {
				if (listSell.get(i).price <= p.price) {
					// Case 5: Buy order with a smaller size than the lowest
					// sell order
					if (listSell.get(i).size > p.size) {
						listSell.get(i).size -= p.size;
						break outerloop;
						// Case 6: Buy order with the same size as the lowest
						// sell order
					} else if (listSell.get(i).size == p.size) {
						listSell.remove(i);
						break outerloop;
						// Case 7: Buy order with a bigger size than the lowest
						// sell order
					} else {
						p.size -= listSell.get(i).size;
						listTemp.add(listSell.remove(i));
						i--;
					}
					// Case 8: Buy order with a lower price than the lowest sell
					// order
				} else {
					listSell.addAll(listTemp);
				}
			}
		}
		listTemp.clear();
	}

	// Reduces (or removes) the order with the specified orderID
	// Finds if the ID given is sell or buy order, removed the order from list
	// if the reduction size is bigger than the order itself, reduces the
	// order's size otherwise.
	public void reduce(Pricer p) {
		p.side = 'N';
		int i;
		for (i = 0; i < listSell.size(); i++) {
			if (p.orderID.compareTo(listSell.get(i).orderID) == 0) {
				p.side = 'S';
				break;
			}
		}
		int j;
		for (j = 0; j < listBuy.size(); j++) {
			if (p.orderID.compareTo(listBuy.get(j).orderID) == 0) {
				p.side = 'B';
				break;
			}
		}
		int res;
		// This order has been executed or never existed.
		if (p.side == 'N') {
			// System.err.println("Wrong reduce order\nThis order has been executed already or has never existed.");
			return;
		} else if (p.side == 'B') {
			res = listBuy.get(j).size - p.size;
			if (res <= 0) {
				listBuy.remove(j);
			} else {
				listBuy.get(j).size = res;
			}
		} else if (p.side == 'S') {
			res = listSell.get(i).size - p.size;
			if (res <= 0) {
				listSell.remove(i);
			} else {
				listSell.get(i).size = res;
			}
		}
	}

	public void cancel(Pricer p) {
		p.side = 'N';
		int i;
		for (i = 0; i < listSell.size(); i++) {
			if (p.orderID.compareTo(listSell.get(i).orderID) == 0) {
				p.side = 'S';
				break;
			}
		}
		int j;
		for (j = 0; j < listBuy.size(); j++) {
			if (p.orderID.compareTo(listBuy.get(j).orderID) == 0) {
				p.side = 'B';
				break;
			}
		}
		// This order has been executed or never existed.
		if (p.side == 'N') {
			// System.err.println("Wrong cancel order\nThis order has been executed already or has never existed.");
			return;
		} else if (p.side == 'B') {
			listBuy.remove(j);
		} else if (p.side == 'S') {
			listSell.remove(i);
		}
	}
	
	// Printing the Order Book with a limit of 10 for both Sell and Buy orders
	public String toString() {
		String s = "\t\t\tBuy Orders\t\tSell Orders\n";
		int i;
		if (listSell.size() >= 10) {
			i = listSell.size() - 10;
		} else {
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