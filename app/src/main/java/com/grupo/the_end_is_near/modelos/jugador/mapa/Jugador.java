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
    public static final String SALTANDO_DERECHA = "saltando_derecha";
    public static final String SALTANDO_IZQUIERDA = "saltando_izquierda";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    public static final String GOLPEADO_DERECHA = "golpeado_derecha";
    public static final String GOLPEADO_IZQUIERDA = "golpeado_izquierda";

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
                CargadorGraficos.cargarDrawable(context, R.drawable.playeridleright),
                ancho, altura,
                4, 8, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playeridle),
                ancho, altura,
                4, 8, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerrunright),
                ancho, altura,
                4, 8, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerrun),
                ancho, altura,
                4, 8, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite saltandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerjumpright),
                ancho, altura,
                4, 4, true);
        sprites.put(SALTANDO_DERECHA, saltandoDerecha);

        Sprite saltandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerjump),
                ancho, altura,
                4, 4, true);
        sprites.put(SALTANDO_IZQUIERDA, saltandoIzquierda);


        Sprite disparandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playershootright),
                ancho, altura,
                4, 4, false);
        sprites.put(DISPARANDO_DERECHA, disparandoDerecha);

        Sprite disparandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playershoot),
                ancho, altura,
                4, 4, false);
        sprites.put(DISPARANDO_IZQUIERDA, disparandoIzquierda);


        Sprite golpeadoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerimpactright),
                ancho, altura,
                4, 4, false);
        sprites.put(GOLPEADO_DERECHA, golpeadoDerecha);

        Sprite golpeadoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerimpact),
                ancho, altura,
                4, 4, false);
        sprites.put(GOLPEADO_IZQUIERDA, golpeadoIzquierda);

        // animación actual
        sprite = paradoDerecha;
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

        if (saltoPendiente){
            saltoPendiente = false;
            enElAire = true;
            velocidadY = velcidadSalto;
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

        if (enElAire && orientacion == IZQUIERDA ){
            sprite = sprites.get(SALTANDO_IZQUIERDA);
        }
        if (enElAire && orientacion == DERECHA ) {
            sprite = sprites.get(SALTANDO_DERECHA);
        }

        if (disparando){
            if (orientacion == DERECHA){
                sprite = sprites.get(DISPARANDO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(DISPARANDO_IZQUIERDA);
            }
        }

        if (golpeado){
            if (orientacion == DERECHA){
                sprite = sprites.get(GOLPEADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(GOLPEADO_IZQUIERDA);
            }
        }



    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, msInmunidad > 0);
    }

    public void procesarOrdenes (float orientacionPad) {
        if (orientacionPad > 0) {
            velocidadX = -5;
        } else if (orientacionPad < 0 ){
            velocidadX = 5;
        } else {
            velocidadX = 0;
        }
    }


    public void restablecerPosicionInicial(){
        if(Opciones.dificultad)
            vidas = 1;
        else
            vidas = 3;
        golpeado = false;
        msInmunidad = 0;

        this.x = xInicial;
        this.y = yInicial;
        orientacion = DERECHA;
        enElAire = false;
        saltoPendiente = false;
    }


    public int golpeado(){
        if (msInmunidad <= 0) {
            if (vidas > 0) {
                vidas--;
                msInmunidad = 3000;
                golpeado = true;
                // Reiniciar animaciones que no son bucle
                sprites.get(GOLPEADO_IZQUIERDA).setFrameActual(0);
                sprites.get(GOLPEADO_DERECHA).setFrameActual(0);
            }
        }
        return vidas;
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
