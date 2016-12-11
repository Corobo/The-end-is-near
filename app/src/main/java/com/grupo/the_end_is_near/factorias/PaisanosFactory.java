package com.grupo.the_end_is_near.factorias;

import android.content.Context;

import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Ciudadano;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Genaro;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Manolo;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.MariPepa;


/**
 * Created by sergiocueto on 08/12/2016.
 */

public class PaisanosFactory {

    public static Ciudadano getGenaro(Context context, double x, double y){
        return new Genaro(context,x,y);
    }

    public static Ciudadano getMariPepa(Context context, double x, double y){
        return new MariPepa(context,x,y);
    }

    public static Ciudadano getManolo(Context context, double x, double y){
        return new Manolo(context,x,y);
    }
}
