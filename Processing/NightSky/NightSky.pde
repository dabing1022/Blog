Sky sky;

ArrayList<Meteor> meteorsArr;
ArrayList<Star> starsArr;
int STARS_NUM = 12; //星星的数目
int METEORS_NUM = 3; //流星的数目（流星有生命周期，死亡后重新设置位置等属性，会反复重用）
void setup() {
  size(800, 500);

  starsArr = new ArrayList<Star>();
  meteorsArr = new ArrayList<Meteor>();
  sky = new Sky(width, height);

  for (int i = 0; i < STARS_NUM; i++) {
    Star star = new Star(new PVector(random(width), random(height)));
    starsArr.add(star);
  }

  for (int i = 0; i < METEORS_NUM; i++) {
    Meteor meteor = new Meteor();
    meteorsArr.add(meteor);
  }
}

void draw() {
  sky.display();
  for (int i = 0; i < starsArr.size(); i++) {
    Star star = starsArr.get(i);
    star.display();
  }

  for (int i = meteorsArr.size()-1; i >= 0; i--) {
    Meteor meteor = meteorsArr.get(i);
    meteor.update();
    meteor.display();
  }
}