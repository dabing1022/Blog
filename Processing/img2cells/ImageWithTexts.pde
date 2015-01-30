class ImageWithTexts
{
  String[] words;
  PFont myFont;
  PGraphics[] wordsG;
  PGraphics mapG;
  PImage sourceImg;
  PGraphics targetG;
  
  int total;
  ImageWithTexts(PImage sourceImg, String[] words) {
    this.sourceImg = sourceImg;
    this.sourceImg.filter(INVERT);
    this.sourceImg.filter(THRESHOLD, 0.9);
    this.words = words;
    this.myFont = createFont("Georgia", 100);
    this.total = 180;
    textFont(this.myFont);
    
    wordsG = new PGraphics[words.length];
    targetG = createGraphics(sourceImg.width, sourceImg.height);
    targetG.smooth();    
    mapG = createGraphics(sourceImg.width, sourceImg.height);

    process();
  }
  
  void process() {
    for (int i = 0; i < wordsG.length; i++) {
      wordsG[i] = createGraphics(int(textWidth(words[i])), int(getFontHeight()));
      wordsG[i].beginDraw();
      wordsG[i].textFont(myFont);
      wordsG[i].textAlign(LEFT, TOP);
      wordsG[i].fill(color(random(0, 255), random(0, 255), random(0, 255)));
//      wordsG[i].fill(color(255));
      wordsG[i].text(words[i], 0, 0);
      wordsG[i].endDraw();
      
      if (wordsG[i].width >= width) {
        wordsG[i].resize(width*2/3, 0);
      }
    }
     
    int tryTimes = 150;
    float scaleFactor = 0.7;
    PGraphics  g;
    for (int i = 0; i < this.total; i++) {
      g = wordsG[i % wordsG.length];
      
      getOut:
      //not draw when width < 10 
      while (g.width > 10) {
        for (int j = 0; j < tryTimes; j++) {
          float startX = random(width - g.width);
          float startY = random(height - g.height);
          if (canPlace(startX, startY, g.width, g.height, this.sourceImg, this.mapG)) {
            this.targetG.beginDraw();
            this.targetG.image(g, startX, startY);
            this.targetG.endDraw();
            
            mapG.beginDraw();
            mapG.fill(0);
            mapG.rect(startX, startY, g.width, g.height);
            mapG.endDraw();
            break getOut;
          }
        }
        g.resize(int(g.width*scaleFactor), 0);
      }
    }
  }
 
  void draw() {
    image(this.targetG, 0, 0);
  }
  
  boolean canPlace(float startX, float startY, float gWidth, float gHeight, PImage sourceImg, PGraphics mapG) {
    boolean canPlace = true;
    for (int i = 0; i < gWidth; i++) {
      for (int j = 0; j < gHeight; j++) {
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
  
  float getFontHeight() {
    return textAscent() + textDescent();
  }
}
