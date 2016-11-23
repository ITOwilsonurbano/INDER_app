package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Countries.Countries;
import com.itosoftware.inderandroid.Api.Etnias.Etnias;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface CountriesListener {
    public void onfinishedLoadCountries(ArrayList<Countries> countries);
    public void onErrorLoadCountries();
}
