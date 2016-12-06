package com.grupo.the_end_is_near.escenario;


import android.content.Context;
import android.graphics.Canvas;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo1;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo2;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo3;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Mage;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Thief;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Warrior;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;

import java.util.LinkedList;
import java.util.List;

public class Combate {

    //TODO
    //1. Colocar personajes con la animación estandar (parados)
    //2. Animaciones para el ataque de Heroes y de enemigos
    //El heroe que ataque se mueve un poco a la izquierda y si es un enemigo hacia la derecha.
    //A continuación vuelven a su posición inicial
    //3. Animación para el Heroe recibiendo el daño. El enemigo lo mismo.
    //4. Animación de muerte para cada Heroe y enemigo (este podría simplemente desaparecer)
    //5. Pantalla de victoria/derrota

    public boolean enCombate;
    private Context context;
    private Nivel nivel;

    private Warrior warrior;
    private Mage mage;
    private Thief thief;

    private List<Enemigo> enemigos;

    private Sprite fondo;

    public Combate(Context context,Nivel nivel) {
        this.context=context;
        this.nivel=nivel;
        this.fondo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.fondo_01),
                512, 312,
                1, 1, false);

        enemigos= new LinkedList<Enemigo>();
        iniciarEnemigosAleatorios();

        this.warrior= new Warrior(context, GameView.pantallaAncho/1.25, GameView.pantallaAlto / 2.5);
        this.mage = new Mage(context, GameView.pantallaAncho/1.2, GameView.pantallaAlto / 1.9);
        this.thief = new Thief(context, GameView.pantallaAncho/1.3, GameView.pantallaAlto / 3.5);

        enCombate=true; //TODO debe ser false. Por el momento true para probarlo
    }

    public void iniciarEnemigosAleatorios() {
        double pos = 0;
        double posY=0;
        int x = new Double(Math.random() * 3).intValue();
        while (x >= 0) {
            int tipoEnemigo = new Double(Math.random() * 3).intValue()+1;
            if(tipoEnemigo==1)
                enemigos.add(new EnemigoTipo1(context, GameView.pantallaAncho / (3 + pos), GameView.pantallaAlto / (2.5 + posY)));
            if(tipoEnemigo==2)
                enemigos.add(new EnemigoTipo2(context, GameView.pantallaAncho / (3 + pos), GameView.pantallaAlto / (2.5 + posY)));
            if(tipoEnemigo==3)
                enemigos.add(new EnemigoTipo3(context, GameView.pantallaAncho / (3 + pos), GameView.pantallaAlto / (2.5 + posY)));
            x--;
            if(pos==0) {
                pos = pos + 1.5;
                posY = posY +1.5;
            }
            else if(pos==1.5) {
                pos = 1.5;
                posY = -0.7;
            }
        }
        //TODO
    }

    public void iniciaCombate(){
        this.enCombate=true;
    }

    public void terminaCombate(){
        this.enCombate=false;
    }

    public void actualizar(long tiempo) {
        warrior.actualizar(tiempo);
        thief.actualizar(tiempo);
        mage.actualizar(tiempo);
        for(Enemigo enemigo: enemigos){
            enemigo.actualizar(tiempo);
        }
    }

    public void dibujar(Canvas canvas) {
        fondo.dibujarSprite(canvas,GameView.pantallaAncho / 2, GameView.pantallaAlto / 2);
        thief.dibujar(canvas);
        warrior.dibujar(canvas);
        mage.dibujar(canvas);
        for(Enemigo enemigo: enemigos){
            enemigo.dibujar(canvas);
        }
    }

    public void atacar(){
        warrior.atacar();
    }
}
