


import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.HashMap

/*
 * "devices.txt" contains MAC address to manufacturer mappings.  Each line is of the format:
 *
 * AA-AA-AA|Manufacturer
 *
 * Use the "DeviceGenerator" program to recreate the file from its source:
 *   http://standards-oui.ieee.org/oui/oui.txt
 *
 *   TBD incorporate these deprecated also
 *   # http://standards.ieee.org/develop/regauth/iab/iab.txt
 *   # http://standards.ieee.org/develop/regauth/oui36/oui36.txt
 */
object DeviceManager  {

    private val map = HashMap<String, String>()       // mac to manufacturer name

    init {

        try {

            val inputStream = javaClass.classLoader.getResourceAsStream("devices.txt")
            val reader = inputStream.bufferedReader();
            val iterator = reader.lineSequence().iterator();

            while (iterator.hasNext()) {

                val line = iterator.next()

                // comment lines start with '#' so ignore them
                val pos = line.indexOf('|')

                if (pos == 0) {
                    continue
                }

                val mac = line.substring(0, pos).replace('-', ':')
                val device = line.substring(pos + 1, line.length)

                map[mac] = device
                //DEBUG(mac + " -> " + device);
            }

        } catch (e: Exception) {
            println("error")
        }


    }

    fun getAllManufacturers(): ArrayList<String> {

        val manufs = HashMap<String, Int>()

        for ((mac, manuf) in map) {
            manufs[manuf] = 1
        }
        val list: ArrayList<String> = ArrayList()
        for ((manuf,value) in manufs) {
            list.add(manuf)
        }
        list.sort()
        return list
    }

    fun findMac(manufacturer: String): ArrayList<String> {

        var list: ArrayList<String> = ArrayList()

        val man = manufacturer.toLowerCase()

        for ((mac, value) in map) {
            val name = value.toLowerCase()
            if (name.contains(man)) {
                list.add(mac)
            }
        }
        return list
    }

    fun findDevice(idparam: String): String {

        var id = idparam
        var device: String = ""

        if (id.isEmpty())
            return device

        id = id.toUpperCase()

        // needs to search 6 octets first
        val subid = id.substring(0, 8)
        val s = map[subid]
        device = s ?: ""

        if (device.isEmpty()) {
            val s2 = map[id]
            device = s2 ?: ""
        }

        return device
    }

}
