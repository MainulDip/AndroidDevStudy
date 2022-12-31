## Overview:
Some common design pattern with quick example

### Builder Pattern:
It's a pattern to build up a complex object in a step by step approach.
```kotlin
open class Item(val name: String, val price: Int)

class Noodles : Item("Noodles", 10) {
    override fun toString(): String {
        return name
    }
}

class Vegetables(vararg val toppings: String) : Item("Vegetables", 5) {
    override fun toString(): String {
        if (toppings.isEmpty()) {
            return "$name Chef's Choice"
        } else {
            return name + " " + toppings.joinToString()
        }
    }
}

class Order(val orderNumber: Int) {
    private val itemList = mutableListOf<Item>()

    fun addItem(newItem: Item): Order {
        itemList.add(newItem)
        return this
    }

    fun addAll(newItems: List<Item>): Order {
        itemList.addAll(newItems)
        return this
    }

    fun print() {
        println("Order #${orderNumber}")
        var total = 0
        for (item in itemList) {
            println("${item}: $${item.price}")
            total += item.price
        }
        println("Total: $${total}")
    }
}

fun main() {
    val ordersList = mutableListOf<Order>()

    // Add an item to an order
    val order1 = Order(1)
    order1.addItem(Noodles())
    ordersList.add(order1)

    // Add multiple items individually
    val order2 = Order(2)
    order2.addItem(Noodles())
    order2.addItem(Vegetables())
    ordersList.add(order2)

    // Add a list of items at one time
    val order3 = Order(3)
    val items = listOf(Noodles(), Vegetables("Carrots", "Beans", "Celery"))
    order3.addAll(items)
    ordersList.add(order3)

    // Use builder pattern
    val order4 = Order(4)
        .addItem(Noodles())
        .addItem(Vegetables("Cabbage", "Onion"))
    ordersList.add(order4)

    // Create and add order directly
    ordersList.add(
        Order(5)
            .addItem(Noodles())
            .addItem(Noodles())
            .addItem(Vegetables("Spinach"))
    )

    // Print out each order
    for (order in ordersList) {
        order.print()
        println()
    }
}

```


### Design Pattern Common:
* Adapter: Adapter is a design pattern that adapts the data into something that can be used by RecyclerView. Usually adapter fetch data from a datastore/databases and format the data to feed the ui/recyclerview.

* ViewHolders: RecyclerView doesn't interact directly with item views, but deals with ViewHolders. A ViewHolder represents a single list item view in RecyclerView, and can be reused when possible. A ViewHolder instance holds references to the individual views within a list item layout (hence the name "view holder"). This makes it easier to update the list item view with new data. View holders also add information that RecyclerView uses to efficiently move views around the screen
* LayoutInflater: inflate an XML layout into a hierarchy of view objects. Or simply it converts xml layout file into a view objects

### Android Architectural Principles:
* The main principles are separation of concerns and driving UI from a model.
 - Separation of concerns: The separation of concerns design principle states that the app should be divided into classes, each with separate responsibilities.
 - Drive UI from a model:  UI should drive/come from a model, preferably a persistent model. Models are components that are responsible for handling the data for an app. They're independent from the Views and app components, so they're unaffected by the app's lifecycle and the associated concerns.

 * The main classes or components in Android Architecture are UI Controller (activity/fragment), ViewModel, LiveData and Room.

 ### UI Controllers(activity/fragment) vs ViewModel:
 The Android system can destroy UI controllers at any time based on certain user interactions or because of system conditions like low memory. Thats why it's not a good place to store app's state. Instead, the decision-making logic about the data should be added in your ViewModel. The ViewModel stores the app related data that isn't destroyed when activity or fragment is destroyed and recreated by the Android framework.

 * Activities and fragments are responsible for drawing views and data to the screen and responding to the user events. As licycle of this is not on developers hand, application state should never live here.

 * ViewModel is responsible for holding and processing all the data needed for the UI. It should never access the view hierarchy (like view binding object) or hold a reference to the activity or the fragment. It will only process and deliver the data to the UI controllers.