package com.grupo.the_end_is_near.modelos.combate.enemigos.extn;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;

/**
 * Created by Corobo on 06/12/2016.
 */

public class EnemigoTipo3 extends Enemigo {
    public EnemigoTipo3(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);

        this.tipo=3;
        this.vida= 400;
        this.daño= 35;
    }

    @Override
    public void inicializar(){
        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_04),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_04_golpeado_anim),
                ancho, altura,
                4, 2, false);
        sprites.put(DAÑADO,dañado);
    }
}
