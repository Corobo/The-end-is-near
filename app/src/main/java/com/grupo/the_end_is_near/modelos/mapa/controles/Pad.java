package com.grupo.the_end_is_near.modelos.mapa.controles;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by uo227602 on 05/10/2016.
 */
public class Pad extends Modelo {

    public Pad(Context context) {
        super(context, GameView.pantallaAncho*0.15 , GameView.pantallaAlto*0.8 ,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 100;
        ancho = 100;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
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

    public int getOrientacionX(float cliclX) {
        int xPulsado = (int) (x - cliclX);
        return xPulsado>15 || xPulsado<-15 ? xPulsado : 0;
    }
    public int getOrientacionY(float clicY){
        int yPulsado = (int) (y - clicY);
        return yPulsado>15 || yPulsado<-15 ? yPulsado : 0;
    }

}

