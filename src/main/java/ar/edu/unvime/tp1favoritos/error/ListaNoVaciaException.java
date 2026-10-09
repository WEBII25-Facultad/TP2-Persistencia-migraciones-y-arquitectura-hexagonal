package ar.edu.unvime.tp1favoritos.error;

public class ListaNoVaciaException extends RuntimeException {
    public ListaNoVaciaException(String message) {
        super(message);
    }
}
