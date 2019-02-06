@file:Suppress("DEPRECATION", "UNUSED")


import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.html.respondHtml
import io.ktor.http.*

import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*

import java.time.Duration


fun main(args: Array<String>) {

    suspend fun getServerInfo(call: ApplicationCall) {

        call.respondHtml {
            head {
                title { +"KtorServer Info" }
            }
            body {
                h1 {
                    +"KtorServer"
                }
                p {
                    +"This server provides OUIs (Organizationally Unique Identifiers) that have been assigned to a manufacturer by IEEE."
                    +"You can lookup OUIs using the manufacturer name and also discover the manufacturer for a given OUI."
                    +"See the GitHub site for more information: https://github.com/bwixted/ktorserver"
                }
            }

        }


    }

    suspend fun getMacAddress(call: ApplicationCall) {
        val id = call.parameters["id"]

        if (id != null) {
            val mac = DeviceManager.findMac(id)
            if (mac.isEmpty())
                call.respondText("Not Found", ContentType.Text.Plain)
            else {
                call.respondText(mac.toString(), ContentType.Text.Plain)
            }
        } else {
            call.respond(HttpStatusCode.NotFound, "Invalid request!")
        }
    }

    suspend fun getManufacturer(call: ApplicationCall) {
        val id = call.parameters["id"]

        if (id != null) {
            val mac = DeviceManager.findDevice(id)
            if (mac.isEmpty())
                call.respondText("Mac Address Not Found", ContentType.Text.Plain)
            else {
                call.respondText(mac, ContentType.Text.Plain)
            }
        }
    }

    suspend fun getAllManufacturers(call: ApplicationCall) {
        // dump entire list
        val list = DeviceManager.getAllManufacturers()
        val str = StringBuilder()

        str.append("<html>")

        for (item in list) {
            str.append("<div>").append(item).append("</div>")
        }
        str.append("</html>")

        call.respondText(str.toString(), ContentType.Text.Html)
    }

    val server : NettyApplicationEngine = embeddedServer(Netty, port = 8080) {

        install(DefaultHeaders) {
        }

        install(CORS) {
            maxAge = Duration.ofDays(1)
        }

        install(StatusPages) {
            exception<Throwable> { e ->
                call.respondText(e.localizedMessage,ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            }
        }

        routing {

            get("/") {
                getServerInfo(call)
            }

            // returns a list of mac addresses for the manufacturer given in 'id'
            // test url: http://127.0.0.1:8080/mac/freebox
            get("/mac/{id}") {
                getMacAddress(call)
            }

            // returns the manufacturer for the mac given in 'id'
            // test url: http://127.0.0.1:8080/manuf/00:07:3d
            get("/manuf/{id}") {
                getManufacturer(call)
            }

            // returns the manufacturer for the mac given in 'id'
            get("/manuf/") {
                getAllManufacturers(call)
            }

        }
    }
    server.start(wait = true)
}