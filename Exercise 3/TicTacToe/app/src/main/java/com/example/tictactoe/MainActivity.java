package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TicTacToe tttGame;
    private Button[][] buttons;
    private TextView status;

    public String player1;
    public String player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tttGame = new TicTacToe();


        Configuration config = getResources().getConfiguration();
        modifyLayout(config);
    }

    public void buildGuiByCode(int w) {


        // create the Layout manager as a gridlayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(TicTacToe.SIDE);
        gridLayout.setRowCount(TicTacToe.SIDE + 1);

        // Create the buttons and add them to gridLayout
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler();
        for(int row = 0; row < TicTacToe.SIDE; row++) {
            for (int col = 0; col < TicTacToe.SIDE; col++) {
                buttons[row][col] = new Button(this);
                buttons[row][col].setTextSize((int) (w * .2));
                buttons[row][col].setOnClickListener(bh);
                gridLayout.addView((buttons[row][col]), w , w);
            }
        }

        // set up layout parameters of 4th row of gridLayout
        status = new TextView(this);
        GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE, 1);
        GridLayout.Spec columnSpec = GridLayout.spec(0, TicTacToe.SIDE);
        GridLayout.LayoutParams lpStatus = new GridLayout.LayoutParams(rowSpec, columnSpec);
        status.setLayoutParams(lpStatus);

        // set up status' characteristics
        status.setWidth(TicTacToe.SIDE * w);
        status.setHeight(w);
        status.setGravity(Gravity.CENTER);
        status.setBackgroundColor(Color.GREEN);
        status.setTextSize((int)(w * .15));
        status.setText(tttGame.result());

        gridLayout.addView(status);

        LinearLayout linear = new LinearLayout(this);
        linear.addView(gridLayout);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setGravity(LinearLayout.VERTICAL);

        // set gridLayout as the View of this activity
        setContentView(linear);
    }

    private void update(int row, int col) {
        int play = tttGame.play(row,col);
        if(play == 1)
            buttons[row][col].setText(player1);
        else if(play == 2)
            buttons[row][col].setText(player2);
        if(tttGame.isGameOver()) { //game over, disable buttons
            status.setBackgroundColor(Color.RED);
            enableButtons(false);
            status.setText(tttGame.result());
            showNewGameDialog(); // offer to play again
        }
    }

    private void enableButtons(boolean enabled) {
        for(int row = 0; row < TicTacToe.SIDE; row++)
            for(int col = 0; col < TicTacToe.SIDE; col++)
                buttons[row][col].setEnabled(enabled);
    }

    public void resetButtons(){
        for(int row = 0; row < TicTacToe.SIDE; row++)
            for(int col = 0; col < TicTacToe.SIDE; col++)
                buttons[row][col].setText("");
    }

    public void showNewGameDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play again?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("Yes", playAgain);
        alert.setNegativeButton("No", playAgain);
        alert.show();
    }

    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int row = 0; row < TicTacToe.SIDE; row++)
                for (int col = 0; col < TicTacToe.SIDE; col++)
                    if (v == buttons[row][col])
                        update(row, col);
        }
    }

    private class PlayDialog implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int id) {
            if(id == -1) /* YES button */ {
                tttGame.resetGame();
                enableButtons(true);
                resetButtons();
                status.setBackgroundColor(Color.GREEN);
                status.setText(tttGame.result());
            } else if(id == -2) // No button
                MainActivity.this.finish();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        modifyLayout(newConfig);
    }

    public void modifyLayout(Configuration config){
        Point size = new Point();
        getDisplay().getRealSize(size);
        int w;

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.w("MainActivity", "Landscape");
            w = size.y / TicTacToe.SIDE / 2;
            buildGuiByCode(w);
            player1 = "A";
            player2 = "Z";
        }
        else if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.w("MainActivity", "Portrait");
            w = size.x / TicTacToe.SIDE;
            buildGuiByCode(w);
            player1 = "X";
            player2 = "O";
        }
    }
}