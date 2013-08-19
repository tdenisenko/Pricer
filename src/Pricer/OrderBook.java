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
	int orderID;
	int size;

	static Comparator<Pricer> comparatorSell = new Comparator<Pricer>() {
		public int compare(Pricer p1, Pricer p2) {
			return (int) ((p1.price - p2.price) * 100);
		}
	};

	static List<Pricer> listSell = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listSell, comparatorSell);
					return true;
				}
			});
	
	static Comparator<Pricer> comparatorBuy = new Comparator<Pricer>() {
		public int compare(Pricer p1, Pricer p2) {
			return (int) ((p2.price - p1.price) * 100);
		}
	};
	
	static List<Pricer> listBuy = Collections
			.synchronizedList(new ArrayList<Pricer>() {
				public boolean add(Pricer p) {
					super.add(p);
					Collections.sort(listBuy, comparatorBuy);
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
				orderID = Integer.valueOf(order[2]);
				size = Integer.valueOf(order[3]);
				p = new Pricer(timestamp, message, orderID, size);
				reduce(p);
			}
			break;
		case 3:
			message = order[0];
			orderID = Integer.valueOf(order[1]);
			size = Integer.valueOf(order[2]);
			p = new Pricer(message, orderID, size);
			reduce(p);
			break;
		}
	}

	public void add(Pricer p) {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		if(p.side.compareTo("S") == 0){
			listSell.add(p);
			//for(int i = 0; i < listBuy.size(); i++){
			int i = 0;
			outerloop:
			while(i < listBuy.size()){
				if(listBuy.get(i).price >= p.price){
					if(listBuy.get(i).size > p.size){
						listBuy.get(i).size -= p.size;
						listSell.remove(listSell.indexOf(p));
						break outerloop;
					}
					else if(listBuy.get(i).size == p.size) {
						listBuy.remove(i);
						listSell.remove(listSell.indexOf(p));
						i++;
					}
					else {
						p.size -= listBuy.get(i).size;
					}
				}
				else {
					break;
				}
			}
		}
		else if(p.side.compareTo("B") == 0) {
			listBuy.add(p);
			int i = 0;
			outerloop:
			while(i < listSell.size()){
				if(listSell.get(i).price <= p.price){
					if(listSell.get(i).size > p.size){
						listSell.get(i).size -= p.size;
						listBuy.remove(listBuy.indexOf(p));
						break outerloop;
					}
					else if(listSell.get(i).size == p.size) {
						listSell.remove(i);
						listBuy.remove(listBuy.indexOf(p));
						i++;
					}
					else {
						p.size -= listSell.get(i).size;
					}
				}
				else {
					break;
				}
			}
		}
	}

	public void reduce(Pricer p) {
		boolean check = false;
		if(p.side.compareTo("S") == 0){
			for (int i = 0; i < listSell.size(); i++) {
				if (p.orderID == listSell.get(i).orderID) {
					check = true;
					break;
				}
			}
			if (check) {
				int res = listSell.get(listSell.indexOf(p)).size - p.size;
				if (res <= 0) {
					listSell.remove(p);
				} else {
					listSell.get(listSell.indexOf(p)).size = res;
				}

			}
		}
		else if(p.side.compareTo("B") == 0) {
			for (int i = 0; i < listBuy.size(); i++) {
				if (p.orderID == listBuy.get(i).orderID) {
					check = true;
					break;
				}
			}
			if (check) {
				int res = listBuy.get(listBuy.indexOf(p)).size - p.size;
				if (res <= 0) {
					listBuy.remove(p);
				} else {
					listBuy.get(listBuy.indexOf(p)).size = res;
				}

			}
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

	// @Override
	// public int compareTo(Pricer p) {
	// return (int) ((list.get(list.indexOf(p)).price - p.price) * 100);
	// }

}
