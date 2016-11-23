package com.itosoftware.inderandroid.Api.Pqr;

/**
 * Created by itofelipeparra on 23/06/16.
 */
public class Pqr {

    private String id;
    private String numero_proceso;
    private String numero_solicitud;
    private String identificacion;
    private String estado;
    private String funcionario_responsable;
    private String fecha_registro;
    private String fecha_estimada_respuesta;
    private String fecha_respuesta;
    private String documento;

    public Pqr(String id, String numero_proceso, String numero_solicitud, String identificacion, String estado, String funcionario_responsable, String fecha_registro, String fecha_estimada_respuesta, String fecha_respuesta, String documento) {
        this.id = id;
        this.numero_proceso = numero_proceso;
        this.numero_solicitud = numero_solicitud;
        this.identificacion = identificacion;
        this.estado = estado;
        this.funcionario_responsable = funcionario_responsable;
        this.fecha_registro = fecha_registro;
        this.fecha_estimada_respuesta = fecha_estimada_respuesta;
        this.fecha_respuesta = fecha_respuesta;
        this.documento = documento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero_proceso() {
        return !numero_proceso.equals("null") && !numero_proceso.equals("") ?numero_proceso:"";
    }

    public void setNumero_proceso(String numero_proceso) {
        this.numero_proceso = numero_proceso;
    }

    public String getNumero_solicitud() {
        return !numero_solicitud.equals("null")&& !numero_solicitud.equals("") ?numero_solicitud:"";
    }

    public void setNumero_solicitud(String numero_solicitud) {
        this.numero_solicitud = numero_solicitud;
    }

    public String getIdentificacion() {
        return !identificacion.equals("null")&& !identificacion.equals("") ?identificacion:"";
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getEstado() {
        return !estado.equals("null")&& !estado.equals("") ?estado:"*";
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFuncionario_responsable() {
        return !funcionario_responsable.equals("null")&& !funcionario_responsable.equals("") ?funcionario_responsable:"";
    }

    public void setFuncionario_responsable(String funcionario_responsable) {
        this.funcionario_responsable = funcionario_responsable;
    }

    public String getFecha_registro() {
        return !fecha_registro.equals("null") && !fecha_registro.equals("")?fecha_registro:"";
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getFecha_estimada_respuesta() {
        return !fecha_estimada_respuesta.equals("null")&& !fecha_estimada_respuesta.equals("") ?fecha_estimada_respuesta:"";
    }

    public void setFecha_estimada_respuesta(String fecha_estimada_respuesta) {
        this.fecha_estimada_respuesta = fecha_estimada_respuesta;
    }

    public String getFecha_respuesta() {
        return !fecha_respuesta.equals("null")&& !fecha_respuesta.equals("") ?fecha_respuesta:"";
    }

    public void setFecha_respuesta(String fecha_respuesta) {
        this.fecha_respuesta = fecha_respuesta;
    }

    public String getDocumento() {
        return !documento.equals("null")&& !documento.equals("") ?documento:"";

    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
