/*
VBO (Vertex Buffer Object) = i dati dei vertici
VAO (Vertex Array Object) = Come vengono utilizzati questi vertici
Il VBO è un contenitore di memoria sulla GPU che contiene dati sui vertici, come la posizione, colori, coordinate texture...
Ma OpenGL non sa cosa significano questi numeri.
Il VAO dice da quale VBO prendere i dati, come interpretarli (i dati sono una posizione? Colore?), perciò a quale attributo
della shader corrispondono i dati presi.

Quando facciamo glBindBuffer(GL_ARRAY_BUFFER, VBO) stiamo dicendo che tutte le operazioni su GL_ARRAY_BUFFER affetteranno
quel VBO. GL_ARRAY_BUFFER è un slot di memoria che OpenGL usa appositamente nel suo stato globale. Facendo il Bind a GL_ARRAY_BUFFER
tutte le operazioni su di esso andranno ad influire sull'oggetto bindato, ovvero VBO.
-Creiamo il VAO e facciamo il Bind a un vertex array
-Creiamo il VBO e gli facciamo il bind del buffer e inseriamo elementi nel buffer
-Diciamo come leggere i dati tramite glVertexAttrbPointer e VertexAttrbArray

A ogni frame bisognerà specificare il shader program -> Fare il VAO bind al VBO e poi tramite altre funzione dire cosa farci con questi dati
Senza VAO dovremmo sempre specificare attributi dell'array e bind VBO etc...

Il VAO salva la configurazione dei dati di un certo VBO, vedendo quale VBO usare per ogni attributo, come interprestare i dati
e quali dati sono abilitati

VAO1 = glGenVertexArrays();
VBO1 = glGenBuffers();

glBindVertexArray(VAO1);
glBindBuffer(GL_ARRAY_BUFFER, VBO1);
glBufferData(GL_ARRAY_BUFFER, datiTriangolo1, GL_STATIC_DRAW);
glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*Float.BYTES, 0L);
glEnableVertexAttribArray(0);

// MESH 2
VAO2 = glGenVertexArrays();
VBO2 = glGenBuffers();

glBindVertexArray(VAO2);
glBindBuffer(GL_ARRAY_BUFFER, VBO2);  // ← Ora usi VBO2!
glBufferData(GL_ARRAY_BUFFER, datiTriangolo2, GL_STATIC_DRAW);
glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*Float.BYTES, 0L);
glEnableVertexAttribArray(0);

// RENDERING
glBindVertexArray(VAO1);  // Usa automaticamente VBO1
glDrawArrays(GL_TRIANGLES, 0, 3);

glBindVertexArray(VAO2);  // Usa automaticamente VBO2
glDrawArrays(GL_TRIANGLES, 0, 3);


The EBO stores indices and tells OpenGL which vertices to use and in what order, to not duplicate vertices. You reference
their position base on the vertex array you pass.
 */

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int VBO; //Stores large amout of vertices on the GPU (Vertex Buffer Object)
    private int VAO; //Vertex array object, vertex attribute calls are bound to it.
    private int EBO; //Element buffer object, we will store indices in it
    private int[] indices;
    private IntBuffer intBuffer;
    private Vertex3D[] vertices;
    private FloatBuffer floatBuffer;
    public Mesh(Vertex3D[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
        verticesToBuffer(vertices);
        indicesToBuffer(indices);
        //Trasformiamo Vertex3D[] in float[], dato che al buffer dobbiamo passare un array di primitivi, non oggetto Vertex3D
        setupMesh();
        debug();
    }

    private void setupMesh(){
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();
        //Tutte le configurazione successive verranno salvate in questo VAO
        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL_STATIC_DRAW);

        //---------Attribute settings------------
        //Quando abbiamo il vertex shader, ogni attributo che sia posizione o colore ha una posizione,
        //Con attribPointer posiamo scegliere la "posizione" per mettere dei settings.
        //Ogni glVertexAttribPointer sta a simboleggiare un set di proprietà del vertice-------------
        //glVertexAttribPointer(indice per il nostro tipo di informazione, ogni "classe" ne ha uno diverso
        // ,dimensione quanti dati ci sono per quel tipo di attributo RGB ne ha 3 RGBA ne ha 4
        // ,GL_FLOAT tipo di dato passato
        // , false
        // , 6*Float.BYTES Dimensione totale in byte di tutti gli attributi del vertice
        // , 0L); offset in byte da dove ogni vertice ha quell'attributo (3 float per posizione sono 12 byte, perciò dopo il 12esimo abbiamo i colori RGB)
        //Posizione
        glVertexAttribPointer(0,3,GL_FLOAT, false, 8*Float.BYTES, 0L);
        glEnableVertexAttribArray(0);
        checkError("after position attribute");
        //Colore
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*Float.BYTES, 12L);//float 4 byte.
        glEnableVertexAttribArray(1);
        checkError("after color attribute");
        //Posizione Texture
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 24L);//ha 6 float davanti ovvero 6*4 = 24
        glEnableVertexAttribArray(2);
        checkError("after texcoordinate attribute");




        //Togliamo il Bind visto che il VAO si ricorda
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void verticesToBuffer(Vertex3D[] vertices){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(this.vertices.length*8); //Creates buffer offheap.
        //lunghezza * 8 perchè sono 8 float per vertice.
        for (int i = 0; i < this.vertices.length; i++){
            buffer.put(vertices[i].getX());
            buffer.put(vertices[i].getY());
            buffer.put(vertices[i].getZ());
            buffer.put(vertices[i].getR());
            buffer.put(vertices[i].getG());
            buffer.put(vertices[i].getB());
            buffer.put(vertices[i].getTx());
            buffer.put(vertices[i].getTy());
        }
        //Pushes each vertex on the buffer
        buffer.flip();
        //Readies buffer for reading mode
        floatBuffer = buffer;
    }

    private void indicesToBuffer(int[] indices){
        IntBuffer buffer = BufferUtils.createIntBuffer(this.indices.length);
        for(int i = 0; i<this.indices.length; i++){
            buffer.put(indices[i]);
        }
        buffer.flip();
        intBuffer = buffer;
    }

    public void vaoBind(){
        glBindVertexArray(VAO);
    }

    public void debug(){
        // DEBUG
        System.out.println("=== MESH DEBUG ===");
        System.out.println("VAO ID: " + VAO);
        System.out.println("VBO ID: " + VBO);
        System.out.println("Numero vertici: " + vertices.length);
        System.out.println("Buffer capacity: " + floatBuffer.capacity());
        System.out.println("Buffer position: " + floatBuffer.position());

        // Stampa i dati del buffer
        floatBuffer.rewind();
        System.out.println("Dati buffer:");
        while(floatBuffer.hasRemaining()) {
            System.out.print(floatBuffer.get() + " ");
        }
        System.out.println("\n==================");

        // Stampa gli indici
        intBuffer.rewind();
        System.out.println("\nDati index buffer:");
        while(intBuffer.hasRemaining()) {
            System.out.print(intBuffer.get() + " ");
        }
        System.out.println("\n==================");
    }

    private void checkError(String step){
        System.out.println(step);
        int error = glGetError();
        if(error != GL_NO_ERROR) {
            System.out.println("ERRORE OPENGL in setupMesh: " + error);
        }
    }

}
