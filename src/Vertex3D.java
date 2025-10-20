public class Vertex3D {
    private float x, y, z, r, g, b, tx, ty;

    public Vertex3D(float x, float y, float z, float r, float g, float b, float tx, float ty){
        //Position-------------
        this.x = x;
        this.y = y;
        this.z = z;
        //---------------------
        //Color----------------
        this.r = r;
        this.g = g;
        this.b = b;
        //---------------------
        //Texture Coordinates--
        this.tx = tx;
        this.ty = ty;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getTx() {
        return tx;
    }

    public float getTy() {
        return ty;
    }

}
