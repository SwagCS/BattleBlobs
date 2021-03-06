package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.avicus.battleblobs.Battlefield;

public class AI extends Blob {

    public AI(Battlefield battlefield, float cx, float cy, float area, Color color) {
        super(battlefield, cx, cy, area, color);
    }

    private float time;
    private float last;
    private Blob placeholderBlob;
    private float timeSinceEscapeStart = 2;
    private float timeSinceChaseStart = 2;
    @Override
    public void act(float delta) {
        super.act(delta);
        timeSinceChaseStart += delta;
        timeSinceEscapeStart += delta;
        time += delta;
        if (time - last < 0.3)
            return;
        last = time;

        float dist = 1000000000000000f;
        Vector2 dir = new Vector2(0,0);
        float x, y;


        for(int i = 0; i < battlefield.entities.size(); i++) {
            //if something is bigger and close run away
           if(battlefield.entities.get(i) instanceof Blob && (Math.sqrt(((Blob) battlefield.entities.get(i)).area/3.15)) > this.radius() && this.distance(battlefield.entities.get(i)) < 5 * Math.sqrt(((Blob) battlefield.entities.get(i)).area / 3.15)) {
                placeholderBlob = (Blob) battlefield.entities.get(i);
                x = placeholderBlob.getPosition().x - getPosition().x;
                y = placeholderBlob.getPosition().y - getPosition().y;
                dir = new Vector2(-x,-y);
                timeSinceEscapeStart = 0;
            }
           //pay more attention to things > 1/3 its size
           else if(battlefield.entities.get(i) instanceof Blob &&(Math.sqrt(((Blob) battlefield.entities.get(i)).area/3.14)) < this.radius() && this.distance(battlefield.entities.get(i)) < this.radius()*5 && ((Blob) battlefield.entities.get(i)).radius() > this.radius()/3 && timeSinceEscapeStart > .1){
               placeholderBlob = (Blob) battlefield.entities.get(i);
               x = placeholderBlob.getPosition().x - getPosition().x;
               y = placeholderBlob.getPosition().y - getPosition().y;
               dir = new Vector2(x,y);
               timeSinceChaseStart = 0;
           }

           //eat the closest thing smaller than it
           else  if(battlefield.entities.get(i) instanceof Blob && (Math.sqrt(((Blob) battlefield.entities.get(i)).area/3.14)) < this.radius() && this.distance(battlefield.entities.get(i)) < dist && timeSinceEscapeStart > .1 && timeSinceChaseStart > .1) {
                placeholderBlob = (Blob) battlefield.entities.get(i);
                dist = this.distance(placeholderBlob);
                x = placeholderBlob.getPosition().x - getPosition().x;
                y = placeholderBlob.getPosition().y - getPosition().y;
                dir = new Vector2(x,y);
            }
        }

        dir.nor();
        dir.scl((float)(5/Math.cbrt(this.area/3.14)));

        for(Body body : border)
            body.setLinearVelocity(dir);
        center.setLinearVelocity(dir);
    }

}
