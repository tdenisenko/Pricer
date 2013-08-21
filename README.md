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
See further explanation and details in comments on the code.

OrderBook.java
======
This class is a data structure for storing all orders together in a class.
This class includes 2 ArrayLists; listBuy and listSell which holds Buy(bid) and Sell(ask) orders respectively.
It is sorted (by price and by timestamps when the prices are the same) and synchronized (takes an order at a time) anytime.
It prints the Order Book once every 100 orders has been processed.
See further details and order types in comments on the code.

Test.java
======
This class tests orders from a given file (pricer.in) that includes over 1 million orders.

Functions of Pricer Project
======
It has 10 cases of processing orders.
1) Sell order with a smaller size than the highest buy order. Solution: Remove the sell order and reduce the size of the highest buy order.
2) Sell order with the same size as the highest buy order. Solution: Remove both lowest sell and highest buy order.
3) Sell order with bigger size than the highest buy order. Solution: Decrease the size of sell order by the highest buy order, remove the highest buy order, iterate from the first case.
4) Sell order with a higher price than the highest buy order. Solution: Just add the sell order to the Order Book.
5) Buy order with a smaller size than the lowest sell order. Solution: Remove the buy order and reduce the size of the lowest sell order.
6) Buy order with the same size as the lowest sell order. Solution: Remove both lowest sell and highest buy order.
7) Buy order with bigger size than the lowest sell order. Solution: Decrease the size of buy order by the lowest sell order, remove the lowest sell order, iterate from the first case.
8) Buy order with a lower price than the lowest buy order. Solution: Just add the buy order to the Order Book.
9) Reduce order for sell orders. Solution: Remove order from the Order Book if reduce order's size is equal to or bigger than the order size, reduce the size otherwise.
10) 9) Reduce order for buy orders. Solution: Remove order from the Order Book if reduce order's size is equal to or bigger than the order size, reduce the size otherwise.
