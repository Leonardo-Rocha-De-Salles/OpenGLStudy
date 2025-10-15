/*

A shader program always begins with a version declarion, followed by a list of input, outputs uniform type and a main function.
The objective of our main function is to transform the input data in output data for our out variables.

#version version_number
in type in_variable_name;
in type in_variable_name;
out type out_variable_name;
uniform type uniform_name;
void main()
{
// process input(s) and do some weird graphics stuff
...
// output processed stuff to output variable
out_variable_name = weird_stuff_we_processed;
}

 */
import static org.lwjgl.opengl.GL30.*;
public class Shader {
    private static final String VERTEX_SHADER = """
        #version 330 core
        layout (location = 0) in vec3 aPos; //attribute position 0
        layout (location = 1) in vec3 aColor; //attribute position 1
        
        out vec3 ourColor;
        
        void main() {
        gl_Position = vec4(aPos, 1.0);
        ourColor = aColor;
        }
        """;
    private static final String FRAGMENT_SHADER = """
        #version 330 core
        out vec4 FragColor;
        in vec3 ourColor;
        
        void main() {
            FragColor = vec4(ourColor, 1.0);
        }
        """;
    private int vertexShader, fragmentShader, shaderProgram;

    public Shader(){
        vertexShader = createShader(GL_VERTEX_SHADER, VERTEX_SHADER);
        fragmentShader = createShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
        setShaderProgram();
        deleteShader();
    }

    public int createShader(int ShaderType, String shaderCode){
        int shaderID = glCreateShader(ShaderType);//Create vertex shader and save it's ID in a variable
        glShaderSource(shaderID, shaderCode);//Now we attach the shader source to the object ID (so the shader code)
        glCompileShader(shaderID);//We compile it.


        //LOG ERRORS
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            String infoLog = glGetShaderInfoLog(shaderID);
            if (ShaderType == GL_VERTEX_SHADER) {
                System.out.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" + infoLog);
            }
            else{
                System.out.println("ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" + infoLog);
            }
        }
        return shaderID;
    }

    public void setShaderProgram(){
        shaderProgram = glCreateProgram(); //we create a program id now we have to attach the previously created shaders
        //Each shader will work like a pipeline with their inputs and outputs
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            String infoLog = glGetProgramInfoLog(shaderProgram);
            System.out.println("ERROR::PROGRAM::LINKING::LINKING_FAILED\n" + infoLog);
        }
        //After linking we specify we are going to use this program, and from now on every shader and rendering call will
        //Use our shader program.
        glUseProgram(shaderProgram);
    }

    public void useProgram(){
        glUseProgram(shaderProgram);
    }

    public void deleteShader(){
        //We can Cleanup After linking
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public int getShader(){
        return shaderProgram;
    }

}
