package ar.edu.unvime.tp1favoritos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class Tp1FavoritosApplication {

	public static void main(String[] args) {
		// Forzamos la zona horaria porque Windows reporta el alias viejo "America/Buenos_Aires" que Postgres 17 rechaza
		TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
		SpringApplication.run(Tp1FavoritosApplication.class, args);
	}

}
