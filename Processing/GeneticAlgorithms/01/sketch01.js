function Sketch01( sketch ) {

  var x = 100; 
  var y = 100;

  sketch.setup = function() {
    sketch.createCanvas(700, 410);
  }

  sketch.draw = function() {
    sketch.background(0);
    sketch.fill(255);
    sketch.rect(x,y,50,50);
  }
}

function test111() {
	alert("helllo")
}