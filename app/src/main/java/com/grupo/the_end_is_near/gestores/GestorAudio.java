package com.grupo.the_end_is_near.gestores;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

public class GestorAudio implements MediaPlayer.OnPreparedListener {
    public static final int SONIDO_DISPARO_JUGADOR = 1;
    public static final int SONIDO_SALTO_JUGADOR = 2;
    public static final int SONIDO_ENEMIGO_GOLPEADO = 3;
    public static final int SONIDO_JUGADOR_GOLPEADO = 4;
    // Pool de sonidos, para efectos de sonido.
    // Suele fallar el utilizar ficheros de sonido demasiado grandes
    private SoundPool poolSonidos;
    private HashMap<Integer, Integer> mapSonidos;
    private Context contexto;
    // Media Player para bucle de sonido de fondo1.
    private MediaPlayer sonidoAmbiente;
    private MediaPlayer sonidoCombate;
    private AudioManager gestorAudio;

    private static GestorAudio instancia = null;

    public static GestorAudio getInstancia(Context contexto,
                                           int idMusicaAmbiente,int idMusicaCombate) {
        synchronized (GestorAudio.class) {
            if (instancia == null) {
                instancia = new GestorAudio();
                instancia.initSounds(contexto, idMusicaAmbiente,idMusicaCombate);
            }
            return instancia;
        }
    }


    public static GestorAudio getInstancia() {
        return instancia;
    }

    private GestorAudio() {
    }

    public void initSounds(Context contexto, int idMusicaAmbiente,int idMusicaCombate) {
        this.contexto = contexto;
        poolSonidos = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mapSonidos = new HashMap<Integer, Integer>();
        gestorAudio = (AudioManager) contexto
                .getSystemService(Context.AUDIO_SERVICE);
        sonidoAmbiente = MediaPlayer.create(contexto, idMusicaCombate);
        sonidoAmbiente.setLooping(true);
        sonidoAmbiente.setVolume(1, 1);

        sonidoCombate = MediaPlayer.create(contexto, idMusicaCombate);
        sonidoCombate.setLooping(true);
        sonidoCombate.setVolume(1, 1);
    }

    public void reproducirMusicaAmbiente() {
        try {
            pararMusicaCombate();
            if (!sonidoAmbiente.isPlaying()) {
                try {
                    sonidoAmbiente.setOnPreparedListener(this);
                    sonidoAmbiente.prepareAsync();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public void pararMusicaAmbiente() {
        if (sonidoAmbiente.isPlaying()) {
            sonidoAmbiente.stop();
        }
    }

    public void pararMusicaCombate(){
        if (sonidoCombate.isPlaying()) {
            sonidoCombate.stop();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void registrarSonido(int index, int SoundID) {
        mapSonidos.put(index, poolSonidos.load(contexto, SoundID, 1));
    }

    public void reproducirSonido(int index) {
        float volumen =
                gestorAudio.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumen =
                volumen / gestorAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        poolSonidos.play(
                (Integer) mapSonidos.get(index),
                volumen, volumen, 1, 0, 1f);
    }

    public void reproducirMusicaCombate() {
        try {
            pararMusicaAmbiente();
            if (!sonidoCombate.isPlaying()) {
                try {
                    sonidoCombate.setOnPreparedListener(this);
                    sonidoCombate.prepareAsync();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
