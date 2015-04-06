package ger;

/**
 *
 * @author Barrionuevo Diego
 */
public class Main {

    private static Conector conector;
    
    public static void main(String[] args) {
        conector = new Conector();
        conector.consultarCantidadCursosPorMateriaPorAnio(2015);
    }

}
