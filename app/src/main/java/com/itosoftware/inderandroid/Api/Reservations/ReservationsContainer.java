package com.itosoftware.inderandroid.Api.Reservations;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.News.New;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by administrador on 4/11/15.
 */
public class ReservationsContainer implements Serializable{

    private Integer totalItems = 0;
    private ArrayList<Reservations> items = new ArrayList<Reservations>();
    private String pagina = "1";
    private String elementos_por_pagina = "10";

    public int getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(Integer totalItems) {
        if(totalItems != 0){
            this.totalItems = totalItems;
        }
    }

    public ArrayList<Reservations> getItems() {
        return items;
    }
    public void setItems(ArrayList<Reservations> items) {
        if (items != null) {
            this.items = items;
        }
    }

    public String getPagina() {
        return pagina;
    }
    public void setPagina(String pagina) {
        if(pagina != null) {
            this.pagina = pagina;
        }
    }

    public String getElementos_por_pagina() {
        return elementos_por_pagina;
    }
    public void setElementos_por_pagina(String elementos_por_pagina) {
        if(elementos_por_pagina != null) {
            this.elementos_por_pagina = elementos_por_pagina;
        }
    }

    public ReservationsContainer(Integer totalItems, ArrayList<Reservations> items, String pagina, String elementos_por_pagina) {
        this.setItems(items);
        this.setTotalItems(totalItems);
        this.setPagina(pagina);
        this.setElementos_por_pagina(elementos_por_pagina);
    }


    @Override
    public String toString() {

        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }

}
