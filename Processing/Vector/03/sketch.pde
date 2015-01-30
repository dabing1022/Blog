Mover mover;
ArrayList<Mover> moverArr;
void setup()
{
    size(1000, 600);
    smooth();

    moverArr = new ArrayList<Mover>();
    for(int i = 0; i < 10; i++) {
        mover = new Mover();
        moverArr.add(mover);
    }
}

void draw()
{
    background(255);

    for (int i = moverArr.size()-1; i >= 0; i--) {
        Mover mover = moverArr.get(i);
        mover.update();
        mover.checkEdge();
        mover.display();
    }
}
