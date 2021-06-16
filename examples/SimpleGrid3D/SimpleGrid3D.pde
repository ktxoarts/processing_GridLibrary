import ktxo.art.processing.*;


Grid g1;

boolean vibrate = false;
int vibrateDIr = GridConstants.GRID_ALL;
int fillCol=-1;
int fillColor;


boolean moveGrid = false;
int direction=1;
float x=0;
float y=0;

int depth=0;

void setup() {
  size(400, 400, P3D);
  g1 = new Grid(this, 4, 4, 200, 200);
  g1.setPoinSize(4);
  g1.move(new PVector(100, 100));

  g1.dump(false);
}


void draw() {
  background(0);
  fill(255);
  text(String.format("%s\n%s\n%s\n%s", 
    "Left click to move the grid", 
    "a,x,y: toggle vibration", 
    "0,1,2: to change column colors", 
    "m: move grid"), 10, 10);

  if (mousePressed && (mouseButton == LEFT)) {
    g1.move(mouseX, mouseY);
  }
  g1.vibrate(vibrate, 20, vibrateDIr);

  if ( fillCol != -1) {
    g1.setGridColorColumn(fillCol, fillColor);
  }
  if ( moveGrid ) {
    PVector[] c = g1.getCorners();
    if ( c[0].x <= 20) {
      direction+=1;
      if ( depth > 10) {
        depth+=2;
      } else { 
        depth++;
      }
    } else if ( c[1].x >= 380 ) {
      direction-=1;
      if ( depth > 10) {
        depth+=2;
      } else { 
        depth++;
      }

    }
    x+=direction*0.5;
    g1.move(x, 100);
  }

  g1.render();
  if (moveGrid) {
    for (int i=0; i<depth; i++) {
      pushMatrix();
      translate(0, 0, -10*(i*2));
      g1.render();
      popMatrix();
    }
  }
}



void keyPressed() {
  if ( key == 'x' ) {
    vibrate=!vibrate;
    vibrateDIr=GridConstants.GRID_AX;
  } else   if ( key == 'y' ) {
    vibrate=!vibrate;
    vibrateDIr=GridConstants.GRID_AY;
  } else if ( key == 'a' ) {
    vibrate=!vibrate;
    vibrateDIr=GridConstants.GRID_ALL;
  } else if ( key == 'v' ) {
    vibrate=!vibrate;
  }
  if ( key == 'm' ) {
    moveGrid=!moveGrid;
    g1.move(new PVector(100, 100));
    x = 100;
  }
  if ( key == '0' ) {
    fillCol=0;
    fillColor=color(random(255), random(255), random(255));
  } else if ( key == '1' ) {
    fillCol=1;
    fillColor=color(random(255), random(255), random(255));
  } else if ( key == '2' ) {
    fillCol=2;
    fillColor=color(random(255), random(255), random(255));
  }
}
