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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Shader {
    private int shaderProgram;
    public Shader(String vertexPath, String fragmentPath){
        String vertexCode = readFiles(vertexPath);
        String fragmentCode = readFiles(fragmentPath);

        int vertexShader = createShader(GL_VERTEX_SHADER, vertexCode);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentCode);

        setShaderProgram(vertexShader, fragmentShader);
        deleteShader(vertexShader, fragmentShader);
    }

    public String readFiles(String path){
        try{
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            System.err.println("ERROR::SHADER::FILE_NOT_SUCCESSFULLY_READ: " + path);
            e.printStackTrace();
            return "";
        }
    }

    public int createShader(int shaderType, String shaderCode){
        int shaderID = glCreateShader(shaderType);//Create vertex shader and save it's ID in a variable
        glShaderSource(shaderID, shaderCode);//Now we attach the shader source to the object ID (so the shader code)
        glCompileShader(shaderID);//We compile it.

        //LOG ERRORS
        checkCompileErrors(shaderID, shaderType);

        return shaderID;
    }

    public void setShaderProgram(int vertexShader, int fragmentShader){
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

    public void deleteShader(int vertexShader, int fragmentShader){
        //We can Cleanup After linking
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public int getShader(){
        return shaderProgram;
    }

    public void checkCompileErrors(int shaderID, int shaderType){
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            String infoLog = glGetShaderInfoLog(shaderID);
            if (shaderType == GL_VERTEX_SHADER) {
                System.out.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" + infoLog);
            }
            else{
                System.out.println("ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" + infoLog);
            }
        }
    }

    void setBool(String name, boolean value) {
        glUniform1i(glGetUniformLocation(shaderProgram, name), value ? 1:0);
    }
    void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(shaderProgram, name), value);
    }
    void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(shaderProgram, name), value);
    }

}
