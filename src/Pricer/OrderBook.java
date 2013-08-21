package Pricer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderBook {

	long timestamp;
	String message;
	String side;
	double price;
	String orderID;
	int size;

	static Comparator<Pricer> comparator = new Comparator<Pricer>() {
		public int compare(Pricer p1, Pricer p2) {
			if((int) ((p2.price - p1.price) * 100) == 0){
				return (int) (p2.timestamp - p1.timestamp);
			}
			return (int) ((p2.price - p1.price) * 100);
		}
	};

	static List<Pricer> listSell = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listSell, comparator);
					return true;
				}
			});

	static List<Pricer> listBuy = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listBuy, comparator);
					return true;
				}
			});

	public OrderBook(String a, Pricer p) {
		this.init(a, p);
		System.out.println(this.toString());
	}

	public void init(String a, Pricer p) {
		String[] order = a.split(" ");
		switch (order.length) {
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
			if (order[0].length() <= 1) {
				message = order[0];
				side = order[1];
				price = Double.valueOf(order[2]);
				size = Integer.valueOf(order[3]);
				p = new Pricer(message, side, price, size);
				add(p);
			} else {
				timestamp = Long.valueOf(order[0]);
				message = order[1];
				orderID = order[2];
				size = Integer.valueOf(order[3]);
				p = new Pricer(timestamp, message, orderID, size);
				reduce(p);
			}
			break;
		case 3:
			message = order[0];
			orderID = order[1];
			size = Integer.valueOf(order[2]);
			p = new Pricer(message, orderID, size);
			reduce(p);
			break;
		}
	}

	public void add(Pricer p) {
		if (p.side.compareTo("S") == 0) {
				listSell.add(p);
				int i = 0;
				outerloop: while (i < listBuy.size()) {
					if (listBuy.get(i).price >= p.price) {
						if (listBuy.get(i).size > p.size) {
							listBuy.get(i).size -= p.size;
							listSell.remove(listSell.indexOf(p));
							break outerloop;
						} else if (listBuy.get(i).size == p.size) {
							listBuy.remove(i);
							listSell.remove(listSell.indexOf(p));
							break outerloop;
						} else {
							p.size -= listBuy.get(i).size;
							listBuy.remove(i);
						}
					} else {
						break;
					}
				}
		} else if (p.side.compareTo("B") == 0) {
				listBuy.add(p);
				int i = listSell.size() - 1;
				outerloop: while (i >= 0) {
					if (listSell.get(i).price <= p.price) {
						if (listSell.get(i).size > p.size) {
							listSell.get(i).size -= p.size;
							listBuy.remove(listBuy.indexOf(p));
							break outerloop;
						} else if (listSell.get(i).size == p.size) {
							listSell.remove(i);
							listBuy.remove(listBuy.indexOf(p));
							break outerloop;
						} else {
							p.size -= listSell.get(i).size;
							listSell.remove(i);
							i--;
						}
					} else {
						break;
					}
				}
		}
	}

	public void reduce(Pricer p) {
		int i;
		for(i = 0; i < listSell.size(); i++){
			if(p.orderID.compareTo(listSell.get(i).orderID) == 0){
				p.side = "S";
				break;
			}
		}
		int j;
		for(j = 0; j < listBuy.size(); j++){
			if(p.orderID.compareTo(listBuy.get(j).orderID) == 0){
				p.side = "B";
				break;
			}
		}
		int res;
		if(p.side.compareTo("S") == 0) {
			res = listSell.get(i).size - p.size;
			if (res <= 0) {
				listSell.remove(i);
			} else {
				listSell.get(i).size = res;
			}
		}
		else if (p.side.compareTo("B") == 0) {
			res = listBuy.get(j).size - p.size;
			if (res <= 0) {
				listBuy.remove(j);
			} else {
				listBuy.get(j).size = res;
			}
		}
		else {
			System.err.println("Wrong reduce ID!");
			return;
		}

	}

	// A S 44.26 100
	public String toString() {
		String s = "\tBuy Orders\t\tSell Orders\n";
		for (int i = 0; i < listSell.size(); i++) {
			s += listSell.get(i).toString();
			s += "\n";
		}
		for (int i = 0; i < listBuy.size(); i++) {
			s += listBuy.get(i).toString();
			s += "\n";
		}
		return s;
	}

}