import java.lang.Math;

public class Engine{
	public int screenHeight;
	public int screenWidth;
	private float aspect;
	private float totViewAngle=90.0f;
	private final float zstart = 0.1f;
	private final float zend = 1000.0f;
	public float viewAngleRad=1.0f/(float)Math.tan(totViewAngle * 0.5f / 180.0f * 3.14159f);
	
	public Engine(int width, int height){
		screenHeight=height;
		screenWidth=width;
		aspect = (float)screenWidth / (float)screenHeight;
	}

	float[][] rotateTriangleX( float[][] triangle, float thetaX ){
		float[][] rotatedTriangle = new float[3][3];
		for(int numVertices=0; numVertices < triangle.length; numVertices++){
			rotatedTriangle[numVertices][0] = triangle[numVertices][0];
			rotatedTriangle[numVertices][1] = triangle[numVertices][1] * (float)Math.cos(thetaX/2.0f)-triangle[numVertices][2] * (float)Math.sin(thetaX/2.0f);
			rotatedTriangle[numVertices][2] = triangle[numVertices][1] * (float)Math.sin(thetaX/2.0f)+triangle[numVertices][2] * (float)Math.cos(thetaX/2.0f);
		}
		return rotatedTriangle;
	}

	float[][] rotateTriangleY( float[][] triangle, float thetaY ){
		float[][] rotatedTriangle = new float[3][3];
		for(int numVertices=0; numVertices < triangle.length; numVertices++){
			rotatedTriangle[numVertices][0] = triangle[numVertices][0] * (float)Math.cos(thetaY/2.0f)-triangle[numVertices][2] * (float)Math.sin(thetaY/2.0f);
			rotatedTriangle[numVertices][1] = triangle[numVertices][1];
			rotatedTriangle[numVertices][2] = triangle[numVertices][0] * (float)Math.sin(thetaY/2.0f)+triangle[numVertices][2] * (float)Math.cos(thetaY/2.0f);
		}
		return rotatedTriangle;
	}

	float[][] rotateTriangleZ( float[][] triangle, float thetaZ ){
		float[][] rotatedTriangle = new float[3][3];
		for(int numVertices=0; numVertices < triangle.length; numVertices++){
			rotatedTriangle[numVertices][0] = triangle[numVertices][0] * (float)Math.cos(thetaZ)-triangle[numVertices][1] * (float)Math.sin(thetaZ);
			rotatedTriangle[numVertices][1] = triangle[numVertices][0] * (float)Math.sin(thetaZ)+triangle[numVertices][1] * (float)Math.cos(thetaZ);
			rotatedTriangle[numVertices][2] = triangle[numVertices][2];
		}
		return rotatedTriangle;
	}

	float[][] scaleTriangle( float[][] triangle, float scaleX, float scaleY, float scaleZ){
		float[][] scaledTriangle = new float[3][3];
		
		for(int numVertices=0; numVertices < triangle.length; numVertices++){
			scaledTriangle[numVertices][0] = triangle[numVertices][0]*scaleX;
			scaledTriangle[numVertices][1] = triangle[numVertices][1]*scaleY;
			scaledTriangle[numVertices][2] = triangle[numVertices][2]*scaleZ;
		}
		return scaledTriangle;
	}

	float[][] transformTriangleTo2D(float[][] triangle) {
		float[][] transformedTriangle = new float[3][3];
		float normVec;
		for(int numVertices = 0; numVertices < triangle.length; numVertices++){
			transformedTriangle[numVertices][0] = triangle[numVertices][0] * aspect * viewAngleRad;
			transformedTriangle[numVertices][1] = triangle[numVertices][1] * viewAngleRad;
			transformedTriangle[numVertices][2] = (triangle[numVertices][2] * zend/(zend-zstart)) - zend * zstart/(zend - zstart);
				
			if(triangle[numVertices][2] != 0.0f ){
				transformedTriangle[numVertices][0]/=triangle[numVertices][2];
				transformedTriangle[numVertices][1]/=triangle[numVertices][2];
				transformedTriangle[numVertices][2]/=triangle[numVertices][2];
			}
		}
		return transformedTriangle;
	}

	float[] normalizeVector( float[] vector ){
		float[] normVector = new float[ 3 ];
		float normConst;
		normConst = (float)Math.sqrt( vector[0]*vector[0] +
									  vector[1]*vector[1] +
									  vector[2]*vector[2] );
		normVector[0]=vector[0]/normConst;
		normVector[1]=vector[1]/normConst;
		normVector[2]=vector[2]/normConst;

		return normVector;
	}

	float[][] translateTriangle( float[][] triangle, float transX, float transY, float transZ){
		float[][] transTriangle = new float[3][3];
		for(int numVertices = 0; numVertices < triangle.length; numVertices++){
			transTriangle[numVertices][0]=triangle[numVertices][0]+transX;
			transTriangle[numVertices][1]=triangle[numVertices][1]+transY;
			transTriangle[numVertices][2]=triangle[numVertices][2]+transZ;
		}
		return transTriangle;
	}

	float[] crossProduct( float[][] triangle){
		float[] v1 = new float[3];
		float[] v2 = new float[3];
		float[] crossVec = new float[3];
		float[] normVec;// = new float[3];

		v1[0] = triangle[1][0] - triangle[0][0];
		v1[1] = triangle[1][1] - triangle[0][1];
		v1[2] = triangle[1][2] - triangle[0][2];

		v2[0] = triangle[2][0] - triangle[0][0];
		v2[1] = triangle[2][1] - triangle[0][1];
		v2[2] = triangle[2][2] - triangle[0][2];

		crossVec[0] = v1[1] * v2[2] - v2[1] * v1[2];
		crossVec[1] = -1.0f * ( v1[0] * v2[2] - v2[0] * v1[2] );
		crossVec[2] = v1[0] * v2[1] - v2[0] * v1[1];

		normVec = normalizeVector( crossVec );

		return normVec;
	}

	float dotProduct( float[] v1, float[] v2){
		return (v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2]);
	}
}