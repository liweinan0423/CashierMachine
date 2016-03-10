## Building From Source
The application uses a [Gradle](http://gradle.org)-based build system. In the instructions
below, `./gradlew` is invoked from the root of the source tree and serves as
a cross-platform, self-contained bootstrap mechanism for the build.

### Prerequisites
[JDK 8 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### build and run
To compile from the source code, run all the test cases and build jars:

`./gradlew build`

To run the application, import the project into an IDE and run the `machine.Main` class

### Configure product and promotion data
The samle products and promotions data is contained the following files

* src/main/resources/products.json
* src/main/resources/promotions.json

You can add more products and more promotion rules

#### Promotion Rules

There are 3 kinds of predefined promotion rules

1. Percentage promotion - add percentage discount to order item

2. BuyXGetYFree promotion - on every purchase of x items, customer will get y free

3. Composite promotion

Sometimes one product may be eligible for multiple promotions, but only the high priority promotion will take place. 
In promotion settings we can combine promotions of different priorities into one promotion that contains a high priority promotion
as well as a low priority promotion. When this compisite promotion gets applied, it will determine which promotion will be applied to 
the product. See `src/main/resources/promotions.json` for example.
