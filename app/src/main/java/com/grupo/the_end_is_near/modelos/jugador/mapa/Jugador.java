package com.grupo.the_end_is_near.modelos.jugador.mapa;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Opciones;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;

import java.util.HashMap;


/**
 * Created by uo227602 on 05/10/2016.
 */
public class Jugador extends Modelo {

    //Animaciones
    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";
    public static final String PARADO_ARRIBA = "Parado_arriba";
    public static final String PARADO_ABAJO = "Parado_abajo";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    public static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    public static final String CAMINANDO_ABAJO = "Caminando_abajo";

    public int vidas;
    // ACTUAL
    public double msInmunidad;
    public boolean golpeado = false;


    //Puntero sprite actual
    private Sprite sprite;

    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    double xInicial;
    double yInicial;


     double velocidadX;
     float velocidadY; // actual
     float velcidadSalto = -14; // velocidad que le da el salto

    public boolean saltoPendiente; // tiene que saltar
    public boolean enElAire; // está en el aire


    public int orientacion;
    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;
    public static final int ARRIBA = 2;
    public static final int ABAJO = -2;

    public boolean disparando;
    public static final String DISPARANDO_DERECHA = "disparando_derecha";
    public static final String DISPARANDO_IZQUIERDA = "disparando_izquierda";


    public Jugador(Context context, double xInicial, double yInicial,int vidas) {
        super(context, 0, 0, 40, 40);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;
        this.vidas = vidas;

        inicializar();
    }

    public void inicializar (){
        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_derecha_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_izquierda_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_derecha_moviendo),
                ancho, altura,
                1, 2, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_izquierda_moviendo),
                ancho, altura,
                1, 2, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite arribaParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_arriba_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_ARRIBA, arribaParado);

        Sprite arribaMoviendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_arriba_moviendo),
                ancho, altura,
                1, 2, true);
        sprites.put(CAMINANDO_ARRIBA, arribaMoviendo);


        Sprite abajoParado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_abajo_parado),
                ancho, altura,
                1, 2, false);
        sprites.put(PARADO_ABAJO, abajoParado);

        Sprite abajoMoviendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.personaje_abajo_moviendo),
                ancho, altura,
                1, 2, false);
        sprites.put(CAMINANDO_ABAJO, abajoMoviendo);


        // animación actual
        sprite = abajoParado;
    }

    public void actualizar (long tiempo) {

        boolean finSprite = sprite.actualizar(tiempo);

        if(msInmunidad > 0){
            msInmunidad -= tiempo;
        }

        if (golpeado && finSprite){
            golpeado = false;
        }
        if(disparando && finSprite){
            disparando = false;
        }
        if (velocidadX > 0 ) {
            sprite = sprites.get(CAMINANDO_DERECHA);
            orientacion = DERECHA;
        }
        if (velocidadX < 0 ) {
            sprite = sprites.get(CAMINANDO_IZQUIERDA);
            orientacion = IZQUIERDA;
        }
        if (velocidadX == 0 ){
            if (orientacion == DERECHA){
                sprite = sprites.get(PARADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(PARADO_IZQUIERDA);
            }
        }
        if(velocidadY >0 ){
            sprite = sprites.get(CAMINANDO_ABAJO);
            orientacion = ABAJO;
        }
        if(velocidadY <0){
            sprite = sprites.get(CAMINANDO_ARRIBA);
            orientacion = ARRIBA;
        }
        if(velocidadY==0){
            if(orientacion==ARRIBA){
                sprite = sprites.get(PARADO_ARRIBA);
            }else if(orientacion == ABAJO){
                sprite = sprites.get(PARADO_ABAJO);
            }
        }


    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, msInmunidad > 0);
    }

    public void procesarOrdenes (float orientacionPad,float orientacionPadY) {
        if (orientacionPad > 0) {
            velocidadX = -5;
        } else if (orientacionPad < 0 ){
            velocidadX = 5;
        } else {
            velocidadX = 0;
        }
        if(orientacionPadY > 0){
            velocidadY = -5;
        } else if (orientacionPadY < 0 ){
            velocidadY = 5;
        } else {
            velocidadY = 0;
        }
    }


    public double getVelocidadX(){
        return velocidadX;
    }
    public float getVelocidadY(){
        return velocidadY;
    }
    public void setVelocidadY(float velocidadY){
        this.velocidadY = velocidadY;
    }
    public void setPosicionInicial(double xInicial,double yInicial){
        this.xInicial=xInicial;
        this.yInicial=yInicial;
    }





}
