package com.fsteni58.tennis;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TennisApplication {

    public static void main(String[] args) {
        // Lee el archivo .env (que NUNCA se sube a GitHub) y lo mete como
        // variables de entorno, para que application.properties las pueda usar.
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(TennisApplication.class, args);
    }
}
