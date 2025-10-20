import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Input {
    private final long window;
    private boolean polyFill;

    public Input(long window){
        this.window = window;
        polyFill=true;
        setupMouseCallBack();
    }


    private void setupMouseCallBack(){
        //Il callback viene chiamato solo quando il mouse cambia di stato, non richiamandolo ogni frame.
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS){
                    polyFill = !polyFill;
                    if(polyFill){
                        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    } else {
                        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                    }
                }
            }
        });
    }


    public void processInput(){
        if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS){
            glfwSetWindowShouldClose(window, true);
        }
    }
}
