package ger;

import java.util.ArrayList;

/**
 *
 * @author Barrionuevo Diego
 */
public interface IEstadisticas {

    public ArrayList<Aula> obtenerAulas();
    
    public ArrayList<Software> obtenerSoftwarePorAula();
    
    public ArrayList<Software> obtenerSoftwarePorMateria();
    
    public int obtenerCantidadAlumnosPorAnio();
    
    public int obtenerCantidadAlumnosPorAnioPorMateria();
    
    public ArrayList<Horario> obtenerHorariosPorDia();//este es el que consulta por rango de horas
    
    public ArrayList<Horario> obtenerHorariosMasUsadosPorDia();
    
    public ArrayList<Horario> obtenerHorariosMenosUsadosPorDia();
    
    public void obtenerCursosPorMateriaPorAnio();
    
}
