import ktxo.art.processing.*; 


Grid g1;
Grid gOri;

boolean vibrate = false;
int col=-1;
PVector p;

void setup() {
  size(400, 400, P3D);
  g1 = new Grid(this, 4, 4, 200, 200);
  g1.move(new PVector(100, 100));
  gOri= g1.clone();
  g1.dump(false);
}


void draw() {
  background(0);
  fill(255);
  text(String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s", 
    "Left click to move the grid", 
    "x: expand grid on X", 
    "y: expand grid on Y",
    "a: expand grid on X and Y",
    "p: change size of random point", 
    "s: change color and weight of random segment", 
    "r: reset weight"), 10, 10);

  if (mousePressed && (mouseButton == LEFT)) {
    g1.move(mouseX, mouseY);
  }
  
  ////
  if ( keyPressed) {
    switch (key) {  
    case 'x': g1.expand(1, GridConstants.GRID_AX); break;
    case 'y': g1.expand(1, GridConstants.GRID_AY); break;
    case 'a': g1.expand(1, GridConstants.GRID_ALL); break;
    
    case 'v': vibrate=!vibrate; break; 
    
    case 'p':
      p = new PVector((int)random(0, 4), (int)random(0, 4));
      println(p.x, p.y);
      g1.setPoinSize(p, random(3, 10));
      g1.setPointColor(p, color(random(255), random(255), random(255)));
      break;
    case 's':
      int c= color(random(255), random(255), random(255));
      p = new PVector((int)random(0, 3), (int)random(0, 3));
      println(p.x, p.y);
      g1.setSegmentColor(p, c, GridConstants.GRID_AX);
      g1.setSegmentColor(p, c, GridConstants.GRID_AY);
      g1.setSegmentWeight(p, random(5, 10), GridConstants.GRID_AX);
      break;
    case 'r': 
      g1 = gOri.clone();
      vibrate=false;
      g1.move(new PVector(100, 100));
      break;
    }
  }

  g1.vibrate(vibrate, 20, GridConstants.VIBRATE_ALL);
  g1.render();
}
