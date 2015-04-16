package ger;


/**
 *
 * @author Barrionuevo Diego
 */
public class Main {

    private static Conector conector;
    
    public static void main(String[] args) {
        conector = new Conector();
        //conector.consultarCantidadCursosPorMateriaPorAnio(2015);
        //conector.consultarHorasPorDia("2015-4-6","2015-4-10");

        conector.consultarHorarioMasUsadoPorDia("2015-4-6", 2);
    }

}
