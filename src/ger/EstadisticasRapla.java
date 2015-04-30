package ger;

import com.sun.javafx.geom.Matrix3f;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Barrionuevo Diego
 */
public class EstadisticasRapla implements IEstadisticas {

    private Conexion conexion;

    public EstadisticasRapla() {
        this.conexion = new Conexion();
    }

    @Override
    public ArrayList<Aula> obtenerAulas() {
        ArrayList<Aula> aulas = new ArrayList<Aula>();
        try {
            conexion.conectar();
            String nombresAulas[] = new String[]{"A1", "A2", "A3", "A4", "A5", "A6", "A7"};
            for (int i = 0; i < nombresAulas.length; i++) {
                ResultSet resultado = conexion.ejecutarProcedimiento("consultar_aula_por_nombre('" + nombresAulas[i] + "')");
                Aula aula = new Aula();
                while (resultado.next()) {
                    if (resultado.getString("attribute_key").equalsIgnoreCase("name")) {
                        aula.setNombre(resultado.getString("attribute_value"));
                    } else if (resultado.getString("attribute_key").equalsIgnoreCase("a1")) {
                        aula.setCantidadEquipos(resultado.getInt("attribute_value"));
                    } else if (resultado.getString("attribute_key").equalsIgnoreCase("a4")) {
                        aula.setCapacidadEstimada(resultado.getInt("attribute_value"));
                    } else if (resultado.getString("attribute_key").equalsIgnoreCase("a3")) {
                        aula.setHardware(new Hardware(resultado.getString("attribute_value")));
                    } else if (resultado.getString("attribute_key").equalsIgnoreCase("a2")) {
                        aula.setSoftware(new Software(resultado.getString("attribute_value")));
                    } else if (resultado.getString("attribute_key").equalsIgnoreCase("color")) {
                        //...
                    }
                }
                aulas.add(aula);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            conexion.cerrar();
        }
        return aulas;
    }

    @Override
    public ArrayList<Software> obtenerSoftwarePorAula() {
        ArrayList<Software> software = new ArrayList<Software>();
        ArrayList<Aula> aulas = obtenerAulas();
        for (int i = 0; i < aulas.size(); i++) {
            software.add(aulas.get(i).getSoftware());
        }
        return software;
    }

    @Override
    public ArrayList<Materia> obtenerSoftwarePorMateriaPorAnio(int anio) 
    {               
        ArrayList<Materia> materias = obtenerMateriasSistemas();
        for (int i = 0; i < materias.size(); i++) 
        {
            try 
            {
                ArrayList<Software> listaSoftware = new ArrayList<Software>();
                conexion.conectar();
                ResultSet resultado1 = conexion.ejecutarProcedimiento("consultar_software_cursos_anual(" + materias.get(i).getIdMateria() + "," + anio + ",'software1');");
                while (resultado1.next()) 
                {
                    listaSoftware.add(new Software(resultado1.getString("software")));
                }
                
                ResultSet resultado2 = conexion.ejecutarProcedimiento("consultar_software_cursos_anual(" + materias.get(i).getIdMateria() + "," + anio + ",'software2');");
                while (resultado2.next()) 
                {
                    listaSoftware.add(new Software(resultado2.getString("software")));
                }
                materias.get(i).setSoftware(listaSoftware);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                conexion.cerrar();
            }
        }
        return materias;
    }

    @Override
    public int obtenerCantidadAlumnosPorAnio(int anio) 
    {
        int cantidadAlumnosAnual = 0;
        //Por cada una d ela s materias de sistemas, ejecuto el procedimiento y mantengo un acumulador que devuelvo
        for (Materia materia : obtenerMateriasSistemas()) 
        {
            cantidadAlumnosAnual += obtenerCantidadAlumnosPorAnioPorMateria(materia, anio);
        }
        return cantidadAlumnosAnual;
    }

    @Override
    public int obtenerCantidadAlumnosPorAnioPorMateria(Materia materia, int anio) 
    {
        int cantidadAlumnos = 0;
        try 
        {
            conexion.conectar();
            ResultSet resultado =  conexion.ejecutarProcedimiento("consultar_cantidad_alumnos_curso_y_aula_anual("+ materia.getIdMateria() + "," + anio + ");");
            while (resultado.next()) 
            {               
                cantidadAlumnos = resultado. getInt("cantidadAlumnos");
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            conexion.cerrar();
        }   
        return cantidadAlumnos;
    }

    @Override
    public ArrayList<Horario> obtenerHorariosPorDia(java.util.Date fechaDesdeDate, java.util.Date fechaHastaDate) 
    {
        ArrayList<Horario> listaHorarios = new ArrayList<Horario>();
        try 
        {
            conexion.conectar();            
            String fecha = fechaDesdeDate.toString();
            while(fechaDesdeDate.before(fechaHastaDate))
            {
                Horario horario = new Horario();
                ResultSet resultado = conexion.ejecutarProcedimiento("consultar_total_horas_por_fecha_hora_inicio_fin('" + fecha + "', '08:00:00','23:00:00');");
                resultado.first();
                
                horario.setCantidadHoras((double)resultado.getInt("horasTotales"));
                horario.setFecha(fecha);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                
                //Le agrega un dia mas a la fecha para que avance en el SQL
                fecha = agregarDiaConsultaBaseDatos(c, sdf, fecha); 
                
                //agrega un dia mas pero a la fecha que se usa en el while
                fechaDesdeDate = agregarDiaDate(fechaDesdeDate, c);
                
                
                listaHorarios.add(horario);
            }

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally 
        {
            conexion.cerrar();
        }
        return listaHorarios;
    }

    private String agregarDiaConsultaBaseDatos(Calendar c, SimpleDateFormat sdf, String fecha) throws ParseException 
    {
        c.setTime(sdf.parse(fecha));
        c.add(Calendar.DATE, 1);  // number of days to add
        fecha = sdf.format(c.getTime());
        return fecha;
    }

    private Date agregarDiaDate(Date fechaDesdeDate, Calendar c) 
    {
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaDesdeDate);
        c2.add(Calendar.DATE, 1);
        fechaDesdeDate = c.getTime();
        return fechaDesdeDate;
    }
    
    @Override
    public ArrayList<Horario> obtenerHorariosMasUsadosPorDia(String fecha, int rango) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Horario> obtenerHorariosMenosUsadosPorDia(String fecha, int rango) 
    {
        ArrayList<Horario> listaHorarios = new ArrayList<Horario>();
        return listaHorarios;
    }

    @Override
    public void obtenerCursosPorMateriaPorAnio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Materia> obtenerMateriasSistemas() 
    {
        ArrayList<Materia> materias = new ArrayList<>();
        try 
        {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_materias_sistemas()");
            while (resultado.next()) 
            {                
                Materia materia = new Materia();
                materia.setIdMateria(resultado.getInt("ID"));
                materia.setNombreMateria(resultado.getString("LABEL"));
                materias.add(materia);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            conexion.cerrar();
        }
        
        return materias;
    }

}
