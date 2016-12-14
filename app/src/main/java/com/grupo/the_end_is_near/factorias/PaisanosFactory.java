package com.grupo.the_end_is_near.factorias;

import android.content.Context;

import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Ciudadano;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.CiudadanoEspecifico;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.CiudadanoPuerta;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.CiudadanoPuzzle;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.Generico;
import com.grupo.the_end_is_near.modelos.mapa.ciudadanos.CiudadanoEspecifico2;


/**
 * Created by sergiocueto on 08/12/2016.
 */

public class PaisanosFactory {

    public static Ciudadano getGenaro(Context context, double x, double y){
        return new CiudadanoEspecifico(context,x,y);
    }

    public static Ciudadano getMariPepa(Context context, double x, double y){
        return new CiudadanoPuzzle(context,x,y);
    }

    public static Ciudadano getManolo(Context context, double x, double y){
        return new CiudadanoPuerta(context,x,y);
    }

    public static Ciudadano getRigoverto(Context context, double x, double y){
        return new CiudadanoEspecifico2(context,x,y);
    }

    public static Ciudadano getGenericCiu(Context context, double x, double y){
        return new Generico(context,x,y);
    }
}
