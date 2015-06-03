package ger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        } catch (SQLException ex) {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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
    public ArrayList<Materia> obtenerSoftwarePorMateriaPorAnio(int anio) {
        ArrayList<Materia> materias = obtenerMateriasSistemas();
        for (int i = 0; i < materias.size(); i++) {
            try {
                ArrayList<Software> listaSoftware = new ArrayList<Software>();
                conexion.conectar();
                ResultSet resultado1 = conexion.ejecutarProcedimiento("consultar_software_cursos_anual(" + materias.get(i).getIdMateria() + "," + anio + ",'software1');");
                while (resultado1.next()) {
                    listaSoftware.add(new Software(resultado1.getString("software")));
                }

                ResultSet resultado2 = conexion.ejecutarProcedimiento("consultar_software_cursos_anual(" + materias.get(i).getIdMateria() + "," + anio + ",'software2');");
                while (resultado2.next()) {
                    listaSoftware.add(new Software(resultado2.getString("software")));
                }
                materias.get(i).setSoftware(listaSoftware);
            } catch (SQLException ex) {
                Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                conexion.cerrar();
            }
        }
        return materias;
    }

    public ArrayList<Software> obtenerSoftwarePorMateriaPorAnio(Materia materia, int anio) {
        ArrayList<Software> listaSoftware = new ArrayList<Software>();
        try {

            conexion.conectar();
            ResultSet resultado1 = conexion.ejecutarProcedimiento("consultar_software_cursos_anual(" + materia.getIdMateria() + "," + anio + ",'software1');");
            while (resultado1.next()) {
                listaSoftware.add(new Software(resultado1.getString("software")));
            }

            ResultSet resultado2 = conexion.ejecutarProcedimiento("consultar_software_cursos_anual(" + materia.getIdMateria() + "," + anio + ",'software2');");
            while (resultado2.next()) {
                listaSoftware.add(new Software(resultado2.getString("software")));
            }
            materia.setSoftware(listaSoftware);
        } catch (SQLException ex) {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.cerrar();
        }
        return listaSoftware;
    }

    @Override
    public int obtenerCantidadAlumnosPorAnio(int anio) {
        int cantidadAlumnosAnual = 0;
        //Por cada una d ela s materias de sistemas, ejecuto el procedimiento y mantengo un acumulador que devuelvo
        for (Materia materia : obtenerMateriasSistemas()) {
            cantidadAlumnosAnual += obtenerCantidadAlumnosPorAnioPorMateria(materia, anio);
        }
        return cantidadAlumnosAnual;
    }
    
    private int obtenerCantidadAlumnosCursoEspecifico(Curso curso, Materia materia, int anio)
    {
        try 
        {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_cantidad_alumnos_curso_especifico(" + materia.getIdMateria() + ",'" + curso.getNombreCurso() + "'," + anio + ");");
            if (resultado.next()) 
            {
                return resultado.getInt("cantidadAlumnos");
            }
            else
            {
                return -1;
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } 
        finally 
        {
            conexion.cerrar();
        }
    }

    @Override
    public int obtenerCantidadAlumnosPorAnioPorMateria(Materia materia, int anio) {
        int cantidadAlumnos = 0;
        try {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_cantidad_alumnos_curso_y_aula_anual(" + materia.getIdMateria() + "," + anio + ");");
            while (resultado.next()) {
                cantidadAlumnos += resultado.getInt("cantidadAlumnos");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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
            while (fechaDesdeDate.before(fechaHastaDate) || fechaDesdeDate.equals(fechaHastaDate)) 
            {
                Horario horario = new Horario();
                ResultSet resultado = conexion.ejecutarProcedimiento("consultar_total_horas_por_fecha_hora_inicio_fin('" + fecha + "', '08:00:00','23:00:00');");
                resultado.first();

                horario.setCantidadHoras((double) resultado.getInt("horasTotales"));
                horario.setFecha(fecha);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();

                //Le agrega un dia mas a la fecha para que avance en el SQL
                fecha = agregarDiaConsultaBaseDatos(c, sdf, fecha);

                //agrega un dia mas pero a la fecha que se usa en el while
                fechaDesdeDate = agregarDiaDate(fechaDesdeDate, c);

                listaHorarios.add(horario);
            }

        } catch (SQLException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally 
        {
            conexion.cerrar();
        }
        return listaHorarios;
    }

    private String agregarDiaConsultaBaseDatos(Calendar c, SimpleDateFormat sdf, String fecha) throws ParseException {
        c.setTime(sdf.parse(fecha));
        c.add(Calendar.DATE, 1);  // number of days to add
        fecha = sdf.format(c.getTime());
        return fecha;
    }

    private Date agregarDiaDate(Date fechaDesdeDate, Calendar c) {
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaDesdeDate);
        c2.add(Calendar.DATE, 1);
        fechaDesdeDate = c.getTime();
        return fechaDesdeDate;
    }

    @Override
    public ArrayList<Horario> obtenerHorariosMasUsadosPorDia(String fecha, int rango) 
    {
        ArrayList<Horario> listaHorarios = new ArrayList<Horario>();
        int horaInicio = 8;
        int i = 0;
        try 
        {
            conexion.conectar();
            while (horaInicio < 23) 
            {
                int horaFin = horaInicio + rango;
                String horaInicioString, horaFinString;
                horaInicioString = ((String.valueOf(horaInicio).length() == 1) ? ("0" + horaInicio) : horaInicio + "") + ":00:00";
                horaFinString = ((String.valueOf(horaFin).length() == 1) ? ("0" + horaFin) : horaFin + "") + ":00:00";
                //resultado1 = ejecutarProcedimiento("consultar_cantidad_cursos_por_rango_horario_fecha('" + fecha + "', '" + horaInicioString + "','" + horaFinString + "');");

                conexion.setSentencia(conexion.getConexion().createStatement());
                /*esta consulta busca todos los cursos (o materias) que estan dentro del rango de horas:
                esto puede ser: que justo termine en ese rango, comienzo en ese rango, comience y termine en ese rango,
                o que haya comenzado antes del inicio del rango y termine desp del final del rango.
                Graficamente son la cantidad de cuadraditos que muestra el rapla para tal dia en el horario indicado por el rango.
                Esta hardcodeada en el codigo por el hhecho de que al llamarla como procedimiento tira error con el ecndoe por el hecho
                de comprar fechas con date_format.
                */
                String consulta = "SELECT count(*) \n"
                        + "FROM rapla.appointment AS a \n"
                        + "INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id \n"
                        + "JOIN allocation al ON al.APPOINTMENT_ID = a.ID\n"
                        + "JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE\n"
                        + "JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID\n"
                        + "WHERE rav.ATTRIBUTE_KEY = \"name\" \n"
                        + "AND eav.ATTRIBUTE_KEY = \"especialidad\" \n"
                        + "AND '" + fecha + "' BETWEEN a.APPOINTMENT_START AND a.REPETITION_END \n"
                        + "AND (\n"
                        + "\n"
                        + "( DATE_FORMAT(a.APPOINTMENT_START,'%T')>='" + horaInicioString + "' AND DATE_FORMAT(a.APPOINTMENT_START,'%T')<'" + horaFinString + "' )\n"
                        + "OR\n"
                        + "( DATE_FORMAT(a.APPOINTMENT_END,'%T')>'" + horaInicioString + "' AND DATE_FORMAT(a.APPOINTMENT_END,'%T')<='" + horaFinString + "' )\n"
                        + "OR\n"
                        + "( DATE_FORMAT(a.APPOINTMENT_START,'%T')<'" + horaInicioString + "' AND DATE_FORMAT(a.APPOINTMENT_END,'%T')>'" + horaFinString + "' )\n"
                        + "\n"
                        + ")"
                        + ""
                        + "AND DAYNAME(a.APPOINTMENT_START) = DAYNAME('" + fecha + "')\n"
                        + "AND a.ID NOT IN(\n"
                        + "	SELECT ae.APPOINTMENT_ID \n"
                        + "	FROM appointment_exception ae \n"
                        + "	WHERE DATE(ae.EXCEPTION_DATE) = '" + fecha + "'\n"
                        + ")";
                ResultSet resultado = conexion.getSentencia().executeQuery(consulta);

                resultado.first();
                
                Horario horas = new Horario();
                horas.setHoraInicio(horaInicioString);
                horas.setHoraFin(horaFinString);
                horas.setCantidadHoras(resultado.getDouble(1));
                horas.setFecha(fecha);
                
                listaHorarios.add(horas);
                //System.out.println("Cantidad de cursos para la fecha " + fecha + " desde las " + horaInicioString + " y las " + horaFinString + ": " + resultado1.getInt(1));
                i++;
                horaInicio += rango;
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
        return listaHorarios;
    }

        private void ordenarMayorMenor(ArrayList<Horario> listaCursosEnRango) {
        Collections.sort(listaCursosEnRango, new Comparator<Horario>()
        {
            @Override
            public int compare(Horario p1, Horario p2)
            {
                return new Double(p2.getCantidadHoras()).compareTo(new Double(p1.getCantidadHoras())); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        System.out.println("\nMayor a Menor");
        for (Horario horario : listaCursosEnRango)
        {
            System.out.println(horario.getHoraInicio() + " " + horario.getHoraFin() + " " + horario.getCantidadHoras().toString());
        }
    }
        
    @Override
    public ArrayList<Horario> obtenerHorariosMenosUsadosPorDia(String fecha, int rango) {
        ArrayList<Horario> listaHorarios = new ArrayList<Horario>();
        return listaHorarios;
    }

    @Override
    public ArrayList<Curso> obtenerCursosPorMateriaPorAnio(Materia materia, int anio) {
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_cursos_por_materia_por_anio(" + materia.getIdMateria() + "," + anio + ")");
            while (resultado.next()) 
            {
                Curso curso = new Curso();
                curso.setNombreCurso(resultado.getString("curso"));
                curso.setCantidadAlumnos(obtenerCantidadAlumnosCursoEspecifico(curso, materia, anio));
                cursos.add(curso);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cursos;
    }

    @Override
    public ArrayList<Materia> obtenerMateriasSistemas() {
        ArrayList<Materia> materias = new ArrayList<>();
        try {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_materias_sistemas()");
            while (resultado.next()) {
                Materia materia = new Materia();
                materia.setIdMateria(resultado.getInt("ID"));
                materia.setNombreMateria(resultado.getString("LABEL"));
                materias.add(materia);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.cerrar();
        }
        return materias;
    }

    @Override
    public ArrayList<Materia> obtenerMateriasSistemasCompletas(int anio) {
        ArrayList<Materia> materias = obtenerMateriasSistemas();
        for (Materia materia : materias) 
        {
            //Obtiene los sw de las materias
            materia.setSoftware(obtenerSoftwarePorMateriaPorAnio(materia, anio));
            //seteo los cursos que tiene la materia junto con la cantida de alumnos que tiene
            materia.setCurso(obtenerCursosPorMateriaPorAnio(materia, anio));
        }
        return materias;
    }

    @Override
    public ArrayList<ArrayList<Horario>> obtenerRangoHorariosFechas(Date fechaDesdeDate, Date fechaHastaDate, int rango) 
    {
        ArrayList<ArrayList<Horario>> arraryArrayHorarios = new ArrayList<ArrayList<Horario>>();
        try 
        {
            String fecha = fechaDesdeDate.toString();
            while (fechaDesdeDate.before(fechaHastaDate)) 
            { 
                arraryArrayHorarios.add(obtenerHorariosMasUsadosPorDia(fecha, rango));
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                
                //Le agrega un dia mas a la fecha para que avance en el SQL
                fecha = agregarDiaConsultaBaseDatos(c, sdf, fecha);
                
                //agrega un dia mas pero a la fecha que se usa en el while
                fechaDesdeDate = agregarDiaDate(fechaDesdeDate, c);
            }            
        } 
        catch (Exception e) 
        {
        }

        return arraryArrayHorarios;
    }

    @Override
    public Horario obtenerPromedioHorasUnSoloDiaAño(String dia, int anio) 
    {
        Horario horario = new Horario();
        try {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_total_horas_por_dia_anio('" + dia + "'," + anio + ")");
            while (resultado.next()) {     
            horario.setFecha(dia);
            horario.setCantidadDiasFecha(resultado.getInt("cantidadDias"));
            //horario.setCantidadHoras(resultado.getDouble("horasTotalesDia"));
            String h = resultado.getString("horasTotalesDia");
            }
 

        } 
        catch (Exception e) {
        }
        return horario;
    }
    
    @Override
    public ArrayList<HorasTotales> obtenerHorasTotalesPorDia(String dia, Periodo periodo)
    {
        ArrayList<HorasTotales> horas = new ArrayList<>();
        try {
            conexion.conectar();
            String inicioPeriodo = ConvertirFechaMySQL(periodo.getComienzoPeriodo());
            String finPeriodo = ConvertirFechaMySQL(periodo.getFinPeriodo());

            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_total_horas_por_dia('" + dia + "', YEAR('"+ inicioPeriodo +"'), MONTH('"+ inicioPeriodo + "'), MONTH('" + finPeriodo + "'))");
            while (resultado.next()) {
                HorasTotales horasTotalesDia = new HorasTotales();
                
                horasTotalesDia.setCantidadDias(resultado.getInt("cantidadDias")); 
                horasTotalesDia.setHorasTotales(resultado.getLong("horasTotalesDia")); 
                
                int day = (int)TimeUnit.SECONDS.toDays(horasTotalesDia.getHorasTotales());    
                long hours = TimeUnit.SECONDS.toHours(horasTotalesDia.getHorasTotales());
                long minute = TimeUnit.SECONDS.toMinutes(horasTotalesDia.getHorasTotales()) - (TimeUnit.SECONDS.toHours(horasTotalesDia.getHorasTotales())* 60);
                long second = TimeUnit.SECONDS.toSeconds(horasTotalesDia.getHorasTotales()) - (TimeUnit.SECONDS.toMinutes(horasTotalesDia.getHorasTotales()) *60);
                
                
                horas.add(horasTotalesDia);
            }
        } 
        catch (Exception e) {
        }
        return horas;
    }

    private String ConvertirFechaMySQL(Date fecha) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(fecha.toString()));
        return sdf.format(c.getTime());
    }

    @Override
    public ArrayList<Periodo> obtenerPeriodos() {
        ArrayList<Periodo> listaPeriodos = new ArrayList<>();
        try {
            conexion.conectar();
            ResultSet resultado = conexion.ejecutarProcedimiento("consultar_periodos()");
            while (resultado.next()) {
                Periodo periodo = new Periodo();
                periodo.setIdPeriodo(resultado.getInt("ID"));
                periodo.setNombre(resultado.getString("NAME"));
                periodo.setComienzoPeriodo(resultado.getDate("PERIOD_START"));
                periodo.setFinPeriodo(resultado.getDate("PERIOD_END"));
                listaPeriodos.add(periodo);
            }
        } catch (Exception e) {
        }
        return listaPeriodos;
    }

}
