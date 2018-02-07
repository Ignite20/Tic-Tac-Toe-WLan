package com.tests.amadalingradinaru.tictactoe;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;
import com.tests.amadalingradinaru.model.Message;
import com.tests.amadalingradinaru.utils.TTTConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class BoardActivity extends AppCompatActivity implements SalutDataCallback{


    private final static String TAG = "BOARD_GAME";

    GridView gvBoard;

    Button btnRestart;


    ArrayList<String> marks = new ArrayList<String>(){
        {
            for (int i = 0; i < 9; i++){
                add(" ");
            }
        }
    };

    private boolean isXTurn = true;

    private boolean gameIsOver = false;
    ArrayAdapter<String> adapter;

    boolean isOnline;
    boolean isServer;

    private Salut network;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        gvBoard = findViewById(R.id.gv_board);
        btnRestart = findViewById(R.id.btn_restart);

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

        isOnline = getIntent().getExtras().getBoolean(TTTConstants.BK_IS_ONLINE);
        isServer = getIntent().getExtras().getBoolean(TTTConstants.BK_IS_SERVER);

        SalutDataReceiver dataReceiver = new SalutDataReceiver(this, this);
        SalutServiceData serviceData = new SalutServiceData("sas", 50489, "TTT instance");

        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });

        Log.d("TYPE_INSTANCE",isServer+"");

        if(isOnline){
            if(isServer){
                network.startNetworkService(new SalutDeviceCallback() {
                    @Override
                    public void call(SalutDevice device) {
                        Log.d(TAG, device.readableName + " has connected!");
                    }
                });
            }else {
                network.discoverNetworkServices(new SalutDeviceCallback() {
                    @Override
                    public void call(SalutDevice device) {
                        network.registerWithHost(device, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.d(TAG, "We're now registered.");
                            }
                        }, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.d(TAG, "We failed to register.");
                            }
                        });
                        Log.d(TAG, "A device has connected with the name " + device.deviceName);
                    }
                }, false);


            }

        }

        initGame();


    }


    private void initGame(){
        adapter  = new ArrayAdapter<>(this,R.layout.mark_layout,marks);

        gvBoard.setAdapter(adapter);

        gvBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(marks.get(i).equalsIgnoreCase(" ")) {

                    if (isXTurn && network.isRunningAsHost) {
                        marks.set(i,"X");


                        Message mMessage = new Message();
                        mMessage.description = "u wot m8";
                        mMessage.mark = "X";
                        mMessage.position = i;

                        network.sendToAllDevices(mMessage, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.e(TAG, "Oh no! The data failed to send.");
                            }
                        });

                        isXTurn = false;
                    } else {
                        marks.set(i,"O");
                        Message mMessage = new Message();
                        mMessage.description = "u wot m8 again?";
                        mMessage.mark = "O";
                        mMessage.position = i;

                        network.sendToHost(mMessage, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.e(TAG, "Oh no! The data failed to send.");
                            }
                        });
                        isXTurn = true;
                    }
                }
                if(checkWinner(marks,"X")) {
                    Toast.makeText(BoardActivity.this, "Player X Won", Toast.LENGTH_SHORT).show();
                    gameIsOver = true;
                }
                else if(checkWinner(marks,"O")) {
                    Toast.makeText(BoardActivity.this, "Player O Won", Toast.LENGTH_SHORT).show();
                    gameIsOver = true;
                }
                else if(checkForDraw(marks)) {
                    Toast.makeText(BoardActivity.this, "Game is Draw", Toast.LENGTH_SHORT).show();
                    gameIsOver = true;
                }
                checkGameStatus();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void checkGameStatus(){

        if(gameIsOver) {
            btnRestart.setVisibility(View.VISIBLE);
            gvBoard.setEnabled(false);
        }

    }

    void restartGame(){
        gvBoard.setEnabled(true);
        marks.clear();
        Collections.addAll(marks, new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ",});
        gameIsOver = false;
        btnRestart.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();

    }

    private boolean checkForDraw(ArrayList<String> marks){
        boolean draw = true;
        int count = 0;

        for (String value :
                marks) {
            if(value.equalsIgnoreCase(" "))
                count++;
        }

        if(count > 0)
            draw = false;

        return draw;
    }

    private boolean checkWinner(ArrayList<String> marks, String player){
        boolean playerWon = false;

        if((marks.get(0).equalsIgnoreCase(player) && marks.get(1).equalsIgnoreCase(player) && marks.get(2).equalsIgnoreCase(player))
                || (marks.get(3).equalsIgnoreCase(player) && marks.get(4).equalsIgnoreCase(player) && marks.get(5).equalsIgnoreCase(player))
                || (marks.get(6).equalsIgnoreCase(player) && marks.get(7).equalsIgnoreCase(player) && marks.get(8).equalsIgnoreCase(player))){
            playerWon = true;
        }

        else if((marks.get(0).equalsIgnoreCase(player) && marks.get(3).equalsIgnoreCase(player) && marks.get(6).equalsIgnoreCase(player))
                || (marks.get(1).equalsIgnoreCase(player) && marks.get(4).equalsIgnoreCase(player) && marks.get(7).equalsIgnoreCase(player))
                || (marks.get(2).equalsIgnoreCase(player) && marks.get(5).equalsIgnoreCase(player) && marks.get(8).equalsIgnoreCase(player))){
            playerWon = true;
        }

        else if((marks.get(0).equalsIgnoreCase(player) && marks.get(4).equalsIgnoreCase(player) && marks.get(8).equalsIgnoreCase(player))
                || (marks.get(6).equalsIgnoreCase(player) && marks.get(4).equalsIgnoreCase(player) && marks.get(2).equalsIgnoreCase(player))){
            playerWon = true;
        }

        return playerWon;

    }

    private void toggleTurn(){
        if(isXTurn)
            isXTurn = false;
        else
            isXTurn = true;
    }



    @Override
    public void onDataReceived(Object o) {
        Log.d(TAG, "Received network data.");
        try
        {
            Log.d(TAG, o.toString());  //See you on the other side!


            Message newMessage = LoganSquare.parse(o.toString(), Message.class);
            marks.set(newMessage.position,newMessage.mark);
            adapter.notifyDataSetChanged();
            checkGameStatus();
            toggleTurn();
            //Log.d(TAG, newMessage.description);  //See you on the other side!
            //Do other stuff with data.
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to parse network data."+ ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(isServer)
            network.stopNetworkService(false);
        else {
            if(!network.isRunningAsHost){
                if(network.isConnectedToAnotherDevice){
                    network.cancelConnecting();
                }
            }

        }
    }
}
