/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MatiasNB
 */
public class Materia 
{
    private int idMateria;
    private ArrayList<String> software;
    private String nombreMateria;
    private int cantidadCursos;
    private Curso curso;

    public Materia()
    {
        this.software = new ArrayList<String>();
        this.curso = new Curso();
    }
    
    /**
     * @return the idMateria
     */
    public int getIdMateria() {
        return idMateria;
    }

    /**
     * @param idMateria the idMateria to set
     */
    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }


    /**
     * @return the nombreMateria
     */
    public String getNombreMateria() {
        return nombreMateria;
    }

    /**
     * @param nombreMateria the nombreMateria to set
     */
    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    /**
     * @return the software
     */
    public ArrayList<String> getSoftware() {
        return software;
    }

    /**
     * @param software the software to set
     */
    public void setSoftware(ArrayList<String> software) {
        this.software = software;
    }

    /**
     * @return the cantidadCursos
     */
    public int getCantidadCursos() {
        return cantidadCursos;
    }

    /**
     * @param cantidadCursos the cantidadCursos to set
     */
    public void setCantidadCursos(int cantidadCursos) {
        this.cantidadCursos = cantidadCursos;
    }

    /**
     * @return the curso
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
}
