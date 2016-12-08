package com.grupo.the_end_is_near.modelos.combate.enemigos.extn;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;

/**
 * Created by Corobo on 06/12/2016.
 */

public class EnemigoTipo2 extends Enemigo {
    public EnemigoTipo2(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);

        this.tipo=2;
        this.vida=240;
        this.daño=60;
    }

    @Override
    public void inicializar(){
        Sprite parado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_03),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO, parado);

        Sprite dañado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemy_03_golpeado_anim),
                ancho, altura,
                4, 2, false);
        sprites.put(DAÑADO,dañado);
    }

}
