import static org.lwjgl.glfw.GLFW.*;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL30.*;
public class Experiments {
    private double timeValue;
    private float greenValue;
    private int vertexColorLocation;
    private int shaderProgram;
    public Experiments(){
    }

    public void setShaderProgram(int shaderProgram){
        timeValue = glfwGetTime();
        greenValue = (float) ((sin(timeValue)/2.0f) + 0.5f);
        this.shaderProgram = shaderProgram;
        vertexColorLocation = glGetUniformLocation(shaderProgram, "ourColor");
    }

    public int getVertexColorLocation() {
        return vertexColorLocation;
    }

    public void setVertexColorLocation(int vertexColorLocation) {
        this.vertexColorLocation = vertexColorLocation;
    }

    public float getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(float greenValue) {
        this.greenValue = greenValue;
    }
}
