package com.grupo.the_end_is_near.modelos.mapa.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.escenario.Nivel;
import com.grupo.the_end_is_near.escenario.Tile;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.Utilidades;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.Modelo;

import java.util.HashMap;

/**
 * Created by cueto on 18/10/2016.
 */

public abstract class Enemigo extends Modelo {
    public int estado = Estado.ACTIVO;

    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String UP = "up";
    public static final String FRONT = "front";

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public double velocidadX = 1.2;

    private int cadenciaDisparo;
    private long milisegundosDisparo;
    private int idEnemigo;

    public Enemigo(Context context, double xInicial, double yInicial,int altura, int ancho,
                   int idEnemigo) {
        super(context, 0, 0, altura, ancho);

        this.x = xInicial -ancho/2;
        this.y = yInicial - altura/2;
        this.idEnemigo = idEnemigo;

        inicializar();
    }

    public abstract void inicializar ();

    @Override
    //actualización por defecto
    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == Estado.INACTIVO){

        } else {

            if (velocidadX > 0) {
                sprite = sprites.get(CAMINANDO_DERECHA);
            }
            if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
        }

        if ( estado == Estado.INACTIVO && finSprite == true){
            estado = Estado.ELIMINAR;
        }

    }

    public abstract void mover(Nivel nivel);

    public void destruir (){
        velocidadX = 0;
        estado = Estado.INACTIVO;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y -Nivel.scrollEjeY);
    }

    public int getIdEnemigo() {
        return idEnemigo;
    }

    protected HashMap<String, Sprite> _getSprites() {
        return sprites;
    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    protected Sprite _getSprite() {
        return sprite;
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public long getMilisegundosDisparo() {
        return milisegundosDisparo;
    }

    public int getCadenciaDisparo() {
        return cadenciaDisparo;
    }

    public void setCadenciaDisparo(int cadenciaDisparo) {
        this.cadenciaDisparo = cadenciaDisparo;
    }

    public void setMilisegundosDisparo(long milisegundosDisparo) {
        this.milisegundosDisparo = milisegundosDisparo;
    }
}