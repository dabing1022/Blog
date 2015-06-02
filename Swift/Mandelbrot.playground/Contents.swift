import Cocoa

//: ## Mandelbrot Set Playground

let sideLength = 200
let rect = CGRect(x: 0, y: 0, width: sideLength, height: sideLength)

// Interesting points in the Mandelbrot Set
var points = [  "a": ("Full Set", Complex(-0.75, 0.0), 0.7),
                "b": ("Seahorse Valley", Complex(-0.744, 0.12), 100),
                "c": ("Elephant Valley", Complex(0.28,0.485), 200),
                "d": ("Little Mandelbrot", Complex(-1.77,0), 20),
                "e": ("Another Mandelbrot", Complex(-0.1592,-1.0338), 80),
                "f": ("Just Some Spot", Complex(-1.25865, -0.373908), 6600)
]

// Focus on a point in the Complex plane
func newMandelbrotRect(triple: (String, Complex, Double)) -> ComplexRect{
    println("Exploring: \(triple.0)")
    var p1 = triple.1
    var p2 = triple.1
    let zoom = triple.2
    p1.real = p1.real - Double(1.0/Double(zoom))
    p1.imaginary = p1.imaginary - Double(1.0/Double(zoom))
    p2.real = p2.real + Double(1.0/Double(zoom))
    p2.imaginary = p2.imaginary + Double(1.0/Double(zoom))
    return ComplexRect(p1, p2)
}

//: ### Mandelbrot Set Rendering
//: This playground renders the Mandelbrot set by running code embedded within the playground's Sources folder.  You can explore the Sources folder by opening the Project Navigator (CMD-1) and clicking the triangle next to the playground file.

let m = MandelbrotView(frame: rect)
m.mandelbrotRect = newMandelbrotRect(points["b"]!)
m.mandelbrotRect = newMandelbrotRect(points["e"]!)
m.mandelbrotRect = newMandelbrotRect(points["f"]!)


















// done.
  

