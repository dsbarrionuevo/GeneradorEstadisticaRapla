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
    
}
