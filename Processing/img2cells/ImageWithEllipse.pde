class ImageWithEllipse 
{
    PImage sourceImg;
    PGraphics targetG;
    PGraphics mapG;
    
    float d;
    int scaleLevel;
    ArrayList<Cell> cells;
    color c;

    ImageWithEllipse(PImage sourceImg) {
      this.sourceImg = sourceImg;
      this.sourceImg.filter(INVERT);
      this.sourceImg.filter(THRESHOLD, 0.2);
      this.mapG = createGraphics(sourceImg.width, sourceImg.height);
      
      d = 40;
      scaleLevel = 10;
      cells = new ArrayList<Cell>();
      
      process();
    }
    
    void process() {
      for (int i = 0; i < scaleLevel; i++) {
        int cellPlaceTimes;
        if (i < scaleLevel * 2/7) {
          cellPlaceTimes = 500;
        } else if (i < scaleLevel * 4/7) {
          cellPlaceTimes = 1500;
        } else {
          cellPlaceTimes = 2000;
        }
        
        for (int j = 0; j < cellPlaceTimes; j++) {
          float startX = random(sourceImg.width - d);
          float startY = random(sourceImg.height - d);
          if (canPlace(startX, startY, this.sourceImg, this.mapG)) {
            mapG.beginDraw();
            mapG.fill(0);
            mapG.ellipse(startX+d/2, startY+d/2, d, d);
            mapG.endDraw();
            
            c = color(random(0, 256), random(0, 256), random(0, 256));
            cells.add(new Cell(startX+d/2, startY+d/2, d, c, 0));
          }
        }
        
        d *= 0.8;
      }
      
      
    }
    
    void draw() {
      Cell cell;
      for (int i = 0;i < cells.size(); i++) {
        cell = cells.get(i); 
        fill(cell.cell_color);
        noStroke();
        cell.draw();
      }
    }
    
    boolean canPlace(float startX, float startY, PImage sourceImg, PGraphics mapG) {
      boolean canPlace = true;
      for (int i = 0; i < d; i++) {
        for (int j = 0; j < d; j++) {
          if (sourceImg.get(int(startX+i), int(startY+j)) != color(0)) {
            canPlace = false;
            break;
          }
          if (mapG.get(int(startX+i), int(startY+j)) == color(0)) {
            canPlace = false;
            break;
          }
        }
      }
    
    return canPlace;
  }
}
