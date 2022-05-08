package top.nowandfuture.jmediapipe;

import top.nowandfuture.jmediapipe.math.Vec3d;

import java.util.LinkedList;

public enum Vec3dPool {
    POOL(478 * 2);
    private LinkedList<Vec3d> pool;
    private int maxSize;

    Vec3dPool(int maxSize){
        this.maxSize = maxSize;
        this.pool = new LinkedList<>();
    }

    public Vec3d get(double x, double y){
        if(!pool.isEmpty()){
            Vec3d ret = pool.removeFirst();
            ret.x = x;
            ret.y = y;
            ret.z = 0;
            return ret;
        }

        return new Vec3d(x,y,0);
    }

    public Vec3d get(double x, double y, double z){
        if(!pool.isEmpty()){
            Vec3d ret = pool.removeFirst();
            ret.x = x;
            ret.y = y;
            ret.z = z;
            return ret;
        }

        return new Vec3d(x, y, z);
    }

    public Vec3d get(){
        if(!pool.isEmpty()){
            return pool.removeFirst();
        }

        return new Vec3d();
    }

    public void recycle(Vec3d vec3d){
        if(pool.size() < maxSize){
            pool.addLast(vec3d);
        }
    }
}
