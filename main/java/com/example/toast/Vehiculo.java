package com.example.toast;

/**
 * Clase modelo para representar un vehículo en el sistema de estacionamiento
 * Contiene todos los atributos necesarios para gestionar la información de los vehículos
 */
public class Vehiculo {

    // Atributos privados de la clase
    private int id;
    private String placa;
    private String propietario;
    private String telefono;
    private String tipoVehiculo;
    private String fechaIngreso;
    private String horaIngreso;

    // Constructor vacío
    public Vehiculo() {
    }

    // Constructor con parámetros (sin ID para nuevos registros)
    public Vehiculo(String placa, String propietario, String telefono,
                    String tipoVehiculo, String fechaIngreso, String horaIngreso) {
        this.placa = placa;
        this.propietario = propietario;
        this.telefono = telefono;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaIngreso = fechaIngreso;
        this.horaIngreso = horaIngreso;
    }

    // Constructor completo (con ID para registros existentes)
    public Vehiculo(int id, String placa, String propietario, String telefono,
                    String tipoVehiculo, String fechaIngreso, String horaIngreso) {
        this.id = id;
        this.placa = placa;
        this.propietario = propietario;
        this.telefono = telefono;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaIngreso = fechaIngreso;
        this.horaIngreso = horaIngreso;
    }

    // Métodos Getter y Setter para todos los atributos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    // Método toString para representación en cadena del objeto
    @Override
    public String toString() {
        return "Vehículo{" +
                "ID=" + id +
                ", Placa='" + placa + '\'' +
                ", Propietario='" + propietario + '\'' +
                ", Teléfono='" + telefono + '\'' +
                ", Tipo='" + tipoVehiculo + '\'' +
                ", Fecha='" + fechaIngreso + '\'' +
                ", Hora='" + horaIngreso + '\'' +
                '}';
    }

    // Método para validar si el vehículo tiene datos válidos
    public boolean esValido() {
        return placa != null && !placa.trim().isEmpty() &&
                propietario != null && !propietario.trim().isEmpty() &&
                tipoVehiculo != null && !tipoVehiculo.trim().isEmpty() &&
                fechaIngreso != null && !fechaIngreso.trim().isEmpty() &&
                horaIngreso != null && !horaIngreso.trim().isEmpty();
    }

    // Método para obtener información resumida del vehículo
    public String getInfoResumida() {
        return placa + " - " + propietario + " (" + tipoVehiculo + ")";
    }
}