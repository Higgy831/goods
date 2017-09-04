package com.goods.game.Space.Shapes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Created by Higgy on 04.09.2017.
 */

public interface Shape {
    public abstract boolean isVisible(Matrix4 transform, Camera cam);
    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    public abstract float intersects(Matrix4 transform, Ray ray);
}
