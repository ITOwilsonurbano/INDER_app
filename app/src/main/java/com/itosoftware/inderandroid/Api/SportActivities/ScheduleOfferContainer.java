package com.itosoftware.inderandroid.Api.SportActivities;

/**
 * Created by administrador on 9/12/15.
 */

        import com.google.gson.Gson;
        import com.itosoftware.inderandroid.Api.Zones.Zones;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.HashMap;

public class ScheduleOfferContainer implements Serializable {

    private Integer totalItems = 0;
    private ArrayList<HashMap> items = new ArrayList<HashMap>();
    private String pagina = "";
    private String elementos_por_pagina = "";

    public int getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(Integer totalItems) {
        if(totalItems != 0){
            this.totalItems = totalItems;
        }
    }

    public ArrayList<HashMap> getItems() {
        return items;
    }
    public void setItems(ArrayList<HashMap> items) {
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

    public ScheduleOfferContainer(Integer totalItems, ArrayList<HashMap> items, String pagina, String elementos_por_pagina) {
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
