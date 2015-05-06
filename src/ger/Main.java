package ger;

import interfaz.prueba;
import java.sql.Date;
import java.util.ArrayList;


/**
 *
 * @author Barrionuevo Diego
 */
public class Main {

    private static Conector conector;
    
    public static void main(String[] args) {
//      prueba ventana = new prueba();
//      ventana.setVisible(true);
        //conector = new Conector();
        //conector.consultarCantidadCursosPorMateriaPorAnio(2015);
        //conector.consultarHorasPorDia("2015-4-1","2015-5-30", Date.valueOf("2015-4-1"), Date.valueOf("2015-5-30"));
        //onector.consultarHorarioMasUsadoPorDia("2015-4-16", 2);
        //conector.consultarSoftwareCursosAnual(2015, "software1", "software2");
        //conector.consultarCantidadAlumnosAnual(2015);
        EstadisticasRapla estadisticas = new EstadisticasRapla();
//        ArrayList<Materia> listaMaterias = estadisticas.obtenerMateriasSistemas();
//        for (Materia materia : listaMaterias) 
//        {
//            System.out.println(materia.getNombreMateria());
//        }
//        for (Materia materia : estadisticas.obtenerSoftwarePorMateriaPorAnio(2015)) 
//        {
//            System.out.println(materia.getNombreMateria() + "Cantidad Alumnos: "+ estadisticas.obtenerCantidadAlumnosPorAnioPorMateria(materia, 2015));
//        }
//        for (Horario horario : estadisticas.obtenerHorariosPorDia(Date.valueOf("2015-4-1"), Date.valueOf("2015-5-30"))) 
//        {
//            System.out.println(horario.getFecha() + "    " + horario.getCantidadHoras());
//        }
        for (Materia materia : estadisticas.obtenerMateriasSistemasCompletas(2015)) 
        {
            System.out.println("Nombre materia: " + materia.getNombreMateria());
            System.out.println("Id materia: " + materia.getIdMateria());
            System.out.println("Software");
            for (Software software : materia.getSoftware()) 
            {
                System.out.println(software.getNombre());
            }
            System.out.println("Cursos:");
            for (Curso cursos : materia.getCurso()) 
            {
                System.out.println("Nombre Curso: " + cursos.getNombreCurso());
                System.out.println("Cantidad Alumnos: " + cursos.getCantidadAlumnos());
            }
        }
    }

}
