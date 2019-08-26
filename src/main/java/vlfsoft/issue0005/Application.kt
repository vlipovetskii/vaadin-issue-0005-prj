package vlfsoft.issue0005

import com.vaadin.flow.spring.annotation.EnableVaadin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.event.EventListener
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.*

@ComponentScan("vlfsoft")
@EnableVaadin("vlfsoft")
@SpringBootApplication
open class VlpApplication {

    @Suppress("unused")
    @EventListener(ApplicationReadyEvent::class)
    fun applicationReadyEvent() {
        // KDesktop.safeBrowse(URI("http://localhost:8080"))
        // VM options: -Durl=http://localhost:8080/treegrid-update-columns-view
        KDesktop.safeBrowse(URI(propertiesFromSystem.getProperty("url") ?: "http://localhost:8080/treegrid-update-columns-view"))
    }

}

/**
 * https://stackoverflow.com/questions/7054972/java-system-properties-and-environment-variables/7054981#7054981
 * System properties are set on the Java operation line using the -Dpropertyname=value syntax.
 * They can also be added at runtime using System.setProperty(String key, String value) or via the various System.getProperties().load() methods.
 * To get a specific system property you can use System.getProperty(String key) or System.getProperty(String key, String def).
 * (Environment variables are set in the OS, e.g. in Linux export HOME=/Users/myusername or on Windows SET WINDIR=C:\Windows etc, and, unlike properties, may not be set at runtime.
 * To get a specific environment variable you can use System.getenv(String name).
 */
val propertiesFromSystem get(): Properties = System.getProperties()

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
    println("Hello, world")

    SpringApplicationBuilder(VlpApplication::class.java)
            //.initializers(beans {  })
            //
            /**
             * To support [KDesktop]
             */
            .headless(false)
            //.profiles()
            .run()


}

object KDesktop {

    @JvmStatic
    inline fun safeAction(action: Desktop.Action, block: Desktop.() -> Boolean) =
            if (Desktop.isDesktopSupported()) {
                with(Desktop.getDesktop()) {
                    if (isSupported(action)) {
                        block()
                    } else {
                        false
                    }
                }
            } else {
                false
            }

    @JvmStatic
    fun safeOpen(file: File) = safeAction(Desktop.Action.OPEN) {
        open(file)
        true
    }

    fun safeOpen(filePath: String) = safeOpen(File(filePath))

    /**
     * KDesktop.safeBrowse(URI("http://localhost:8080"))
     */
    @JvmStatic
    fun safeBrowse(uri: URI) = safeAction(Desktop.Action.BROWSE) {
        browse(uri)
        true
    }

}
