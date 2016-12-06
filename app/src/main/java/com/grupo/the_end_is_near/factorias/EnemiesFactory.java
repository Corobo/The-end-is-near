package com.grupo.the_end_is_near.factorias;

import android.content.Context;

import com.grupo.the_end_is_near.modelos.enemigos.Enemigo;
import com.grupo.the_end_is_near.modelos.enemigos.Enemigo1;
import com.grupo.the_end_is_near.modelos.enemigos.EnemigoInteligente;

/**
 * Created by sergiocueto on 05/11/2016.
 */

public class EnemiesFactory {
    public static Enemigo getEnemigo1(Context context, double xInicial, double yInicial){
        return new Enemigo1(context, xInicial, yInicial);
    }

    public static Enemigo getEnemigoInteligente(Context context, double xInicial, double yInicial){
        return new EnemigoInteligente(context, xInicial, yInicial);
    }
}
