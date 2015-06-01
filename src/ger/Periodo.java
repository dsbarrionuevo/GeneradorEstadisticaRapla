/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ger;

import java.util.Date;


/**
 *
 * @author MatiasNB
 */
public class Periodo {

    private int idPeriodo;
    private String nombre;
    private Date comienzoPeriodo;
    private Date finPeriodo;

    /**
     * @return the idPeriodo
     */
    public int getIdPeriodo() {
        return idPeriodo;
    }

    /**
     * @param idPeriodo the idPeriodo to set
     */
    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the comienzoPeriodo
     */
    public Date getComienzoPeriodo() {
        return comienzoPeriodo;
    }

    /**
     * @param comienzoPeriodo the comienzoPeriodo to set
     */
    public void setComienzoPeriodo(Date comienzoPeriodo) {
        this.comienzoPeriodo = comienzoPeriodo;
    }

    /**
     * @return the finPeriodo
     */
    public Date getFinPeriodo() {
        return finPeriodo;
    }

    /**
     * @param finPeriodo the finPeriodo to set
     */
    public void setFinPeriodo(Date finPeriodo) {
        this.finPeriodo = finPeriodo;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
