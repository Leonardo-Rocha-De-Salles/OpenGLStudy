/*
Texture coordinates range from 0 to 1 in the x and y axis.
If we specify coordinates outside the texture OpenGL by default repeats the texture
But OpenGL offers different options:
GL_REPEAT
GL_MIRRORED_REPEAT
GL_CLAMP_TO_EDGE
GL_CLAMP_TO_BORDER (need to specify border color)
Each of these options can be set on the x,y (and z) axis (s,t,r) coordinate system in openGL
with glTexParameter* function (GL_TEXTURE_2D, axis, option)

Texture Filterning: Texture coordinates do not depend on resolution, OpenGL has the job to figure
out which texture pixel (texel) to map to the texture coordinate. So we have texture filtering options.
GL_NEAREST: selects the texel that center is closest to the texture coordinate
GL_LINEAR: (bilinear filtering) takes an interpolated value of all neighbour texels.
We also have to specify texture settings for magnification (texture displayer larger than original size)
and Minification.

Mipmaps : If we have far away objects with a high resolution texture attached OpenGL will have difficulty
choosing the fragment color. So we use mipmaps, basically a collection of textures (of the same texture)
downsized by twice it's actual size, so OpenGL chooses a downsized version.
We do it with glGenerateMipmap after texture creation
 */

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private int textureID;
    public Texture(String path){
        generateTexture();
        loadTextureSTB(path);
    }

    public void loadTextureSTB(String path) {
        int textureID;
        //We create a temporary stack to make our jpeg reading fast, we could also just
        //use normal buffers though
        try (MemoryStack stack = MemoryStack.stackPush()) {
            //Push on stack width, height and channels (RGB, RGBA,...)
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(path, w, h, channels, 3);

            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
            }
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(0), h.get(0),
                    0, GL_RGB, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        }
    }
    private void generateTexture(){
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        //set texture wrapping and filtering options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public int getTextureID(){
        return textureID;
    }
}

