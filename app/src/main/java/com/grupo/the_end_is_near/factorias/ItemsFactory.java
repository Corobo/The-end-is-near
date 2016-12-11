package com.grupo.the_end_is_near.factorias;

import android.content.Context;

import com.grupo.the_end_is_near.modelos.mapa.items.Item;
import com.grupo.the_end_is_near.modelos.mapa.items.Key;
import com.grupo.the_end_is_near.modelos.mapa.items.Pocion;
import com.grupo.the_end_is_near.modelos.mapa.items.Runa1;
import com.grupo.the_end_is_near.modelos.mapa.items.Runa2;
import com.grupo.the_end_is_near.modelos.mapa.items.Runa3;
import com.grupo.the_end_is_near.modelos.mapa.items.Portal1;


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

    public static Item getRuna1(Context context, double x, double y){
        return new Runa1(context, x, y);
    }

    public static Item getRuna2(Context context, double x, double y){
        return new Runa2(context, x, y);
    }

    public static Item getRuna3(Context context, double x, double y){
        return new Runa3(context, x, y);
    }

    public static Item getPortal1(Context context, double x, double y){
        return new Portal1(context, x, y);
    }

}
