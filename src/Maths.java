import org.joml.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL30.*;
public class Maths {
    private Matrix4f trans;
    private Matrix4f trans2;
    public Maths(){
        trans = new Matrix4f();
        //
        trans2 = new Matrix4f();
    }
    public void uniformSet(int shaderID){
        trans.identity();
        //Try using this instead and watch a trippy animation
        //trans.rotate((float)Math.toRadians(glfwGetTime()), 0.0f,0.0f,1.0f); //rotate on z axis
        trans.rotate((float)Math.toRadians(glfwGetTime()), 0.0f,0.0f,1.0f);
        try(MemoryStack stack = MemoryStack.stackPush()){//Creo stack buffer
            FloatBuffer fb = stack.mallocFloat(16); //Matrice 4x4
            trans.get(fb);
            int uniformLocation = glGetUniformLocation(shaderID, "transform");
            glUniformMatrix4fv(uniformLocation, false, fb);
        }
    }
//
    public void uniformSetScale(int shaderID){
        trans2.identity();
        trans2.scale((float)Math.sin(glfwGetTime()),(float)Math.sin(glfwGetTime()),(float)Math.sin(glfwGetTime()));
        try(MemoryStack stack = MemoryStack.stackPush()){//Creo stack buffer
            FloatBuffer fb = stack.mallocFloat(16); //Matrice 4x4
            trans2.get(fb);
            int uniformLocation = glGetUniformLocation(shaderID, "transform");
            glUniformMatrix4fv(uniformLocation, false, fb);
        }
    }
    //
}
