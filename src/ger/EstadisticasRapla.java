package ger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            conexion.cerrar();
        } catch (SQLException ex) {
            Logger.getLogger(EstadisticasRapla.class.getName()).log(Level.SEVERE, null, ex);
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
    public ArrayList<Software> obtenerSoftwarePorMateria() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int obtenerCantidadAlumnosPorAnio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int obtenerCantidadAlumnosPorAnioPorMateria() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Horario> obtenerHorariosPorDia() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Horario> obtenerHorariosMasUsadosPorDia() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Horario> obtenerHorariosMenosUsadosPorDia() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obtenerCursosPorMateriaPorAnio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
