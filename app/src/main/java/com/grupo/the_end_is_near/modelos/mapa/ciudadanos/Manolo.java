package com.grupo.the_end_is_near.modelos.mapa.ciudadanos;

import android.content.Context;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.mapa.Conversation;

/**
 * Created by sergiocueto on 08/12/2016.
 */

public class Manolo extends Ciudadano {
    // para gestionar el desplazamiento de manolete
    private int desplazamiento;
    private boolean moverse;

    public Manolo(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        desplazamiento = 0;
        moverse = false;

        nombre = "Manolo";
        frases = new String[]{"No puedes pasar!!", "corred insensatos"};

        Sprite sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.guardian_puerta),
                ancho, altura,
                1, 1, true);
        inicializar(sprite, null);
    }

    @Override
    public void hablar(Nivel nivel){
        boolean pulsado = nivel.btAccionPulsado;
        if(pulsado ){
            if(nivel.isKey())
                frases = new String[]{"Conseguiste la llave","ahora puedes pasar"};
            estado = Estado.HABLANDO;
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
                estado=Estado.ACTIVO;
                if(nivel.isKey()){
                    moverse =true;
                    nivel.addPortal(360,10);
                }
            }

        }
    };

    @Override
    public void mover(Nivel nivel){
        if(moverse && desplazamiento < 44){
            desplazamiento++;

            x -=1.2;

        }

    }
}
