package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.mapa.Conversation;
import com.grupo.the_end_is_near.modelos.Modelo;

import java.util.HashMap;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public abstract class Ciudadano extends Modelo {

    public PStates estado = PStates.ACTIVO;
    private Sprite sprite;
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    public static final String DEFAULT = "default";
    public static final String HABLANDO = "default";

    protected String[] frases;
    protected int index =0;
    protected String nombre;


    public Ciudadano(Context context, double x, double y, int altura, int ancho) {
        super(context, x, y, altura, ancho);

        this.x = x - ancho / 2;
        this.y = y - altura / 2;
    }

    protected void inicializar(Sprite def, Sprite hablando) {
        sprites.put(DEFAULT, def);
        if (hablando != null)
            sprites.put(HABLANDO, hablando);

        sprite = def;
    }

    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finSprite = false;
        if (sprite != null)
            finSprite = sprite.actualizar(tiempo);

        if (estado == PStates.HABLANDO) {
            if (finSprite){
                sprite = sprites.get(DEFAULT);
            }
            else if (sprites.containsKey(HABLANDO))
                sprite = sprites.get(HABLANDO);
        }
    }

    public void mover(Nivel nivel){
        //no hace nada, por lo general los ciudadanos no se mueven
    }

    /*se le pasa directamente el gameView para que los Items puedan realizar cualquier tipo
     de acciones sobre el juego */
    public void hablar(Nivel nivel){
        boolean pulsado = nivel.btAccionPulsado;
        if(pulsado ){
            estado = PStates.HABLANDO;
            if(index < frases.length){
                if(index < frases.length -1)
                    frases[index]+="\n...";
                if(index==0)
                    nivel.setConver(new Conversation(context,this.x,this.y-(altura+8),frases[index]));
                else
                nivel.getConver().setText(frases[index]);
                index++;
            }else{
                nivel.setConver(null);
                index = 0;
                estado=PStates.ACTIVO;
            }
        }
    };
}
