package com.goods.game.Space.Stars;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;

/**
 * Created by Higgy on 31.08.2017.
 */

public class StarObjectModelInstance extends GameObjectModelInstance{
    private static ObjectType oType = ObjectType.Star;

    private Vector3 rotationVector3;
    // Planet Settings

    // Helper
    public StarObjectModelInstance(Model model, float size, String name) {
        super(model, size, oType, name);
        setSelfRotationSpeed(0.5f);
        setRandomRotation();
    }

    private void setRandomRotation(){
        int a,b;
        do{
            a = MathUtils.random(1);
            b = MathUtils.random(1);
        }while(a+b!=1);
        rotationVector3 = new Vector3(a,0,b);
    }


    public Vector3 getFace(){
        return rotationVector3;
    }



    @Override
    public String toString() {
        return oType.name()+super.getId()+getOrbitObjectsToString();
    }

}
