import java.util.Random;

public class Landscape {
    private int gridSize;
    public Landscape( int gs ){
        gridSize = gs;
    }

// Assuming clockwise rotation of the triangles implying that the normal
// of the facing side will be positive.

    public float[][][] makeLandscape(){
        final float gridDelta = 1 / (float) gridSize;
        final int numTriangles = gridSize * gridSize *2;
        Random randomVariable = new Random();
        float[][][] tri = new float[numTriangles][3][3];
        float[][] grid = new float[gridSize][gridSize];
        grid[0][0] = (float)Math.random()*gridDelta;
        int k=0;

        for(int i = 1 ; i< gridSize; i++){
            grid[0][i] = grid[0][i - 1] + (float)(randomVariable.nextGaussian())*gridDelta*10.0f;
            grid[i][0] = grid[i - 1][0] + (float)(randomVariable.nextGaussian())*gridDelta*10.0f;
        }


        for(int i = 1; i < gridSize; i++){
            for(int j = 1; j < gridSize; j++){
                grid[i][j] = 0.5f * ( grid[i][j-1] + grid[i-1][j])+ (float)(randomVariable.nextGaussian())*gridDelta*2.0f;
            }
        }

        for(int i=0; i + 1 < gridSize; i++){
            for(int j=0; j + 1 < gridSize; j++){
                tri[k][2][0] = gridDelta * (float)j;
                tri[k][2][2] = gridDelta * (float)i;
                tri[k][2][1] = grid[i][j];
                tri[k][1][0] = gridDelta * (float)j;
                tri[k][1][2] = gridDelta * (float)(i + 1);
                tri[k][1][1] = grid[i + 1][j];
                tri[k][0][0] = gridDelta * (float)(j + 1);
                tri[k][0][2] = gridDelta * (float)(i + 1);
                tri[k][0][1] = grid[i + 1][j + 1];

                tri[k + 1][2][0] = gridDelta * (float)(j + 1);
                tri[k + 1][2][2] = gridDelta * (float)(i + 1);
                tri[k + 1][2][1] = grid[i + 1][j + 1];
                tri[k + 1][1][0] = gridDelta * (float)(j + 1);
                tri[k + 1][1][2] = gridDelta * (float)i;
                tri[k + 1][1][1] = grid[i][j + 1];
                tri[k + 1][0][0] = gridDelta * (float)j;
                tri[k + 1][0][2] = gridDelta * (float)i;
                tri[k + 1][0][1] = grid[i][j];
                k+=2;
            }
        }
        return tri;
    }
}
