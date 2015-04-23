package ger;

/**
 *
 * @author Barrionuevo Diego
 */
public class Aula {

    private String nombre;
    private Software software;
    private Hardware hardware;
    private int capacidadEstimada;//alumnos
    private int cantidadEquipos;

    public Aula() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public int getCapacidadEstimada() {
        return capacidadEstimada;
    }

    public void setCapacidadEstimada(int capacidadEstimada) {
        this.capacidadEstimada = capacidadEstimada;
    }

    public int getCantidadEquipos() {
        return cantidadEquipos;
    }

    public void setCantidadEquipos(int cantidadEquipos) {
        this.cantidadEquipos = cantidadEquipos;
    }

}
