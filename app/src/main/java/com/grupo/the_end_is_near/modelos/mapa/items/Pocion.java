package com.grupo.the_end_is_near.modelos.mapa.items;

import android.content.Context;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;

/**
 * Created by sergiocueto on 05/11/2016.
 */

public class Pocion extends Item {

    public Pocion(Context context, double x, double y) {
        super(context, x, y, 35, 32,false);

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.pocion),
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
    public void doSomething(Nivel nivel) {
        boolean pulsado = nivel.btAccionPulsado;
        if(pulsado && estado == Estado.ACTIVO){
            estado = Estado.RECOLECTADO;
            boolean usarPocion=true;
            for(Personaje heroe:GameView.combate.heroes){
                if((heroe.vida<heroe.vidaMaxima || heroe.mana<heroe.manaMaximo) &&  usarPocion){
                    heroe.restablecerVida();
                }
                else{
                    usarPocion=false;
                }
            }
            if(!usarPocion)
                GameView.combate.pociones++;
        }
    }
}
