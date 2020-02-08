 
package machine

import java.util.Scanner

class CoffeeMachine(
        var water: Int,
        var milk: Int,
        var beans: Int,
        var cups: Int,
        var money: Int
) {
    enum class CoffeeType(
            val water: Int,
            val milk: Int,
            val beans: Int,
            val money: Int
    ) {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6),
        NULL(-1, -1, -1, -1);
    }
    enum class State {
        BUY, FILL, TAKE,
        REMAINING, NULL;
    }
    enum class ThingToFill(val question: String) {
        WATER("Write how many ml of water do you want to add: "),
        MILK("Write how many ml of milk do you want to add: "),
        BEANS("Write how many grams of coffee beans do you want to add: "),
        CUPS("Write how many disposable cups of coffee do you want to add: ");
    }
    var state = State.NULL
    var coffeeType = CoffeeType.NULL
    val notAvailable
        get() = when {
            water - coffeeType.water <= 0 -> "water"
            milk - coffeeType.milk < 0 -> "milk"
            beans - coffeeType.beans <= 0 -> "coffee beans"
            cups <= 0 -> "cups"
            else -> ""
        }
    val areThingsAvailable get() = notAvailable == ""
    var fill = ThingToFill.WATER
    var action = "back"
        set(value) {
            when (state) {
                State.BUY -> {
                    field = "buy"
                    coffeeType = when (value) {
                        "1" -> CoffeeType.ESPRESSO
                        "2" -> CoffeeType.LATTE
                        "3" -> CoffeeType.CAPPUCCINO
                        else -> CoffeeType.NULL
                    }
                    if (coffeeType != CoffeeType.NULL) {
                        if (areThingsAvailable) {
                            water -= coffeeType.water
                            milk -= coffeeType.milk
                            beans -= coffeeType.beans
                            cups--
                            money += coffeeType.money
                            println("I have enough resources, making you a coffee!")
                        } else println("Sorry, not enough $notAvailable!")
                        println()
                        field = "back"
                    } else if (value == "back") {
                        println()
                        field = value
                    }
                }
                State.FILL -> {
                    fun isInt(value: String): Boolean {
                        val scanner = Scanner(value)
                        var isInt = true
                        do {
                            if (scanner.hasNextInt()) scanner.nextInt()
                            else isInt = false
                        } while (scanner.hasNextInt())
                        return isInt
                    }
                    field = "fill"
                    fill = if (isInt(value)) when (fill.ordinal) {
                        0 -> {
                            this.water += value.toInt()
                            ThingToFill.MILK
                        }
                        1 -> {
                            this.milk += value.toInt()
                            ThingToFill.BEANS
                        }
                        2 -> {
                            this.beans += value.toInt()
                            ThingToFill.CUPS
                        }
                        else -> {
                            println()
                            this.cups += value.toInt()
                            field = "back"
                            ThingToFill.WATER
                        }
                    }
                    else fill
                }
                else -> field = value
            }
            state = when (field) {
                "buy" -> State.BUY
                "fill" -> State.FILL
                "take" -> State.TAKE
                "remaining" -> State.REMAINING
                else -> {
                    if (value != "exit") field = "back"
                    State.NULL
                }
            }
        }
    val message: Unit
        get() {
            when (state) {
                State.BUY -> {
                    println()
                    print(
                            "What do you want to buy? " +
                                    "1 - espresso, " +
                                    "2 - latte, " +
                                    "3 - cappuccino, " +
                                    "back - to main menu: "
                    )
                }
                State.FILL -> {
                    if (fill == ThingToFill.WATER) println()
                    print(fill.question)
                }
                State.TAKE -> {
                    println()
                    println("I gave you \$${this.money}")
                    println()
                    this.money = 0
                    action = "back"
                }
                State.REMAINING -> {
                    println()
                    println("The coffee machine has:")
                    println("${this.water} of water")
                    println("${this.milk} of milk")
                    println("${this.beans} of coffee beans")
                    println("${this.cups} of disposable cups")
                    println("$${this.money} of money")
                    println()
                    action = "back"
                }
                State.NULL -> {}
            }
            if (action == "back")
                print("Write action (buy, fill, take, remaining, exit): ")
        }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val coffeeMachine = CoffeeMachine(
            water = 400,
            milk = 540,
            beans = 120,
            cups = 9,
            money = 550
    )
    do {
        coffeeMachine.message
        coffeeMachine.action = scanner.nextLine()
    } while (coffeeMachine.action != "exit")
}
