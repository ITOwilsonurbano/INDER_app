package com.itosoftware.inderandroid.Api.Recommended;


        import com.android.volley.toolbox.StringRequest;
        import com.google.gson.Gson;

        import java.io.Serializable;
        import java.util.HashMap;

/**
 * Created by administrador on 13/10/15.
 */
public class Recommended implements Serializable{


    public Integer id = 0;
    public String imagen_url = "";
    public String titulo = "";
    public String url = "";

    public String getUrl() { return url;}
    public void setUrl(String url) { this.url = url; }

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        if(id != null) {
            this.id = id;
        }
    }

    public String getImageUrl() {
        return this.imagen_url;
    }
    public void setImageUrl(String imageUrl) {
        this.imagen_url = imageUrl;
    }

    public String getTitulo() {
        return this.titulo;
    }
    public void setTitulo(String content) {
        this.titulo = content;
    }


    public Recommended(HashMap params) {
        this.setId((Integer) params.get("id"));
        this.setImageUrl((String) params.get("imagen_url"));
        this.setTitulo((String) params.get("titulo"));
        this.setUrl((String) params.get("url"));
    }

    @Override
    public String toString() {

        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }

}
