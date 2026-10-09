package ar.edu.unvime.tp1favoritos.error;

public class ServicioExternoException extends RuntimeException {
    public ServicioExternoException(String mensaje) {
        super(mensaje);
    }
}
