package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Communes.Communes;
import java.util.ArrayList;

/**
 * Created by administrador on 9/11/15.
 */

public interface CommunesListener {
    public void onfinishedLoadCommunes(ArrayList<Communes> communes);
    public void onErrorLoadCommunes();
}

