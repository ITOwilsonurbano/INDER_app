package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Neighborhood.Neighborhood;
import java.util.ArrayList;

/**
 * Created by administrador on 9/11/15.
 */
public interface NeighborhoodListener {
    public void onfinishedLoadNeighborhood(ArrayList<Neighborhood> neighborhood);
    public void onErrorLoadNeighborhood();
}
