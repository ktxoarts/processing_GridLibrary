import ktxo.art.processing.*;
import java.util.Arrays;


Grid g1;

// Face coordinates
float[] FX=  {10, 15, 10, 15, -5, 0, -15, -15, 0, -5, 5, -40, -40};
float[] FY=  {0, 60, 70, 120, 125, 150, 160, 160, 170, 190, 215, 230, 270};
float rotation=0;


// Build a grid from Face coordinates
Point[][] buildFace(float[] XS, float[] YS, float depth, int middle) {
  float[] xs = Arrays.copyOf(XS, XS.length);
  float[] ys = Arrays.copyOf(YS, YS.length);
  int middlenum=2+middle;
  Point[][] face = new Point[xs.length][middlenum];

  int j=0;
  // Get minor distance
  float div = Float.MAX_VALUE;
  for (float f : xs) {
    if ( (depth+f) < div) {
      div=depth+f;
    }
  }
  div /= middle;
  for (int i =0; i< xs.length; i++) {
    face[i][0]= new Point(this,0F, ys[j]);
    for (int k =1; k<= middle; k++) {
      face[i][k]= new Point(this, k*div, ys[j]);
    }
    face[i][middlenum-1]= new Point(this, xs[i]+depth, ys[j]);
    j++;
  }
  return face;
}


void setup() {
  size(400, 400,P3D);
  g1 = new Grid(this, buildFace(FX, FY,150F, 4));
  g1.setPoinSize(1);

}


void draw() {
  background(0);
  translate(190, 60,-40);
  rotateY(rotation+=0.01);
  g1.render();
  
}
