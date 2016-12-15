package com.grupo.the_end_is_near.modelos.combate.controles;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by Corobo on 06/12/2016.
 */

public class Enemigo extends Modelo {
    public Enemigo(Context context,double pos, double posY) {
        super(context,GameView.pantallaAncho / (3 + pos), GameView.pantallaAlto / (2.5 + posY),54,63);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)
                ) {
            estaPulsado = true;
        }
        return estaPulsado;
    }
}