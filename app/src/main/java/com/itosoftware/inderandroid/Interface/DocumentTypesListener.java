package com.itosoftware.inderandroid.Interface;


import com.itosoftware.inderandroid.Api.DocumentType.DocumentTypes;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface DocumentTypesListener {
    public void onfinishedLoadDocumentTypes(ArrayList<DocumentTypes> documentTypes);
    public void onErrorLoadDocumentTypes();
}
