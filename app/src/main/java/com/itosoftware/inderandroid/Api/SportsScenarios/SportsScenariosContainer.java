package com.itosoftware.inderandroid.Api.SportsScenarios;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by administrador on 5/11/15.
 */
public class SportsScenariosContainer implements Serializable {

    private Integer totalItems = 0;
    private ArrayList<SportsScenarios> items = new ArrayList<SportsScenarios>();
    private String pagina = "1";
    private String elementos_por_pagina = "800";

    public int getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(Integer totalItems) {
        if(totalItems != 0){
            this.totalItems = totalItems;
        }
    }

    public ArrayList<SportsScenarios> getItems() {
        return items;
    }
    public void setItems(ArrayList<SportsScenarios> items) {
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

    public SportsScenariosContainer(Integer totalItems, ArrayList<SportsScenarios> items, String pagina, String elementos_por_pagina) {
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
