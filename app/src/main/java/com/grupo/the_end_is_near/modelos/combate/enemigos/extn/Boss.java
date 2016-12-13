package com.grupo.the_end_is_near.modelos.combate.enemigos.extn;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;

/**
 * Created by jaime on 12/12/2016.
 */

public class Boss extends Enemigo {

    public Boss(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial,129,148);

        this.tipo=4;
        this.vida = 2000;
        this.daño = 70;
    }

    @Override
    public void inicializar(){
        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.boss_18),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.boss_18_golpeado_anim),
                ancho, altura,
                4, 2, false);
        sprites.put(DAÑADO,dañado);

        sprite = parado;
    }
}
