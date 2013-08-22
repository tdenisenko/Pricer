Pricer project by Timothy Denisenko
======

This project is an example of implementation of simple Order Book and its functions in Java.

Compiled and tested in:
Eclipse IDE for Java Developers
Version: Kepler Release
Build id: 20130614-0229

This project has 3 classes: Pricer.java, OrderBook.java and Test.java

Pricer.java
======
This class is a data structure for storing individual orders in a class.
It accepts different kinds of order formats.

Formats:
*Format: "Timestamp message orderID side price size"
*Ex.: "28800538 A 32s S 44.26 100"

*Format: "Timestamp message side price size" (ID is auto-generated)
*Ex.: "28800538 A B 44.26 100"

*Format: "message side price size" (No timestamp format, ID isauto-generated)
*Ex.: "A B 44.26 100"

*Format: "Timestamp message orderID size" (reduce orders)
*Ex.: "28800538 R 32s 100"

*Format: "message orderID size" (No timestamp format, reduce orders)
*Ex.: "R 32s 50"

OrderBook.java
======
This class is a data structure for storing all orders together in a class.
This class includes 2 ArrayLists; listBuy and listSell which holds Buy(bid) and Sell(ask) orders respectively.
It is sorted (by price and by timestamps when the prices are the same) and synchronized (takes an order at a time) anytime.
It prints the Order Book once every 100 orders has been processed.
This class creates 2 ArrayLists, ListBuy (Bid orders) and ListSell (Ask orders)
It adds the order to a list when an object of it has created.
For ex. OrderBook o = new OrderBook(a, p);
Where "a" is the order String and "p" is the Pricer object that you want your order to be stored to and added to specified list.
See Pricer.java for order message examples, and Test.java for implementation.

Test.java
======
This class tests orders from a given file (pricer.in) that includes over 1 million orders.

Functions of Pricer Project
======
It has 10 cases of processing orders.
*1) Sell order with a smaller size than the highest buy order. Solution: Remove the sell order and reduce the size of the highest buy order.
*2) Sell order with the same size as the highest buy order. Solution: Remove both lowest sell and highest buy order.
*3) Sell order with bigger size than the highest buy order. Solution: Decrease the size of sell order by the highest buy order, remove the highest buy order, iterate from the first case.
*4) Sell order with a higher price than the highest buy order. Solution: Just add the sell order to the Order Book.
*5) Buy order with a smaller size than the lowest sell order. Solution: Remove the buy order and reduce the size of the lowest sell order.
*6) Buy order with the same size as the lowest sell order. Solution: Remove both lowest sell and highest buy order.
*7) Buy order with bigger size than the lowest sell order. Solution: Decrease the size of buy order by the lowest sell order, remove the lowest sell order, iterate from the first case.
*8) Buy order with a lower price than the lowest buy order. Solution: Just add the buy order to the Order Book.
*9) Reduce order for sell orders. Solution: Remove order from the Order Book if reduce order's size is equal to or bigger than the order size, reduce the size otherwise.
*10) Reduce order for buy orders. Solution: Remove order from the Order Book if reduce order's size is equal to or bigger than the order size, reduce the size otherwise.
