String sourceImgName;
PImage sourceImg;
ImageWithEllipse imageWithEllipse;
ImageWithTexts imageWithTexts;

String[]words={"李小龙","Bruce Lee","华人","武打演员","导演","武术家","截拳道","精武门","香港","猛龙过江","龙争虎斗","死亡游戏","恰恰舞", 
               "青蜂侠","唐山大兄","综合格斗", "中国人"};
               
               PImage txt;
void setup() 
{
  sourceImgName = "BruceLee04.jpg";
  
  sourceImg = loadImage(sourceImgName);
//  sourceImg.resize(1200, 0);
  size(sourceImg.width, sourceImg.height);
  
//  imageWithEllipse = new ImageWithEllipse(sourceImg);
  imageWithTexts = new ImageWithTexts(sourceImg, words);
 


  /*
  txt=loadImage("BruceLee03.jpg");   //把文字那张图片赋给变量txt，即把图片载入内存
  txt.resize(sourceImg.width, sourceImg.height);   //把图片拉伸到窗口大小
  //sourceImg.filter(INVERT);   //对头像使用反相滤镜
  sourceImg.filter(GRAY);   //把图片进一步变成灰阶
  sourceImg.filter(POSTERIZE,4);   //进一步把图像分色，即图片原本含有很多种灰阶颜色，这一步把所有这些颜色归纳为4种
  txt.mask(sourceImg);   //文字是底图，经滤镜处理过的头像是蒙版，这一步是为底图加蒙版
  */
}

void draw()
{
  background(255);
//  image(sourceImg, 0, 0);
//  image(txt,0,0); 

//  imageWithEllipse.draw();
  image(loadImage("BruceLee04.jpg"), 0, 0);
  imageWithTexts.draw();
  save("result.jpg");
  noLoop();
}
