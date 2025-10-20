import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
//Static permette di chiamare il metodo direttamente senza fare GL11.method
public class Window {
    private final long window;
    private final int width;
    private final int height;

    public Window(){
        width = 1920;
        height = 1080;
        this.window = glfwCreateWindow(width,height,"LearnOpenGL",0,0);
        if(window == 0){
            System.out.println("Failed to create LearnOpenGL window");
            glfwDestroyWindow(this.window);
            glfwTerminate();
            return;
        }
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities(); //Importante da chiamare dopo il constesto per creare le funzionalità necessarie.
        glViewport(0, 0, width, height);
        /*Creiamo una finestra, diciamo di renderla il contesto del thread corrente, e successivamente diciamo a OpenGL
        Come vogliamo che la window sia renderizzata.
        In questo caso le coordinate (-0.5,0.5) sono (200,450)
        */


        //---------------OPTIONS-----------------
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);


        glfwSetFramebufferSizeCallback(window, this::framebuffer_size_callback); //tell openGL to call the function every time window is resized

    }

    public void update(){
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        /*
        Whenever we call glClear and clear the color buffer,
        the entire color buffer will be filled with the color as configured by glClearColor.
         */
    }

    public void bufferSwap(){
        glfwSwapBuffers(window);
    }


    public void framebuffer_size_callback(long window, int width, int height){
        glViewport(0, 0, width, height);
    }

    public long getWindow(){return window;}
}
