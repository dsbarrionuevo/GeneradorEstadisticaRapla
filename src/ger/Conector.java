package ger;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

/**
 *
 * @author Barrionuevo Diego
 */
public class Conector {

    private Connection conexion;
    private Statement sentencia;
    private String baseDatos, usuario, clave;

    public Conector(String baseDatos, String usuario, String clave) {
        this.baseDatos = baseDatos;
        this.usuario = usuario;
        this.clave = clave;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Conector() {
        this("rapla", "admin", "admin");
    }

    public void conectar() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/" + baseDatos, usuario, clave);
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet ejecutarProcedimiento(String nombreProcedimiento) {
        try {
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery("CALL " + nombreProcedimiento);
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void cerrar() {
        try {
            this.conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarCantidadCursosPorMateriaPorAnio(int anio) {
        conectar();
        ResultSet resultado1, resultado2 = null;
        resultado1 = ejecutarProcedimiento("consultar_materias_sistemas();");
        try {
            while (resultado1.next()) {
                Materia materia = new Materia();
                
                materia.setIdMateria(resultado1.getInt(1));
                materia.setNombreMateria(resultado1.getString(4));
                
                //System.out.println(resultado.getInt(1));
                resultado2 = ejecutarProcedimiento("consultar_cantidad_cursos_por_materia_anio(" + materia.getIdMateria() + "," + anio + ");");
                resultado2.first();
                materia.setCantidadCursos(resultado2.getInt(1));
                if (materia.getCantidadCursos() != 0)
                {
                    System.out.println("La materia: " + materia.getNombreMateria() + ", posee " + materia.getCantidadCursos() + " cursos");
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultado1 != null) {
                    resultado1.close();
                }
                if (resultado2 != null) {
                    resultado1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }

   
    public void consultarHorasPorDia(String fechaDesde, String fechaHasta, java.util.Date fechaDesdeDate, java.util.Date fechaHastaDate)
    {
        ResultSet resultado1 = null;
        try 
        {
            conectar();
            Horario horario = new Horario();
            Matcher matcher = (Pattern.compile("(\\d{4}-\\d{1,2})-(\\d{1,2})")).matcher(fechaDesde);
            matcher.find();
            String restoFecha = matcher.group(1);
            int diaInicial = Integer.parseInt(matcher.group(2));
            Matcher matcher2 = (Pattern.compile("(\\d{4}-\\d{1,2})-(\\d{1,2})")).matcher(fechaHasta);
            matcher2.find();
            int diaFinal = Integer.parseInt(matcher2.group(2));

//            while (diaInicial <= diaFinal) 
//            {
//                resultado1 = ejecutarProcedimiento("consultar_total_horas_por_fecha_hora_inicio_fin('" + restoFecha + "-" + diaInicial + "', '08:00:00','23:00:00');");
//                resultado1.first();
//                horario.setDia(diaInicial);
//                horario.setCantidadHoras((double)resultado1.getInt(1));
//                System.out.println("DIA: " + horario.getDia() + " HORAS: " + horario.getCantidadHoras());
//                diaInicial++;
//            }
            String fecha = fechaDesdeDate.toString();
            while(fechaDesdeDate.before(fechaHastaDate))
            {

                resultado1 = ejecutarProcedimiento("consultar_total_horas_por_fecha_hora_inicio_fin('" + fecha + "', '08:00:00','23:00:00');");
                resultado1.first();
                
                horario.setCantidadHoras((double)resultado1.getInt(1));
                System.out.println("DIA: " + fecha.toString() + " HORAS: " + horario.getCantidadHoras());
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(fecha));
                c.add(Calendar.DATE, 1);  // number of days to add
                fecha = sdf.format(c.getTime()); 
                
                Calendar c2 = Calendar.getInstance(); 
                c2.setTime(fechaDesdeDate); 
                c2.add(Calendar.DATE, 1);
                fechaDesdeDate = c.getTime();
            }

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally 
        {
            try 
            {
                if (resultado1 != null) 
                {
                    resultado1.close();
                }
            } catch (SQLException ex) 
            {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }

    public void consultarHorarioMasUsadoPorDia(String fecha, int rango) {
        ResultSet resultado1 = null;
        ArrayList<Horario> listaCursosEnRango = new ArrayList<Horario>();
        int horaInicio = 8;
        int i = 0;
        try {
            conectar();
            while (horaInicio < 24) {
                int horaFin = horaInicio + rango;
                String horaInicioString, horaFinString;
                horaInicioString = ((String.valueOf(horaInicio).length() == 1) ? ("0" + horaInicio) : horaInicio + "") + ":00:00";
                horaFinString = ((String.valueOf(horaFin).length() == 1) ? ("0" + horaFin) : horaFin + "") + ":00:00";
                //resultado1 = ejecutarProcedimiento("consultar_cantidad_cursos_por_rango_horario_fecha('" + fecha + "', '" + horaInicioString + "','" + horaFinString + "');");

                sentencia = conexion.createStatement();
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
                resultado1 = sentencia.executeQuery(consulta);

                resultado1.first();
                Horario horas = new Horario();
                horas.setHoraInicio(horaInicioString);
                horas.setHoraFin(horaFinString);
                horas.setCantidadHoras(resultado1.getDouble(1));
                
                listaCursosEnRango.add(horas);
                //System.out.println("Cantidad de cursos para la fecha " + fecha + " desde las " + horaInicioString + " y las " + horaFinString + ": " + resultado1.getInt(1));
                i++;
                horaInicio += rango;
            }
            
            ordenarMenorMayor(listaCursosEnRango);
            
            ordenarMayorMenor(listaCursosEnRango);           
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultado1 != null) {
                    resultado1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
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

    private void ordenarMenorMayor(ArrayList<Horario> listaCursosEnRango) 
    {
        Collections.sort(listaCursosEnRango, new Comparator<Horario>()
        {
            @Override
            public int compare(Horario p1, Horario p2)
            {
                return new Double(p1.getCantidadHoras()).compareTo(new Double(p2.getCantidadHoras())); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        System.out.println("Menor a Mayor");
        for (Horario horario : listaCursosEnRango)
        {
            System.out.println(horario.getHoraInicio() + " " + horario.getHoraFin() + " " + horario.getCantidadHoras().toString());
        }
    }
    
    public void consultarSoftwareCursosAnual(int anio, String software, String software2)
    {
        conectar();
        ResultSet resultado1, resultado2 = null, resultado3 = null;
        ArrayList<Materia> listaSoftwareMaterias = new ArrayList<Materia>();
        resultado1 = ejecutarProcedimiento("consultar_materias_sistemas();");
        try 
        {
            while (resultado1.next()) 
            {
                Materia materia = new Materia();
                List<String> listaSoftware = materia.getSoftware();                    
                materia.setIdMateria(resultado1.getInt(1));
                materia.setNombreMateria(resultado1.getString(4));
                resultado2 = ejecutarProcedimiento("consultar_software_cursos_anual(" + materia.getIdMateria() + "," + anio + ",'" + software + "');");
                while(resultado2.next())
                {
                    listaSoftware.add(resultado2.getString(6));
                }
                
                resultado3 = ejecutarProcedimiento("consultar_software_cursos_anual(" + materia.getIdMateria() + "," + anio + ",'" + software2 + "');");
                while(resultado2.next())
                {
                    listaSoftware.add(resultado3.getString(6));
                }
                listaSoftwareMaterias.add(materia);
            }
            
            for (Materia materia : listaSoftwareMaterias) 
            {
                if (materia.getSoftware().size() != 0) 
                {
                    System.out.println("La materia: " + materia.getNombreMateria() + ", posee el siguiente SW: " + materia.getSoftware().toString());
                }
                
            }
        } catch (SQLException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally 
        {
            try 
            {
                if (resultado1 != null) 
                {
                    resultado1.close();
                }
                if (resultado2 != null) {
                    resultado1.close();
                }
                if (resultado3 != null) {
                    resultado1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }
    
    public void consultarCantidadAlumnosAnual(int anio)
    {
        conectar();
        ResultSet resultado1, resultado2 = null;
        ArrayList<Materia> listaSoftwareMaterias = new ArrayList<Materia>();
        resultado1 = ejecutarProcedimiento("consultar_materias_sistemas();");
        try 
        {
            while (resultado1.next()) 
            {
                Materia materia = new Materia();                   
                materia.setIdMateria(resultado1.getInt(1));
                materia.setNombreMateria(resultado1.getString(4));
                resultado2 = ejecutarProcedimiento("consultar_cantidad_alumnos_curso_y_aula_anual(" + materia.getIdMateria() + "," + anio + ");");
                while(resultado2.next())
                {
                    materia.getCurso().setCantidadAlumnos(resultado2.getInt(8));
                    materia.getCurso().setNombreCurso(resultado2.getString(6));
                }
                
                listaSoftwareMaterias.add(materia);
            }
            
            for (Materia materia : listaSoftwareMaterias) 
            {
                if (materia.getSoftware().size() != 0) 
                {
                    System.out.println("La materia: " + materia.getNombreMateria() + ", posee el siguiente SW: " + materia.getSoftware().toString());
                }
                
            }
        } catch (SQLException ex) 
        {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally 
        {
            try 
            {
                if (resultado1 != null) 
                {
                    resultado1.close();
                }
                if (resultado2 != null) {
                    resultado1.close();
                }
            
            } catch (SQLException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cerrar();
        }
    }
}
