public class Lightning {

    public int[] getLuminescenseColour( float lum, float colDifR, float colDifG, float colDifB ){
        int[] col= {0,0,0};
        int pixCol1 = (int)(255.0f * lum * colDifR);
        int pixCol2 = (int)(255.0f * lum * colDifG);
        int pixCol3 = (int)(255.0f * lum * colDifB);
        if( pixCol1>0 && pixCol1<255 && pixCol2>0 && pixCol2<255 && pixCol3>0 && pixCol3<255 )
            col = new int[] {pixCol1, pixCol2, pixCol3};

        return col;
    }
}
