

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
public class Engine {
    private Experiments experiments;

    private Window window;
    private Input input;
    private long windowHandle;
    private Mesh mesh;
    private Shader shader;
    public Engine(){
        init();
        runLoop();
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
    }

    public void init(){
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = new Window();
        this.windowHandle = window.getWindow();
        input = new Input(windowHandle);
        shader = new Shader("shaders/vertexShader.glsl", "shaders/fragmentShader.glsl");


        int[] indices = {0, 1, 2, // first triangle
                };
        Vertex3D[] vertices = new Vertex3D[]{
                new Vertex3D(-0.5f, -0.5f, 0.0f,1.0f,0.0f,0.0f),
                new Vertex3D(0.5f, -0.5f, 0.0f,0.0f,1.0f,0.0f),
                new Vertex3D(0f, 0.5f, 0.0f,0.0f,0.0f,1.0f)
        };


        mesh = new Mesh(vertices, indices);
        experiments = new Experiments();
        int error = glGetError();
        if(error != GL_NO_ERROR) {
            System.out.println("ERRORE OPENGL dopo mesh creation: " + error);
        }
    }

    private void runLoop(){
        while(!glfwWindowShouldClose(windowHandle)){
            input.processInput();
            window.update();


//-----Rendering Loop-------
            shader.useProgram();

            shader.setFloat("offSet", -0.3f);

            mesh.vaoBind();
            glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
//--------------------------

            window.bufferSwap();
            glfwPollEvents();
        }
        glfwTerminate();
    }
}