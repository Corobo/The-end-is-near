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
    private String name;
    public Conversation(Context context, double x, double y, String text, String name) {
        super(context, x, y, 50, 200);
        this.text = text;
        this.name = name;
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
        paint.setColor(Color.DKGRAY);
        paint.setAntiAlias(true);
        paint.setTextSize(16);
        canvas.drawText(String.valueOf(name+":"), (float) (xIzquierda+ ancho*0.12), (float)(y-Nivel.scrollEjeY-altura*0.14), paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(14);
        canvas.drawText(String.valueOf(text), (float) (xIzquierda+ ancho*0.12), (float)(y-Nivel.scrollEjeY+altura*0.16), paint);
    }

    public void setText(String text) {
        this.text = text;
    }
}
