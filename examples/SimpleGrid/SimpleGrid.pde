import ktxo.art.processing.*;


Grid g1;

boolean vibrate = false;
int vibrateDIr = GridConstants.GRID_ALL;
int colId=-1;
int colColor=-1;
float colWeight=-1;

boolean moveGrid = false;
int direction=1;
float x=0;
float y=0;


void setup() {
  size(400, 400, P3D);
  g1 = new Grid(this, 4, 4, 200, 200);
  g1.move(new PVector(100, 100));
  frameRate(50);
  g1.dump(false);
}


void draw() {
  background(0);
  fill(255);
  text(String.format("%s\n%s\n%s\n%s\n%s\n%s", 
    "Left click to move the grid", 
    "a,x,y: toggle vibration", 
    "0,1,2,3: change column weight", 
    "4,5,6,7: change column color", 
    "m: move grid", 
    "r: reset"), 10, 10);

  // move
  if (mousePressed && (mouseButton == LEFT)) {
    g1.move(mouseX, mouseY);
  }
  if ( keyPressed) {
    switch (key) {  
    case 'x': vibrate=true; vibrateDIr=GridConstants.GRID_AX;  break;
    case 'y': vibrate=true; vibrateDIr=GridConstants.GRID_AY;  break;
    case 'a': vibrate=true; vibrateDIr=GridConstants.GRID_ALL; break;
    case 'v': vibrate=true; break;    
    case 'm': moveGrid=!moveGrid; break;
      
    case '0': colId=0; colWeight=random(1,10); break;
    case '1': colId=1; colWeight=random(1,10); break;
    case '2': colId=2; colWeight=random(1,10); break;
    case '3': colId=3; colWeight=random(1,10); break;
    
    case '4': colId=0; colColor=color(random(255), random(255), random(255)); break;
    case '5': colId=1; colColor=color(random(255), random(255), random(255)); break;
    case '6': colId=2; colColor=color(random(255), random(255), random(255)); break;
    case '7': colId=3; colColor=color(random(255), random(255), random(255)); break;
    
    case 'r': 
      g1.setGridInternalWeight(Grid.GRID_WEIGHT);
      g1.setGridContourWeight(Grid.GRID_WEIGHT);
      g1.setGridInternalColor(Grid.GRID_COLOR);
      g1.setGridContourColor(Grid.GRID_COLOR);
      g1.setPoinSize(Grid.POINT_SIZE);
      g1.setPointColor(Grid.POINT_COLOR);
      vibrate=false;
      moveGrid=false;
      g1.move(new PVector(100, 100));
      colId=-1;
      colColor=-1;
      colWeight=-1;
      break;
  }

  }
  if ( colWeight != -1){ 
    g1.setGridWeightColumn(colId,colWeight); 
    colWeight=-1; 
    colId=-1;
  }
  if ( colColor  != -1){
    g1.setGridColorColumn(colId,colColor)  ; 
    colColor=-1;
    colId=-1;
  }
  if ( moveGrid ){
      PVector[] c = g1.getCorners();
      if ( c[0].x <= width*0.1) {
        direction+=1;
      } else if ( c[1].x >= width*0.9 ) {
        direction-=1;
      }
      x=direction*0.5;
      g1.move(c[0].x+x, 100);
  }
  g1.vibrate(vibrate, 20, vibrateDIr);
  g1.render();
}
