package top.nowandfuture;

public class Vec3d {
    public double x, y, z;

    public Vec3d(){
        x = y = z = 0;
    }

    public Vec3d(double x, double y , double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(double x, double y){
        this(x, y, 0);
    }

    public Vec3d add(Vec3d other){
        return new Vec3d(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vec3d sub(Vec3d other){
        return new Vec3d(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vec3d scale(float sc){
        return new Vec3d(this.x * sc, this.y * sc, this.z * sc);
    }

    public double normalize(){
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double dot(Vec3d other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public static Vec3d ZERO(){
        return new Vec3d();
    }



}
