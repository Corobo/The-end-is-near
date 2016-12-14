package com.grupo.the_end_is_near.escenario;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.grupo.the_end_is_near.GameView;
import com.grupo.the_end_is_near.R;
import com.grupo.the_end_is_near.gestores.CargadorGraficos;
import com.grupo.the_end_is_near.gestores.GestorAudio;
import com.grupo.the_end_is_near.global.Estado;
import com.grupo.the_end_is_near.global.Turno;
import com.grupo.the_end_is_near.graficos.Sprite;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.Boss;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo1;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo2;
import com.grupo.the_end_is_near.modelos.combate.enemigos.extn.EnemigoTipo3;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Mage;
import com.grupo.the_end_is_near.modelos.combate.jugadores.Personaje;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Thief;
import com.grupo.the_end_is_near.modelos.combate.jugadores.extn.Warrior;
import com.grupo.the_end_is_near.modelos.combate.enemigos.Enemigo;
import com.grupo.the_end_is_near.modelos.mapa.jugador.Jugador;

import java.util.LinkedList;
import java.util.List;

public class Combate {

    public boolean enCombate;
    private Context context;

    public List<Enemigo> enemigos;
    public List<Personaje> heroes;

    private Sprite fondo;

    public int turno= Turno.JUGADOR;

    public Enemigo enemigoAtacando=null;
    public int enemigosTerminados=0;

    public Personaje compañeroAtacando=null;
    public int compañerosTerminados=0;

    public int pociones;
    public boolean jefe;

    public int jX;
    public int jY;

