package com.werwolv.render;

import com.werwolv.model.ModelRaw;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJModelLoader {

    public static ModelRaw loadObjModel(String fileName, ModelLoader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/models/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not load file!");
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals  = new ArrayList<>();
        List<Integer> indices   = new ArrayList<>();

        float[] verticesArray;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray;

        try {
            while(true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");

                if(line.startsWith("v "))
                    vertices.add(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));
                else if(line.startsWith("vt "))
                    textures.add(new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2])));
                else if(line.startsWith("vn "))
                    normals.add(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));
                else if(line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while(line != null) {
                if(!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);

                line = reader.readLine();

            }

            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertPtr = 0;
        for(Vector3f vert : vertices) {
            verticesArray[vertPtr++] = vert.x;
            verticesArray[vertPtr++] = vert.y;
            verticesArray[vertPtr++] = vert.z;
        }

        for(int i = 0; i < indices.size(); i++)
            indicesArray[i] = indices.get(i);

        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int currVertPtr = Integer.parseInt(vertexData[0]) -1;
        indices.add(currVertPtr);

        Vector2f currTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currVertPtr * 2 + 0] = currTex.x;
        textureArray[currVertPtr * 2 + 1] = 1 - currTex.y;

        Vector3f currNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currVertPtr * 3 + 0] = currNorm.x;
        normalsArray[currVertPtr * 3 + 1] = currNorm.y;
        normalsArray[currVertPtr * 3 + 2] = currNorm.z;

    }
}
