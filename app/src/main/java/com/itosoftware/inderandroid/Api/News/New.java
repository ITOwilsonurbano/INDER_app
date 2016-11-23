package com.itosoftware.inderandroid.Api.News;

import android.media.Image;
import android.widget.ImageView;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by administrador on 13/10/15.
 */
public class New implements Serializable{

    public Integer id = 0;
    public String titulo = "";
    public String subtitulo = "";
    public String imagen_url = "";
    public String texto = "";
    public Boolean is_active = false;
    public String date = "";
    public String url = "";

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        if(id != null){
            this.id = id;
        }
    }

    public String getTitle() {
        return titulo;
    }
    public void setTitle(String titulo) {
        if(titulo != null){
            this.titulo = titulo;
        }
    }

    public String getSubtitle() {
        return subtitulo;
    }
    public void setSubtitle(String subtitulo) {
        if(subtitulo != null) {
            this.subtitulo = subtitulo;
        }
    }

    public String getImageUrl() {
        return imagen_url;
    }
    public void setImageUrl(String imagen_url) {
        if(imagen_url != null) {
            this.imagen_url = imagen_url;
        }
    }

    public String getDescription() {
        return texto;
    }
    public void setDescription(String texto) {
        if(texto != null) {
            this.texto = texto;
        }
    }

    public Boolean getIs_active() {
        return is_active;
    }
    public void setIs_active(Boolean is_active) {
        if (is_active != null) {
            this.is_active = is_active;
        }
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        if (date != null) {
            this.date = date;
        }
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        if (url != null) {
            this.url = url;
        }
    }

    public void setObject(HashMap params){
        this.setId((Integer) params.get("id"));
        this.setTitle((String) params.get("titulo"));
        this.setSubtitle((String) params.get("subtitulo"));
        this.setDescription((String) params.get("texto"));
        this.setImageUrl((String) params.get("imagen_url"));
        this.setIs_active((Boolean) params.get("is_active"));
        this.setDate((String) params.get("date"));
        this.setUrl((String) params.get("url"));
    }

    public void New(HashMap params){
        this.setObject(params);
    }

    @Override
    public String toString() {

        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }

}