    public Combate(Context context) {
        this.context=context;
        this.fondo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.fondo_01),
                GameView.pantallaAncho, GameView.pantallaAlto,
                1, 1, false);

        enemigos= new LinkedList<Enemigo>();
        heroes =new LinkedList<Personaje>();

        heroes.add(new Thief(context, GameView.pantallaAncho/1.3, GameView.pantallaAlto / 3.5));
        heroes.add(new Warrior(context, GameView.pantallaAncho/1.25, GameView.pantallaAlto / 2.5));
        heroes.add(new Mage(context, GameView.pantallaAncho/1.2, GameView.pantallaAlto / 1.9));

        enCombate=false;
    }

    public void actualizar(long tiempo) {
        int enemigosDerrotados = 0; //Para saber si hemos derrotados a todos o no
        boolean ocupado=false; //Nos dice si hay alguien ocupado en este momento

        turnoCompañeros();
        turnoEnemigos();


        for(Personaje heroe:heroes){
            if(turno==Turno.JUGADOR && heroe.estaBloqueando) {
                //Nuevo turno, y por lo tanto ya no estará bloqueado
                heroe.accion("Parado");
                heroe.estaBloqueando = false;
            }
            heroe.actualizar(tiempo);
            if(heroe.estaOcupado())
                ocupado=true;
        }

        for(Enemigo enemigo: enemigos){
            if(enemigo.estaOcupado())
                ocupado=true;
            enemigo.actualizar(tiempo);
            if(enemigo.estado == Estado.INACTIVO)
                enemigosDerrotados++;
        }

        if(resultadoCombate(enemigosDerrotados)==0&& !ocupado){
            //Gano el combate, por lo tanto sube la experiencia
            boolean sonarNivelUp=false;
            for(Personaje heroe:heroes){
                if(heroe.estado == Estado.ACTIVO){
                    heroe.reiniciarValores();
                    heroe.accion("Parado");
                }
                sonarNivelUp = heroe.subirNivel(heroe.nivel*35);
            }
            if(sonarNivelUp){
                GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LEVELUP_MAPA);
            }
            if(!ocupado) {
                if(jefe)
                    GameView.nivel.ganoJefe = 1;
                terminaCombate();
                GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_GANANCIA_COMBATE);
            }
        }else if (resultadoCombate(enemigosDerrotados)==1 && !ocupado){
            //Perdio el combate, por lo tanto baja de nivel
            for(Personaje heroe:heroes){
                if(heroe.estado == Estado.ACTIVO){
                    heroe.reiniciarValores();
                    heroe.accion("Parado");
                }
                heroe.bajarNivel();
            }
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LEVELDOWN_MAPA);
            if(!ocupado) {
                if(jefe)
                    GameView.nivel.ganoJefe = 0;
                terminaCombate();
                GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_PERDIDA_COMBATE);
            }
        }

    }

    public void dibujar(Canvas canvas) {
        fondo.dibujarSprite(canvas,GameView.pantallaAncho / 2, GameView.pantallaAlto / 2);

        for(Enemigo enemigo: enemigos){
            enemigo.dibujar(canvas);
        }

        for(Personaje heroe: heroes){
            heroe.dibujar(canvas);
        }
    }

    /**
     * El usuario ataca con su personaje, seleccionando a un enemigo. A continuación
     * deja paso al turno de los compañeros.
     */
    public void atacar(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            Personaje heroe = heroes.get(1);
            Enemigo enemigo = enemigos.get(GameView.enemigo);
            heroe.atacar(enemigo);
            enemigo.golpeado(heroe.tipo,heroe.daño);
        }
    }

    /**
     * El usuario se defiende, y sus compañeros le imitan. Puesto que en ese turno solo esta
     * defendiendo, se pasará al turno de los enemigos.
     */
    public void defender(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.ENEMIGO;
            heroes.get(1).bloquear();
            heroes.get(0).bloquear();
            heroes.get(2).bloquear();
        }
    }

    /**
     * El usuario realiza un hechizo con su personaje, seleccionando a un enemigo. A continuación
     * deja paso al turno de los compañeros.
     */
    public void magia(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            Personaje heroe = heroes.get(1);
            Enemigo enemigo = enemigos.get(GameView.enemigo);
            enemigo.golpeado(heroe.tipo,heroe.dañoMagico);
            heroe.magia(enemigo);
        }
    }

    /**
     * Mientras que tenga pociones, podrá reestablecer la vida de todos los heroes.
     * Esto consumirá un turno del jugador.
     */
    public void pocion() {
        if(pociones>0) {
            for (Personaje heroe : heroes) {
                heroe.restablecerVida();
            }
            pociones--;
            turno=Turno.COMPAÑEROS;
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_POCION_COMBATE);
        }
    }

    /**
     * El usuario deja el combate
     */
    public void huir() {
        if(!jefe) {
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_HUIR_COMBATE);
            terminaCombate();
            turno = Turno.JUGADOR;
        }else{
            GameView.nivel.ganoJefe=0;
            terminaCombate();
        }
    }

    /**
     * Cada enemigo activo atacará aleatoriamente a un heroe del campo que este activo también
     */
    public void turnoEnemigos(){
        if(turno==Turno.ENEMIGO) {
            for (Enemigo enemigo : enemigos){
                enemigosTerminados = enemigosTerminados + 1;
                if(enemigo.estado==Estado.ACTIVO &&!enemigo.utilizado && ((enemigoAtacando!=null && !enemigoAtacando.estaOcupado()) || enemigoAtacando==null)) {
                    enemigo.utilizado=true; //Ya se utilizo este heroe

                    int x = new Double(Math.random() * 3).intValue(); //Elije uno aleatoriamente
                    Personaje heroe = heroes.get(x);

                    if(heroe.estado == Estado.ACTIVO) { //Solo si el heroe no esta muerto ya
                        int daño = enemigo.golpear(heroe.tipo, heroe.nivel);
                        enemigoAtacando = enemigo;
                        heroe.golpeado(daño);

                        GameView.pintarDaño=x;
                        GameView.dañoActual=daño;
                    }
                }
            }
            if(enemigosTerminados >= enemigos.size() && (enemigoAtacando!=null && !enemigoAtacando.estaOcupado())){
                todosEnemigosUsados();
                enemigosTerminados=0;
                enemigoAtacando=null;
                turno= Turno.JUGADOR;//El jugador podrá volver a seleccionar una acción
            }
        }
    }

    /**
     * Cada compañero activo atacará aleatoriamente a un enemigo del campo que este activo también.
     */
    public void turnoCompañeros(){
        int compañerosMuertos=0;
        if(!heroes.get(1).estaBloqueando) {
            if (!heroes.get(1).estaOcupado() && turno == Turno.COMPAÑEROS) {
                for (Personaje heroe : heroes) {
                    if (heroe.estado == Estado.INACTIVO)
                        compañerosMuertos++;
                    if ((compañeroAtacando != null && !compañeroAtacando.estaOcupado()) || compañeroAtacando == null) {
                        if (heroe.tipo != 0 && heroe.estado == Estado.ACTIVO) {
                            compañerosTerminados = compañerosTerminados + 1;

                            int x = new Double(Math.random() * enemigos.size()).intValue();
                            Enemigo enemigo = enemigos.get(x);

                            heroe.accionAleatoria(enemigo);
                            heroe.utilizado=true;

                            if (enemigo.estado == Estado.ACTIVO)
                                enemigo.golpeado(heroe.tipo, heroe.daño);
                            else{
                                enemigo=primerEnemigoActivo(); //Buscará un enemigo que si este activo
                                if(enemigo!=null)
                                    enemigo.golpeado(heroe.tipo, heroe.daño);
                            }
                            compañeroAtacando = heroe;
                        }
                    }
                }
                if ((compañerosTerminados >= heroes.size() && !compañeroAtacando.estaOcupado()) || compañerosMuertos >= 2) {
                    todosCompañerosUsados();
                    todosEnemigosUsados();
                    compañerosTerminados = 0;
                    compañeroAtacando = null;
                    turno = Turno.ENEMIGO; //Turno de los compañeros terminado, pasamos el turno a los enemigos
                }
            }
        }else{
            turno = Turno.ENEMIGO;
        }
    }

    /**
     * @return el primer enemigo activo que encuentre del listado de enemigos
     */
    private Enemigo primerEnemigoActivo() {
        for (Enemigo enemigo:enemigos){
            if(enemigo.estado==Estado.ACTIVO)
                return enemigo;
        }
        return null;
    }

    /**
     * Inicializa todos los heroes para que esten listos para ser usados en posteriores
     * turnos
     */
    public void todosCompañerosUsados(){
        for(Personaje heroe:heroes){
            heroe.utilizado=false;
        }
    }

    /**
     * Inicializa todos los enemigos para que esten listos para ser usados en posteriores
     * turnos
     */
    public void todosEnemigosUsados(){
        for(Enemigo enemigo:enemigos){
            enemigo.golpeado=false;
            enemigo.sprite = enemigo.sprites.get("Parado");
            enemigo.utilizado=false;
        }
    }


    /**
     * Genera aleatoriamente los enemigos en el combate
     */
    private void generarEnemigos(){
        if(enemigos.size()==1){
            enemigos.remove(0);
        }else if(enemigos.size()==2){
            enemigos.remove(1);
            enemigos.remove(0);
        }else if(enemigos.size()==3){
            enemigos.remove(2);
            enemigos.remove(1);
            enemigos.remove(0);
        }
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

    /**
     * Genera un único enemigo, mas fuerte que los normales, para el combate.
     */
    private void generarBoss() {
        enemigos.add(new Boss(context, GameView.pantallaAncho / (3), GameView.pantallaAlto / (2.5)));
        this.fondo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.fondo_08),
                GameView.pantallaAncho, GameView.pantallaAlto,
                1, 1, false);
    }

    /**
     * Inicializa el combate, parando el mapa en el que el jugador se estaba moviendo con anterioridad
     * @param esBoss
     */
    public void iniciaCombate(boolean esBoss){
        if(enemigos.size()>0)
            enemigos.clear();
        GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_INICIO_COMBATE);
        GameView.nivel.nivelPausado=true;
        GameView.nivel.pararJugador();
        jefe = esBoss;
        if(esBoss)
            generarBoss();
        else
            generarEnemigos();
        this.enCombate=true;
        turno= Turno.JUGADOR;
    }

    private int resultadoCombate(int derrotados){
        if(derrotados==enemigos.size())
            return 0;
        if(heroes.get(1).estado==Estado.INACTIVO)
            return 1;
        return -1;
    }

    /**
     * Devuelve al jugador al mapa, y salimos del combate.
     */
    private void terminaCombate(){
        GameView.gestorAudio.reproducirMusicaAmbiente();
        this.enCombate = false;
        GameView.nivel.getJugador().x = jX;
        GameView.nivel.getJugador().y = jY;
        GameView.nivel.pararJugador();

        GameView.nivel.nivelPausado = false;
    }



}
