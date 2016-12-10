package com.grupo.the_end_is_near.modelos.mapa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.modelos.Modelo;

/**
 * Created by sergiocueto on 09/12/2016.
 */

public class Conversation extends Modelo {
    private String text;
    public Conversation(Context context, double x, double y, String text) {
        super(context, x, y, 50, 200);
        this.text = text;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.message);
    }

    @Override
    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(18);
        canvas.drawText(String.valueOf(text), (float) (xIzquierda+ ancho*0.18), (int)y-Nivel.scrollEjeY, paint);
    }

    public void setText(String text) {
        this.text = text;
    }
}
