package com.mygdx.game.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

//COPIED FROM THE INTERNET. W.r.t https://libgdx.com/wiki/graphics/2d/scene2d/scene2d
//NEEDS REFACTORING
//PLEASE IGNORE THIS CODE
public class menuActor extends Actor {

    private ShapeRenderer renderer;

    public menuActor() {
        renderer = new ShapeRenderer();

    }

    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin();
        renderer.setColor(Color.WHITE);
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }
}
