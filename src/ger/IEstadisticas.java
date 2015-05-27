package ger;

import java.util.ArrayList;

/**
 *
 * @author Barrionuevo Diego
 */
public interface IEstadisticas {

    public ArrayList<Aula> obtenerAulas();
    
    public ArrayList<Software> obtenerSoftwarePorAula();
    
    //Me devuelve un array de materias, dentro de las cuales estan cargados los Software que usan
    //La otra es hacer que devuelva un array de software, pero tenemos que pasarle por parametro la materia.
    public ArrayList<Materia> obtenerSoftwarePorMateriaPorAnio(int anio);
    
    public int obtenerCantidadAlumnosPorAnio(int anio);
    
    //Me devuelve para la materia pasada por parametro, junto con el año, la cantidad de alumnos que tiene cargado
    public int obtenerCantidadAlumnosPorAnioPorMateria(Materia materia, int anio);
    
    public ArrayList<Horario> obtenerHorariosPorDia(java.util.Date fechaDesdeDate, java.util.Date fechaHastaDate);
    
    public ArrayList<Horario> obtenerHorariosMasUsadosPorDia(String fecha, int rango);
    
    public ArrayList<Horario> obtenerHorariosMenosUsadosPorDia(String fecha, int rango);
    
    public ArrayList<Curso> obtenerCursosPorMateriaPorAnio(Materia materia, int anio);
    
    //Obtiene una lista de materias, pero unicamente dentro de a carpeta de sistemas.
    public ArrayList<Materia> obtenerMateriasSistemas();
    
    public ArrayList<Materia> obtenerMateriasSistemasCompletas(int anio);
 
    public Horario obtenerPromedioHorasUnSoloDiaAño(String dia, int anio);
    
    //Obtiene los periodos que existen en la BD
    public ArrayList<Periodo> obtenerPeriodos();
    
    //Obtiene las horas totales de un dia en especifico en el cual es utilizado el laboratorio. Solo Devuelve String de horasTotales
    public String obtenerHorasTotalesPorDia(String dia, Periodo periodo);
    
    public ArrayList<ArrayList<Horario>> obtenerRangoHorariosFechas(java.util.Date fechaDesdeDate, java.util.Date fechaHastaDate, int rango);
}
