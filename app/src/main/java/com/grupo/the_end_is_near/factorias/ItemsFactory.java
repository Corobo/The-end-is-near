package com.grupo.the_end_is_near.factorias;

import android.content.Context;

import com.grupo.the_end_is_near.modelos.items.Item;
import com.grupo.the_end_is_near.modelos.items.Key;
import com.grupo.the_end_is_near.modelos.items.Pocion;


/**
 * Created by sergiocueto on 05/11/2016.
 */

public class ItemsFactory {
    public static Item getPocion(Context context, double x, double y){
        return new Pocion(context, x, y);
    }

    public static Item getKey(Context context, double x, double y){
        return new Key(context, x, y);
    }

}
