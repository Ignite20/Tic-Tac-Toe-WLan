package com.tests.amadalingradinaru.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by a.madalin.gradinaru on 07/02/2018.
 */

@JsonObject
public class Message {
    /*
    * Annotate a field that you want sent with the @JsonField marker.
    */
    @JsonField
    public String description;

    @JsonField
    public int position;

    @JsonField
    public String mark;


    /*
     * Note that since this field isn't annotated as a
     * @JsonField, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    public int nonJsonField;
}
