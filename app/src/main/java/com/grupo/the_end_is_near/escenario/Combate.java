package com.grupo.the_end_is_near.escenario;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.global.Turno;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo1;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo2;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo3;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Mage;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Thief;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Warrior;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;
import com.grupo.the_end_is_near.modelos.jugador.mapa.Jugador;

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

    public List<Enemigo> enemigos;
    public List<Personaje> heroes;

    private Sprite fondo;
    public long millis;

    public int turno= Turno.JUGADOR;

    public Enemigo enemigoAtacando=null;
    public int enemigosTerminados=0;

    public Personaje compañeroAtacando=null;
    public int compañerosTerminados=0;

    public Combate(Context context,Nivel nivel) {
        this.context=context;
        this.nivel=nivel;
        this.fondo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.fondo_01),
                512, 312,
                1, 1, false);

        enemigos= new LinkedList<Enemigo>();
        heroes =new LinkedList<Personaje>();
        iniciarEnemigosAleatorios();

        heroes.add(new Thief(context, GameView.pantallaAncho/1.3, GameView.pantallaAlto / 3.5));
        heroes.add(new Warrior(context, GameView.pantallaAncho/1.25, GameView.pantallaAlto / 2.5));
        heroes.add(new Mage(context, GameView.pantallaAncho/1.2, GameView.pantallaAlto / 1.9));

        enCombate=true; //TODO debe ser false. Por el momento true para probarlo
        turnoEnemigos();
    }

    public void iniciarEnemigosAleatorios() {
        double pos =0;
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
    }

    public void iniciaCombate(){
        this.enCombate=true;
    }

    public void terminaCombate(){
        this.enCombate=false;
    }

    public void actualizar(long tiempo) {
        int numHeroesDerrotados=0;
        int numEnemigosDerrotados=0;

        for(Personaje heroe: heroes){
            heroe.actualizar(tiempo);
            if(heroe.vida<=0)
                numHeroesDerrotados++;
        }

        if(numHeroesDerrotados==heroes.size())
            //terminaCombate();//DERROTA TODO

        for(Enemigo enemigo: enemigos){
            enemigo.actualizar(tiempo);
            if(enemigo.vida<=0)
                numEnemigosDerrotados++;
        }

        if(numEnemigosDerrotados==enemigos.size()) {
            //terminaCombate();//TODO Victoria
        }

        turnoCompañeros();
        turnoEnemigos();
    }

    public void dibujar(Canvas canvas) {
        fondo.dibujarSprite(canvas,GameView.pantallaAncho / 2, GameView.pantallaAlto / 2);
        for(Personaje heroe: heroes){
            heroe.dibujar(canvas);
        }

        for(Enemigo enemigo: enemigos){
            enemigo.dibujar(canvas);
        }
    }

    public void atacar(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            Personaje heroe = heroes.get(1);
            heroe.atacar();
            Enemigo enemigo = enemigos.get(GameView.enemigo);
            enemigo.golpeado(heroe.tipo,heroe.daño);
        }
    }

    public void defender(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            heroes.get(1).bloquear();
        }
    }

    public void magia(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            Personaje heroe = heroes.get(1);
            heroe.magia();
            Enemigo enemigo = enemigos.get(GameView.enemigo);
            enemigo.golpeado(heroe.tipo,heroe.dañoMagico);
        }
    }

    public void pocion() {

    }
    public void huir() {
    }

    public void turnoEnemigos(){
        if(turno==Turno.ENEMIGO) {
            for (Enemigo enemigo : enemigos){
                if(!enemigo.utilizado && ((enemigoAtacando!=null && !enemigoAtacando.estaOcupado()) || enemigoAtacando==null)) {
                    enemigosTerminados = enemigosTerminados + 1;
                    enemigo.utilizado=true;
                    int activos = 0;
                    //for(Jugador persoanje:heroes)

                    int x = new Double(Math.random() * 3).intValue();
                    Personaje heroe = heroes.get(x);
                    if(heroe.estado == Estado.ACTIVO) {
                        int daño = enemigo.golpear(heroe.tipo, heroe.nivel);
                        enemigoAtacando = enemigo;
                        heroe.golpeado(daño);
                        GameView.pintarDaño=x;
                        GameView.dañoActual=daño;
                    }
                }
            }
            if(enemigosTerminados >= enemigos.size() && !enemigoAtacando.estaOcupado()){
                todosEnemigosUsados();
                enemigosTerminados=0;
                enemigoAtacando=null;
                turno= Turno.JUGADOR;
            }
        }
    }

    public void turnoCompañeros(){
        if(!heroes.get(1).estaOcupado() && turno==Turno.COMPAÑEROS){
            for(Personaje heroe:heroes){
                if((compañeroAtacando!=null && !compañeroAtacando.estaOcupado()) || compañeroAtacando==null) {
                    if (heroe.tipo != 0 && heroe.estado==Estado.ACTIVO) {
                        compañerosTerminados=compañerosTerminados+1;
                        heroe.accionAleatoria();
                        int x = new Double(Math.random() * enemigos.size()).intValue();
                        Enemigo enemigo = enemigos.get(x);
                        enemigoAtacando = enemigo;
                        enemigo.golpeado(heroe.tipo,heroe.daño);
                        compañeroAtacando = heroe;
                        GameView.pintarDaño= x;
                        GameView.dañoActual = enemigo.ultimoDañoRecibido;
                    }
                }
            }
            if(compañerosTerminados >= heroes.size() && !compañeroAtacando.estaOcupado()){
                todosCompañerosUsados();
                compañerosTerminados=0;
                compañeroAtacando=null;
                turno= Turno.ENEMIGO;
            }
        }
    }

    public void todosCompañerosUsados(){
        for(Personaje heroe:heroes){
            heroe.utilizado=false;
        }
    }

    public void todosEnemigosUsados(){
        for(Enemigo enemigo:enemigos){
            enemigo.utilizado=false;
        }
    }


}
