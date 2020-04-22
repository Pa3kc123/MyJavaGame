package sk.pa3kc.util.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import sk.pa3kc.pojo.RawModel;
import sk.pa3kc.pojo.matrix.Vector2f;
import sk.pa3kc.pojo.matrix.Vector3f;
import sk.pa3kc.util.Loader;

public class ObjLoader {

    public static RawModel loadObjModel(String filename, Loader loader) throws FileNotFoundException {
        if (filename == null || loader == null) {
            throw new NullPointerException();
        }

        final File file = new File(filename);

        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " does not exists");
        }

        ArrayList<Vector3f> verticies = new ArrayList<>();
        ArrayList<Vector2f> textures = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Integer> indicies = new ArrayList<>();

        float[] verticiesArr = null;
        float[] texturesArr = null;
        float[] normalsArr = null;
        int[] indiciesArr = null;

        try (final BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            mainLoop:
            for (; line != null; line = br.readLine()) {
                if (line.startsWith("#") || line.length() == 0) continue;

                final String[] splits = line.split(" ");

                switch (splits[0]) {
                    // Vertex position
                    case "v":
                        verticies.add(new Vector3f(
                            Float.parseFloat(splits[1]),
                            Float.parseFloat(splits[2]),
                            Float.parseFloat(splits[3])
                        ));
                    break;

                    // Vertex texture
                    case "vt":
                        textures.add(new Vector2f(
                            Float.parseFloat(splits[1]),
                            Float.parseFloat(splits[2])
                        ));
                    break;

                    // Vertex normal
                    case "vn":
                        normals.add(new Vector3f(
                            Float.parseFloat(splits[1]),
                            Float.parseFloat(splits[2]),
                            Float.parseFloat(splits[3])
                        ));
                    break;

                    // Vertex indicies (faces)
                    case "f":
                        texturesArr = new float[verticies.size() * 2];
                        normalsArr = new float[normals.size() * 3];
                    break mainLoop;
                }
            }

            final int[][] vers = new int[3][3];
            for (; line != null; line = br.readLine()) {
                if (line.startsWith("#") || line.length() == 0) continue;

                final String[] splits = line.split(" ");

                for (int groupIndex = 1; groupIndex < 4; groupIndex++) {
                    final String[] tmp = splits[groupIndex].split("/");

                    for (int valIndex = 0; valIndex < 3; valIndex++) {
                        vers[groupIndex - 1][valIndex] = Integer.parseInt(tmp[valIndex]);
                    }
                }

                for (int groupId = 0; groupId < vers.length; groupId++) {
                    final int currVerPointer = vers[groupId][0] - 1;
                    indicies.add(currVerPointer);

                    final Vector2f currTex = textures.get(vers[groupId][1] - 1);
                    texturesArr[currVerPointer * 2] = currTex.x;
                    texturesArr[currVerPointer * 2 + 1] = 1 - currTex.y;

                    final Vector3f currNormal = normals.get(vers[groupId][2] - 1);
                    normalsArr[currVerPointer * 3] = currNormal.x;
                    normalsArr[currVerPointer * 3 + 1] = currNormal.y;
                    normalsArr[currVerPointer * 3 + 2] = currNormal.z;
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        verticiesArr = new float[verticies.size() * 3];
        indiciesArr = new int[indicies.size()];

        int index = 0;
        for (Vector3f vertex : verticies) {
            verticiesArr[index++] = vertex.x;
            verticiesArr[index++] = vertex.y;
            verticiesArr[index++] = vertex.z;
        }

        for (int i = 0; i < indicies.size(); i++) {
            indiciesArr[i] = indicies.get(i);
        }

        return loader.loadModelToVAO(verticiesArr, texturesArr, normalsArr, indiciesArr);
    }
}
