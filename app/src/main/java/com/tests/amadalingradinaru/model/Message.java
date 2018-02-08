package com.tests.amadalingradinaru.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by a.madalin.gradinaru on 07/02/2018.
 */

@JsonObject
public class Message {
    /*
    * Annotate a field that you want sent with the @JsonField marker.
    */

    @JsonField
    public boolean isGameOver;

    @JsonField
    public boolean isXTurn;

    @JsonField
    public int position;

    @JsonField
    public ArrayList<String> marks;

    @JsonField
    public int restart;

    @JsonField
    public boolean isNewGame;

    /*
     * Note that since this field isn't annotated as a
     * @JsonField, LoganSquare will ignore it when parsing
     * and serializing this class.
     */
    public int nonJsonField;
}
