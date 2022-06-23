package com.pablohorst.booking.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Booking API
 *
 * @author Pablo Horst
 */
@SpringBootApplication(scanBasePackages = {"com.pablohorst.*"})
public class BookingApiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BookingApiApplication.class);
        app.setBanner((environment, sourceClass, out) -> out.println(""
                +
                "  ____  _   _                _     ____              _    _                  _    ____ ___ \n" +
                " |  _ \\| | | | ___  _ __ ___| |_  | __ )  ___   ___ | | _(_)_ __   __ _     / \\  |  _ \\_ _|\n" +
                " | |_) | |_| |/ _ \\| '__/ __| __| |  _ \\ / _ \\ / _ \\| |/ / | '_ \\ / _` |   / _ \\ | |_) | | \n" +
                " |  __/|  _  | (_) | |  \\__ \\ |_  | |_) | (_) | (_) |   <| | | | | (_| |  / ___ \\|  __/| | \n" +
                " |_|   |_| |_|\\___/|_|  |___/\\__| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, | /_/   \\_\\_|  |___|\n" +
                "==================================================================|___/====================\n" +
                "Application Version :: " + environment.getProperty("info.app.version") + "\n" +
                "Spring Boot Version :: " + SpringBootVersion.getVersion() + "\n" +
                "Java :: Vendor: " + environment.getProperty("java.vendor") +
                " - Server: " + environment.getProperty("java.vm.name") +
                " - Version: " + environment.getProperty("java.version") + "\n"));
        app.run(args);
    }
}
