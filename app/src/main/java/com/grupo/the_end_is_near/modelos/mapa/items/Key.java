package com.grupo.the_end_is_near.modelos.mapa.items;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class Key extends Item {

    public Key(Context context, double x, double y) {
        super(context, x, y, 35, 32,false);

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.key),
                ancho, altura,
                1, 1, true);

        Sprite recolectando = null/*new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.recogiendo_pocion),
                ancho, altura,
                8, 8, false)*/;

        //TODO buscar un sprite para cuando lo coges
        inicializar(sprite,recolectando);
    }

    @Override
    public void doSomething(Nivel nivel) throws Exception {
        boolean pulsado = nivel.btAccionPulsado;
        if(pulsado && estado == IStates.ACTIVO){
            nivel.setKey(true);
            estado = IStates.RECOLECTADO;
        }
    }
}
