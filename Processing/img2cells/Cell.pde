class Cell {

  PVector location;
  PVector size;
  color cell_color;
  int shapeID;

  Cell(float posX, float posY, float diameter, color cell_color, int shapeID) {
    this.location = new PVector(posX, posY);
    this.size = new PVector(diameter, diameter);
    this.cell_color = cell_color;
    this.shapeID = shapeID;
  }
  
  void draw() {
    if (this.shapeID == 0) {
      ellipse(this.location.x, this.location.y, this.size.x, this.size.y);
    } else if (this.shapeID == 1) {
      rectMode(CENTER);
      rect(this.location.x, this.location.y, this.size.x, this.size.y);
    }
  }
}

