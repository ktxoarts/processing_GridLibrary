import ktxo.art.processing.*; //<>//

Grid g, gb;
int NX=6;
int NY=6;
int W=300;
int H=300;

PVector[] P_UP, P_BOTTOM, P_LEFT, P_RIGHT;

float factor=1;
int state=0;


//
void setup() {
  size(400, 400, P3D); 
  g = new Grid(this, NX, NY, 300, 300);
  g.move(width/2-W/2, height/2 -H/2);
  g.setGridContourWeight(2);
  g.setGridInternalWeight(1);
  g.setGridColor(color(255, 145, 0));
  g.setPoinSize(2);
  g.setPointColor(color(255, 100, 0));

  gb = g.clone();
  
  // Lines
  P_UP = new PVector[NX];
  P_BOTTOM = new PVector[NX];
  P_LEFT = new PVector[NY];
  P_RIGHT = new PVector[NY];

  float  offset=0;
  for (int i=0; i < g.getNumCols(); i++) {
    offset = 20 + 10* map(i%2, 0, 1, -1, 1);
    PVector pup = g.getPosFromCoordinates(i, 0);
    P_UP[i]= new PVector(pup.x, pup.y - offset );

    PVector pbot = g.getPosFromCoordinates(i, g.getNumCols()-1);
    P_BOTTOM[i]= new PVector(pbot.x, pbot.y + offset);
  }
  for (int i=0; i < g.getNumCols(); i++) {
    offset = 20 + 10* map(i%2, 0, 1, -1, 1);
    PVector pup = g.getPosFromCoordinates(0, i);
    P_LEFT[i]= new PVector(pup.x - offset, pup.y );

    PVector pbot = g.getPosFromCoordinates(g.getNumRows()-1, i);
    P_RIGHT[i]= new PVector(pbot.x + offset, pbot.y );
  }

  background(color(0));
}


// 
boolean checkOverlap(PVector[] s, PVector[] d, float factor) {
  float acc=0;
  for (int i=0; i< s.length; i++) {
    acc+=PVector.dist(s[i], d[i]);
  }
  if ( acc < factor) {
    return true;
  } else {
    return false;
  }
}

// 
void drawPoints() {
  stroke(color(color(34, 0, 255)));
  noFill();
  beginShape();
  for (PVector p : P_UP) { vertex(p.x, p.y); }
  endShape();
  
  beginShape();
  for (PVector p : P_BOTTOM) { vertex(p.x, p.y); }
  endShape();
  
  beginShape();
  for (PVector p : P_LEFT) { vertex(p.x, p.y); }
  endShape();
  
  beginShape();
  for (PVector p : P_RIGHT) { vertex(p.x, p.y); }
  endShape();
}

// 
void draw() {
  background(color(0));
  drawPoints();
  switch ( state) {
  case 0:
    g.moveColumnTo(0, P_LEFT, factor);
    if ( checkOverlap(g.getPointsOfColumn(0), P_LEFT, 1) ) { 
      state=1;
    }
    break;
  case 1:
    g.moveColumnTo(NX-1, P_RIGHT, factor);
    if ( checkOverlap(g.getPointsOfColumn(NX-1), P_RIGHT, 1) ) { 
      state=2;
    }
    break;
  case 2:
    g.moveRowTo(0, P_UP, factor);
    if ( checkOverlap(g.getPointsOfRow(0), P_UP, 1) ) { 
      state=3;
    }
    break;
  case 3:
    g.moveRowTo(NY-1, P_BOTTOM, factor);
    if ( checkOverlap(g.getPointsOfRow(NY-1), P_BOTTOM, 1) ) { 
      state=4;
    }
    break;  
  case 4:
    for (int i=0; i<g.getNumCols(); i++) {
      for (int j=0; j<g.getNumRows(); j++) {
        g.movePoint(new PVector(i, j), gb.getPosFromCoordinates(i, j), 1);
      }
    }    
    if ( checkOverlap(g.getPoints(), gb.getPoints(), 1) ) { 
      state=0;
    }
    break;
  }
  g.render();
}
