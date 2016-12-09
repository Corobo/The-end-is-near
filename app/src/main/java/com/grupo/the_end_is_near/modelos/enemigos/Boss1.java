package com.grupo.the_end_is_near.modelos.enemigos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class Boss1 extends Enemigo {

    public static final String FRONT = "front";

    public Boss1(Context context, double xInicial, double yInicial, int idEnemigo) {
        super(context, xInicial, yInicial, 100, 100, idEnemigo);
        setVelocidadX(0);
    }

    @Override
    public void inicializar() {

        Sprite front = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_enemigo_alien_front),
                ancho, altura,
                5, 3, true);

        _getSprites().put(FRONT, front);

        setSprite(front);
    }

    @Override
    public void mover(Nivel nivel) {
        //no se mueve
    }
}
