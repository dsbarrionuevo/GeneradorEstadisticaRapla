package ger;

import interfaz.prueba;
import java.sql.Date;


/**
 *
 * @author Barrionuevo Diego
 */
public class Main {

    private static Conector conector;
    
    public static void main(String[] args) {
//      prueba ventana = new prueba();
//      ventana.setVisible(true);
        conector = new Conector();
        //conector.consultarCantidadCursosPorMateriaPorAnio(2015);
        //conector.consultarHorasPorDia("2015-4-1","2015-5-30", Date.valueOf("2015-4-1"), Date.valueOf("2015-5-30"));
        //onector.consultarHorarioMasUsadoPorDia("2015-4-16", 2);
        //conector.consultarSoftwareCursosAnual(2015, "software1", "software2");
        conector.consultarCantidadAlumnosAnual(2015);
    }

}
