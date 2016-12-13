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

    public List<Enemigo> enemigos;
    public List<Personaje> heroes;

    private Sprite fondo;
    public long millis;

    public int turno= Turno.JUGADOR;

    public Enemigo enemigoAtacando=null;
    public int enemigosTerminados=0;

    public Personaje compañeroAtacando=null;
    public int compañerosTerminados=0;
    public int pociones;

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

        enCombate=false; //TODO debe ser false. Por el momento true para probarlo
        turnoEnemigos();
    }

    public void actualizar(long tiempo) {
        int enemigosDerrotados = 0;

        turnoCompañeros();
        turnoEnemigos();

        boolean ocupado=false;
        for(Personaje heroe:heroes){
            if(turno==Turno.JUGADOR && heroe.estaBloqueando) {
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
            //TODO AnimacionGanar + sumar experiencia y si sube de nivel recuperar vida.
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
                terminaCombate();
                GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_GANANCIA_COMBATE);
            }
        }else if (resultadoCombate(enemigosDerrotados)==1 && !ocupado){
            //TODO AnimacionPerder + volver al mapa volviendo a la entrada pero con un nivel menos.
            for(Personaje heroe:heroes){
                if(heroe.estado == Estado.ACTIVO){
                    heroe.reiniciarValores();
                    heroe.accion("Parado");
                }
                heroe.bajarNivel();

            }
            GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LEVELDOWN_MAPA);
            //nivel.VolverPosada=true;
            if(!ocupado) {
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

    public void atacar(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            Personaje heroe = heroes.get(1);
            Enemigo enemigo = enemigos.get(GameView.enemigo);
            heroe.atacar(enemigo);
            enemigo.golpeado(heroe.tipo,heroe.daño);
        }
    }

    public void defender(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.ENEMIGO;
            heroes.get(1).bloquear();
            heroes.get(0).bloquear();
            heroes.get(2).bloquear();
        }
    }

    public void magia(){
        if(turno==Turno.JUGADOR) {
            turno=Turno.COMPAÑEROS;
            Personaje heroe = heroes.get(1);
            Enemigo enemigo = enemigos.get(GameView.enemigo);
            enemigo.golpeado(heroe.tipo,heroe.dañoMagico);
            heroe.magia(enemigo);
        }
    }

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
    public void huir() {
        GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_HUIR_COMBATE);
        terminaCombate();
        turno = Turno.JUGADOR;
    }

    public void turnoEnemigos(){
        if(turno==Turno.ENEMIGO) {
            for (Enemigo enemigo : enemigos){
                enemigosTerminados = enemigosTerminados + 1;
                if(enemigo.estado==Estado.ACTIVO &&!enemigo.utilizado && ((enemigoAtacando!=null && !enemigoAtacando.estaOcupado()) || enemigoAtacando==null)) {
                    enemigo.utilizado=true;
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
            if(enemigoAtacando!=null)
                System.out.println(enemigosTerminados+","+enemigos.size()+","+enemigoAtacando.estaOcupado());
            if(enemigosTerminados >= enemigos.size() && (enemigoAtacando!=null && !enemigoAtacando.estaOcupado())){
                todosEnemigosUsados();
                enemigosTerminados=0;
                enemigoAtacando=null;
                turno= Turno.JUGADOR;
            }
        }
    }

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
                            //enemigoAtacando = enemigo;
                            heroe.accionAleatoria(enemigo);
                            heroe.utilizado=true;
                            if (enemigo.estado == Estado.ACTIVO)
                                enemigo.golpeado(heroe.tipo, heroe.daño);
                            else{
                                enemigo=primerEnemigoActivo();
                                if(enemigo!=null)
                                    enemigo.golpeado(heroe.tipo, heroe.daño);
                            }
                            compañeroAtacando = heroe;
                        }
                    }
                }
                if ((compañerosTerminados >= heroes.size() && !compañeroAtacando.estaOcupado()) || compañerosMuertos >= 2) {
                    todosCompañerosUsados();
                    compañerosTerminados = 0;
                    compañeroAtacando = null;
                    turno = Turno.ENEMIGO;
                }
            }
        }else{
            turno = Turno.ENEMIGO;
        }
    }

    private Enemigo primerEnemigoActivo() {
        for (Enemigo enemigo:enemigos){
            if(enemigo.estado==Estado.ACTIVO)
                return enemigo;
        }
        return null;
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

    private void iniciarEnemigosAleatorios() {
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

    private void generarBoss() {
        enemigos.add(new Boss(context, GameView.pantallaAncho / (3), GameView.pantallaAlto / (2.5)));
    }

    public void iniciaCombate(boolean esBoss){
        GameView.gestorAudio.reproducirSonido(GestorAudio.SONIDO_INICIO_COMBATE);
        GameView.nivel.nivelPausado=true;
        GameView.nivel.pararJugador();
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

    private void terminaCombate(){
        GameView.gestorAudio.reproducirMusicaAmbiente();
        this.enCombate = false;
        GameView.nivel.getJugador().x = jX;
        GameView.nivel.getJugador().y = jY;
        GameView.nivel.pararJugador();

        GameView.nivel.nivelPausado = false;
    }



}
