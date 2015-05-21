package net.avicus.battleblobs.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.avicus.battleblobs.Battlefield;

public abstract class Entity {

    protected final Battlefield battlefield;
    protected final World world;

    public Entity(Battlefield battlefield) {
        this.battlefield = battlefield;
        this.world = battlefield.world;
    }

    public float distance(Entity entity) {
        return entity.getPosition().dst(entity.getPosition());
    }

    public float distanceSquared(Entity entity) {
        return entity.getPosition().dst2(entity.getPosition());
    }

    public abstract void act(float delta);

    public abstract void draw();

    public abstract Vector2 getPosition();

}
