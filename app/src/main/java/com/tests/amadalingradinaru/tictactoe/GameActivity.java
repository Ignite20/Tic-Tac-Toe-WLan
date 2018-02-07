package com.tests.amadalingradinaru.tictactoe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tests.amadalingradinaru.utils.TTTConstants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        Button btnStartGame = findViewById(R.id.btn_start_game);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "offline room");
                startGame(false,false);
            }
        });

        Button btnSearchRoom = findViewById(R.id.btn_search_players);
        btnSearchRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "search room");
                startGame(true,false);
            }
        });

        Button btnCreateRoom = findViewById(R.id.btn_create_room);
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "create room");
                startGame(true,true);
            }
        });
    }

    @OnClick(R.id.btn_start_game)
    void startOfflineGame(){

    }

    @OnClick(R.id.btn_search_players)
    void startOnlineGame(){

    }

    @OnClick(R.id.btn_create_room)
    void createRoom(){

    }


    void startGame(boolean isOnline, boolean isThisServer){

        Bundle bundle = new Bundle();
        bundle.putBoolean(TTTConstants.BK_IS_ONLINE, isOnline);
        bundle.putBoolean(TTTConstants.BK_IS_SERVER, isThisServer);
        intent = new Intent(GameActivity.this, BoardActivity.class);

        intent.putExtras(bundle);
        startActivity(intent);
    }
}


