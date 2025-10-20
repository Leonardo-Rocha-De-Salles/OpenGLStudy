import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
public class Engine {
    private Window window;
    private Input input;
    private long windowHandle;
    private Texture texture;
    private Mesh mesh;
    private Mesh mesh2;
    private Shader shader;

    private Shader shader2;

    private Maths maths;


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

//

        shader2 = new Shader("shaders/vertexShader.glsl", "shaders/fragmentShader.glsl");

//

        maths = new Maths();

        //--------MESH---------------


        int[] indices = {0, 1, 2, // first triangle
                0,3,2
                };

        Vertex3D[] vertices = new Vertex3D[]{
                new Vertex3D(0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f),
                new Vertex3D(0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f),
                new Vertex3D(-0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f),
                new Vertex3D(-0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f)
        };



        maths.uniformSet(shader.getShader());


        //-----------------------------


        mesh = new Mesh(vertices, indices);

        //
        mesh2 = new Mesh(vertices, indices);
        //

        texture = new Texture("textures/wall.jpg");


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


            glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
            mesh.vaoBind();
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);

            maths.uniformSet(shader.getShader());

            //

            shader2.useProgram();
            mesh2.vaoBind();
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);

            maths.uniformSetScale(shader2.getShader());

            //

//Operations

//--------------------------
            window.bufferSwap();

            glfwPollEvents();
        }
        glfwTerminate();
    }
}