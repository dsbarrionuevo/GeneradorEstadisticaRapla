package ger;

import java.sql.Date;


/**
 *
 * @author Barrionuevo Diego
 */
public class Main {

    private static Conector conector;
    
    public static void main(String[] args) {
        conector = new Conector();
        //conector.consultarCantidadCursosPorMateriaPorAnio(2015);
        conector.consultarHorasPorDia("2015-4-1","2015-5-30", Date.valueOf("2015-4-1"), Date.valueOf("2015-5-30"));
        //conector.consultarHorarioMasUsadoPorDia("2015-4-16", 2);
        //conector.consultarSoftwareCursosAnual(2015, "software1");
    }

}
