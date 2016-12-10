package com.grupo.the_end_is_near.factorias;

import android.content.Context;

import com.grupo.the_end_is_near.modelos.mapa.enemigos.Boss1;
import com.grupo.the_end_is_near.modelos.mapa.enemigos.Enemigo;
import com.grupo.the_end_is_near.modelos.mapa.enemigos.Enemigo1;
import com.grupo.the_end_is_near.modelos.mapa.enemigos.EnemigoInteligente;

/**
 * Created by sergiocueto on 05/11/2016.
 */

public class EnemiesFactory {
    //EL ID ES para saber con que enemigo se colisionó
    private static final int ID_ENEMIGO1 =0;
    private static final int ID_ENEMIGOINTELIGENTE =1;
    private static final int ID_BOSS1 =2;

    public static Enemigo getEnemigo1(Context context, double xInicial, double yInicial){
        return new Enemigo1(context, xInicial, yInicial,ID_ENEMIGO1);
    }

    public static Enemigo getEnemigoInteligente(Context context, double xInicial, double yInicial){
        return new EnemigoInteligente(context, xInicial, yInicial,ID_ENEMIGOINTELIGENTE);
    }

    public static Enemigo getBoss1(Context context, double xInicial, double yInicial){
        return new Boss1(context, xInicial, yInicial,ID_BOSS1);
    }
}
