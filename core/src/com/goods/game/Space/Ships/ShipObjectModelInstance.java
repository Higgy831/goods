package com.goods.game.Space.Ships;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Ressources.Form;
import com.goods.game.Space.Ressources.RessourceType;
import com.goods.game.Space.Ressources.Storage;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

/**
 * Created by Higgy on 31.08.2017.
 */

public class ShipObjectModelInstance extends GameObjectModelInstance{
    private static ObjectType oType = ObjectType.Ship;

    // Ship Settings
    private Vector3 direction, direction4Rotation;
    private ShipType sType;
    private GameObjectModelInstance destination;
    private boolean isMoving;
    private float shipNormalTravelSpeed, shipRotationSpeed, shipWarpTravelSpeed;
    private float accelerationPower, decelerationPower, rotationPower;

    private float mass;
    private final float speedFactor = 0.002f, rotateFactor = 2f;
    private float minWarpDistance = 50f;
    private Storage[] storages;
    private Vector3 propPos;
    private float deltaTime;

    Quaternion rot;
    Quaternion tmpQ;


    public Vector3 getDirection() {
        return direction;
    }

    private void setDirection() {
        Vector3 norTarget;
        norTarget = new Vector3(destination.getPosition().cpy().sub(getPosition()));
        norTarget.nor();
        this.direction = norTarget;
        this.direction4Rotation = norTarget;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public GameObjectModelInstance getDestination() {
        return destination;
    }

    public void setDestination(GameObjectModelInstance destination) {
        if (this.destination == null || this.destination.getId() != destination.getId() ){
            rot = new Quaternion();
            tmpQ = new Quaternion();
        }
        this.destination = destination;
        setDirection();
    }

    private float calculateTravelSpeed(){
        float newSpeed;
        float distanceFactor, accelerationDecelerationFactor;
        if (canWarp()){
            // distance > minWarpDistance
            newSpeed = shipWarpTravelSpeed;
        }else{
            if (hasReachedDestination()){
                // distance < orbitDistance
                newSpeed = 0;
            }else{
                // distance between 10 and 50
                
                // Thrust calculation

                // Distance calculation
                distanceFactor = (shipNormalTravelSpeed* (getDistanceToDestination()- destination.getOrbitDistance()))/minWarpDistance;
                newSpeed = distanceFactor +10;
            }
        }
        return newSpeed * speedFactor;
    }

    private float calculateRotationSpeed(){
        return 0.1f;
    }


    Vector3 tmpV = new Vector3();
    int pitchDir =1, yawDir= 1;

    public void rotateToTarget() {


        // rotate Ship to Target
//        tmpQ.set(tmpV.set(direction4Rotation).crs(Vector3.Y),deltaTime*50);
//        rot.mulLeft(tmpQ);
//        setRotation(rot);
//        this.updateTransform();


        // welche achse muss in welche richtung rotiert werden? kürzeste winkel nehmen
        // Pitch Winkel berechnen

        tmpQ = new Quaternion();

        if(true){
            pitchDir =1;
        }else {
            pitchDir = -1;
        }
        if(false){
            yawDir =1;
        }else {
            yawDir = -1;
        }


        // Pitch
//        tmpQ.setEulerAngles(this.getRotation().getYaw(),pitchDir*rotateFactor*deltaTime,this.getRotation().getRoll());

        // Yaw Winkel berechnen

        // Yaw
       // tmpQ.setEulerAngles(yawDir*rotateFactor*deltaTime,this.getRotation().getPitch(),this.getRotation().getRoll());


        // both
        //tmpQ.setEulerAngles(direction4Rotation.x*yawDir*rotateFactor*deltaTime,direction4Rotation.y*pitchDir*rotateFactor*deltaTime,this.getRotation().getRoll());




        // rotiert zwar in die richtung stoppt aber nicht!
        //tmpQ.set(tmpV.set(direction4Rotation).crs(Vector3.Y),deltaTime*50);


        if (abs(rot.dot(getRotation())-1.0) < 0.001){
            // same direction
            return;
        }else{
//            tmpQ.setEulerAngles(direction4Rotation.x*yawDir*rotateFactor*deltaTime,direction4Rotation.y*pitchDir*rotateFactor*deltaTime,this.getRotation().getRoll());
//            rot.mulLeft(tmpQ);
//            setRotation(rot);
//            this.updateTransform();
        }












// Rotiert ship einmal direkt auf target
//        Quaternion q = new Quaternion();
//        Matrix4 mtx = new Matrix4();
//        mtx.rotate(direction4Rotation, Vector3.Y);
//        mtx.inv();
//        q.setFromMatrix(mtx);
//        setRotation(q);
//        this.updateTransform();
    }



    // notwendig?
    public void dockToTarget(Vector3 positionTarget, Vector3 scaleTarget, Quaternion rotationTarget){

    }

    private boolean isInDirection(){
        return false;
    }

    public void moveShip(GameObjectModelInstance target, float deltaTime){
        this.deltaTime = deltaTime;
        setDestination(target);
        rotateToTarget();
        moveToTarget();
    }

    private void moveToTarget() {
        float speed = calculateTravelSpeed();
        Vector3 tmp = getPosition();
        tmp.add(new Vector3(direction.x*speed,direction.y*speed,direction.z*speed));
        this.setPosition(tmp);
        this.updateTransform();
    }

    private float getDistanceToDestination(){
        Vector3 pos = this.getPosition();
        float dist  = pos.dst(destination.getPosition());
        return dist;
    }

    // TODO: 07.09.2017 warp nur wenn rotation auf target ausgerichtet ist und es ausserhalb eines "raumes" ist
    private boolean canWarp(){
        if (getDistanceToDestination()> 50){
            Material mat = this.materials.get(0);
            mat.clear();
            mat.set(new Material(ColorAttribute.createDiffuse(Color.LIME)));
            return true;
        }else{
            Material mat = this.materials.get(0);
            mat.clear();
            mat.set(new Material(ColorAttribute.createDiffuse(Color.GRAY)));
            return false;
        }
    }

    public boolean hasReachedDestination(){
        if (getDistanceToDestination()< destination.getOrbitDistance()){
            return true;
        }else{
            return false;
        }
    }

    // Helper
    public ShipObjectModelInstance(Model model, float size, ShipType sType, String name, int sotrages, float shipNormalTravelSpeed, float shipWarpTravelSpeed, float shipRotationSpeed, float accelerationPower, float decelerationPower, float rotationPower) {
        super(model, size, oType, name);
        this.sType = sType;
        this.shipNormalTravelSpeed = shipNormalTravelSpeed;
        this.shipRotationSpeed = shipRotationSpeed;
        this.shipWarpTravelSpeed = shipWarpTravelSpeed;
        this.decelerationPower = decelerationPower;
        this.accelerationPower = accelerationPower;
        this.rotationPower = rotationPower;
        this.storages = new Storage[sotrages];
        sb = new StringBuilder();
        sb1 = new StringBuilder();
    }

    private void rotatePropPos(){
        Matrix4 mtx = new Matrix4();
        mtx.set(getRotation());
        Vector3 offsetPropPos = new Vector3(getPosition());
        offsetPropPos.sub(propPos);
        offsetPropPos.rot(mtx);
        propPos.rot(mtx);
    }

    private void adjustPropPos(){
        // Rotate


        // Move



    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        propPos = position.cpy().sub(0,10,0);
    }

    @Override
    public void setRotation(Quaternion rotation) {
        super.setRotation(rotation);
        rotatePropPos();
    }

    public Vector3 getPropPos() {
        return propPos;
    }

    public void setStorageType(int storage, Form form, float storageSize){
        storages[storage] = new Storage(form,storageSize);
    }

    public float getAmountOfStorage(int storage){
        return storages[storage].getCurrentAmount();
    }

    // TODO: 07.09.2017 load und unload zuerst storages mit wenig resourcen leer machen
    public float loadRessource(RessourceType type, float amount){
        for (int i = 0; i < storages.length; i++) {
            amount = storages[i].load(type,amount);
        }
        return amount;

    }

    public float unloadRessource(RessourceType type, float amount){
        for (int i = 0; i < storages.length; i++) {
            amount = storages[i].unload(type,amount);
        }
        return amount;
    }

    public int getStoragesAmount(){
       return storages.length;
    }

    private String cargoToString(){
        sb1.delete(0, sb.length());
       // Material[60,100],Material[60,100],Material[60,100],Material[60,100]
        for (int i = 0; i < storages.length; i++) {
            sb1.append(storages[i].toString()+",");
        }
        sb1.replace(sb1.length()-1,sb1.length(),"");
        return sb1.toString();
    }

    private String routeToString(){
        if(destination != null){
            return destination.getType().name()+ " " +destination.getName();
        }else{
            return "no active route";
        }
    }

    StringBuilder sb, sb1;

    @Override
    public String toString() {
        sb.delete(0, sb.length());
        sb.append("Ship:");
        sb.append("\n"+sType.name() + " " + super.getName());
        sb.append("\nCargo:");
        sb.append("\n"+cargoToString());
        sb.append("\nTransport:");
        sb.append("\n"+routeToString());
       return sb.toString();
    }

}
