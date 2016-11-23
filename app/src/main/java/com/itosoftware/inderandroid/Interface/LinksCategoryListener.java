package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Countries.Countries;
import com.itosoftware.inderandroid.Api.LinksCategory.LinksCategory;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface LinksCategoryListener {
    public void onfinishedLoadLinksCategory(ArrayList<LinksCategory> linksCategory, Integer maxPages);
}
