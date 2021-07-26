import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.BufferCapabilities;
import java.awt.ImageCapabilities;
import java.awt.AWTException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;

public class MultiThreadedBrownianLandscapes extends JFrame{

	private float[][][] mesh;
	public int sleepTime = 5;
	private final int screenWidth, screenHeight;
	public float deltaDeg = 0.0f;
	public float deltaDegX = 1.8f;
	private float xdir=0.0f;
	private float ydir=0.0f;
	private float zdir=-0.1f;
	private float colDifR=1.0f;
	private float colDifG=1.0f;
	private float colDifB=1.0f;
	private int numTriangles = 1;
	private int trianglesPerThread = 1;
	private final int numThreads = 2;
	private float[][] translatedTriangle;
	private float[][] transformedTriangle;
	private float[][] rotatedTriangleX;
	private float[][] rotatedTriangleY;
	private float[][] scaledTriangle;
	private float[] normVec, planeVec, normalizedVector;
	private int[] col;
	private float dp;
	private int[] xp = new int[3];
	private int[] yp = new int[3];

	public final Engine eng;
	private final Landscape ls;
	private final Lightning light;
	private BufferStrategy bs;
	private BufferCapabilities bufCap;
	private ImageCapabilities imgBackBufCap; 
	private ImageCapabilities imgFrontBufCap;
	private Graphics2D gg;

	public MultiThreadedBrownianLandscapes( int width, int height, int gridSize ){
		screenWidth = width;
		screenHeight = height;
		eng = new Engine( width, height );
		ls = new Landscape( gridSize );
		light = new Lightning();
		this.setTitle("Alien landscapes");
		this.setVisible( true );
		
		addMouseWheelListener(
				e -> {

       				if( e.getWheelRotation() < 0 && !e.isControlDown() && !e.isShiftDown() && !e.isAltDown() ){
       					eng.viewAngleRad+=0.1;
       				}

       				if( e.getWheelRotation() > 0 && !e.isControlDown() && !e.isShiftDown() && !e.isAltDown()){
	   					eng.viewAngleRad-=0.1;
       				}

       				if( e.getWheelRotation() < 0 && e.isControlDown() && !e.isShiftDown() && !e.isAltDown()){
       					sleepTime++;
       				}

       				if( e.getWheelRotation() > 0 && e.isControlDown() && !e.isShiftDown() && !e.isAltDown()){
       					sleepTime--;
       				}

       				if( e.getWheelRotation() < 0 && e.isAltDown() && !e.isShiftDown() && !e.isControlDown()){
       					deltaDegX+=0.1;
       				}

       				if( e.getWheelRotation() > 0 && e.isAltDown() && !e.isShiftDown() && !e.isControlDown()){
       					deltaDegX-=0.1;
       				}

       				if( e.isShiftDown() )
       					mesh = ls.makeLandscape();

					repaint();
        		}
		);
	}

	@Override
	public void update( Graphics g ){
		if( mesh == null ){
			mesh = ls.makeLandscape();
			numTriangles = mesh.length;
			trianglesPerThread = mesh.length/numThreads;
		}

		Thread[] myThreads = new Thread[ numThreads ];
		imgBackBufCap = new ImageCapabilities( true );
		imgFrontBufCap = new ImageCapabilities( true );
		bufCap = new BufferCapabilities(imgFrontBufCap, imgBackBufCap, BufferCapabilities.FlipContents.COPIED);
		try{ createBufferStrategy( 2, bufCap );
		} catch (AWTException ex){
			createBufferStrategy( 2 );
		}
		bs = this.getBufferStrategy();

		gg = (Graphics2D)bs.getDrawGraphics();
		gg.setColor( Color.BLACK );
		gg.fillRect(0, 0, screenWidth, screenHeight);

		for( int tIDx = 0; tIDx < numThreads; tIDx++ ){
				
			final int k = tIDx;
			myThreads[tIDx] = new Thread(() -> {
				for(int i = k * trianglesPerThread; i < (k + 1 ) * trianglesPerThread; i++){
					plotTriangles( mesh[i], gg );
				}
			});
			myThreads[tIDx].start();
			try{
				myThreads[tIDx].join();
			}
			catch(InterruptedException ie){
				System.out.println("Thread-joining exception:"+ie.toString());
			}
		}

		if( !bs.contentsLost() )
			bs.show();
		gg.dispose();
	}

	@Override
	public void paint(Graphics g){
		update( g );
	}

	private void plotTriangles( float[][] triangle, Graphics2D g ){

		float[] lightVec = { xdir, ydir, zdir };
		translatedTriangle = eng.translateTriangle( triangle, -0.5f, -0.5f, -0.5f );
		rotatedTriangleY = eng.rotateTriangleY( translatedTriangle, deltaDeg);
		rotatedTriangleX = eng.rotateTriangleX( rotatedTriangleY, deltaDegX);
		translatedTriangle = eng.translateTriangle( rotatedTriangleX, 0.0f, 0.0f, 5.0f);
		normVec = eng.crossProduct( translatedTriangle );
		planeVec = translatedTriangle[0];
		if( eng.dotProduct( normVec, planeVec ) < 0.0f ){
			transformedTriangle = eng.transformTriangleTo2D( translatedTriangle );
			translatedTriangle = eng.translateTriangle( transformedTriangle, 1.0f, 1.0f, 0.0f);
			scaledTriangle = eng.scaleTriangle(translatedTriangle, (float)eng.screenWidth*0.5f, (float)eng.screenHeight*0.5f, 1.0f);
			lightVec = eng.normalizeVector( lightVec );
			normalizedVector = eng.normalizeVector( triangle[0] );
			dp = Math.max( 0.1f, normalizedVector[2]*eng.dotProduct( lightVec, normVec) );
			col = light.getLuminescenseColour( dp, colDifR, colDifG, colDifB );
			for(int i = 0; i<3; i++ ){
			 	xp[i] = (int)scaledTriangle[i][0];
			  	yp[i] = (int)scaledTriangle[i][1];
			}
			g.setColor( new Color( col[0], col[1], col[2] )  );
			g.fillPolygon( xp, yp, 3);
		}
	}

	public static void main(String[] args){
		MultiThreadedBrownianLandscapes l = new MultiThreadedBrownianLandscapes( 1280, 768, 200 );
		l.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		l.setSize(1280, 768 );
		do {
			l.deltaDeg += 0.01;
			l.repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(l.sleepTime);
			} catch (InterruptedException ie) {
				System.out.println("Timer interrupted");
			}

		} while (true);
	}
}
