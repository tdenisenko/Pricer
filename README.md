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
