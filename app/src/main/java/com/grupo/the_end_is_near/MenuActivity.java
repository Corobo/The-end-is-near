package com.grupo.the_end_is_near;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuActivity extends Activity implements View.OnClickListener {

    Button botonIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        botonIniciar = (Button) findViewById(R.id.boton_iniciar);
        botonIniciar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==botonIniciar){
            Intent actividadJuego = new Intent(MenuActivity.this,
                    MainActivity.class);
            startActivity(actividadJuego);
            finish();
        }
    }
}
