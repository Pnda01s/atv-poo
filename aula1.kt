class HasNet(val rede: Boolean){

}

class SmartDevice
    (   val name: String,
        val category: String,

    ){
        val rede: String = ""
        fun turnOn() {
            println("Smart device is turned on.")
        }

    fun turnOff() {
        println("Smart device is turned off.")
    }
}



fun main() {
    val obj1 = SmartDevice(category = "Android", name = "Android", )
    obj1.turnOn()
    obj1.turnOff()
    println(obj1.name)
}