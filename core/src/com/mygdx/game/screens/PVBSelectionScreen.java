package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.Omega;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;
import com.mygdx.game.bots.*;
import com.mygdx.game.coordsystem.Hexagon;

public class PVBSelectionScreen implements Screen {
    Omega game;
    Skin menuSkin;
    Stage stage;
    SelectBox bots1;
    TextButton confirm;
    int index;

    public PVBSelectionScreen(Omega game) {
        super();
        stage = new Stage();
        this.game = game;
        stage = new Stage(new FillViewport(1280, 720));
        menuSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        bots1 = new SelectBox<String>(menuSkin);
        bots1.setItems("Random bot", "FitnessGroup bot", "MaxN Paranoid", "OneLookAhead bot", "MCTree bot");
        bots1.setPosition(750, 250);
        bots1.setSize(200, 50);

        confirm = new TextButton("Confirm", menuSkin);
        confirm.setColor(Color.BLACK);
        confirm.setPosition(820, 300);

        stage.addActor(bots1);
        stage.addActor(confirm);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.90f, 1.00f, 1.00f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bots1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                index = bots1.getSelectedIndex();
            }
        });

        switch(index){
            case 1:{ 
                new RandomBot();
            }
            case 2:{
                new FitnessGroupBot(Hexagon.state.RED,Hexagon.state.BLUE,false);
            }
            case 3:{
                new MaxN_Paranoid_Bot(Hexagon.state.BLUE,Hexagon.state.RED)
            }
            case 4:{
                new OLABot();
            }
            case 5:{
                new TreeBotMC(5, 10);
            }
        }
        game.setScreen(new LoadingScreen(game, true, false));
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }
}
