import Cocoa

public class MandelbrotView: NSView {
    public var mandelbrotRect = ComplexRect(Complex(-2.1, 1.5), Complex(1.0, -1.5))
    let rectScale = 1
    let blockiness = 0.5 // pick a value from 0.25 to 5.0
    let colorCount = 2000
    var colorSet : Array<NSColor> = Array()
   
    public override func drawRect(rect : CGRect) {
        
        NSBezierPath.fillRect(rect)
        let startTime = NSDate().timeIntervalSince1970
        drawMandelbrot(rect)
        let elapsedTime = NSDate().timeIntervalSince1970 - startTime
        println("Elapsed time: \(elapsedTime) seconds")
        
        // Draw some coordinates
        // Axes
//        let p1 = complexCoordinatesToViewCoordinates(Complex(0,2), rect: rect)
//        NSColor.redColor().set()
//        NSBezierPath.fillRect(CGRect(origin: p1, size: CGSize(width: 0.5,height: rect.size.height*2)))
//        let p2 = complexCoordinatesToViewCoordinates(Complex(-3,0), rect: rect)
//        NSBezierPath.fillRect(CGRect(origin: p2, size: CGSize(width: rect.size.width*2,height: 0.5)))
//
//        var p3 = complexCoordinatesToViewCoordinates(Complex(0, 1), rect: rect)
//        p3.x = p3.x - 2
//        var o =  NSBezierPath(rect: CGRect(origin: p3, size: CGSize(width: 4,height: 0.25)))
//        o.lineWidth = 0.25
//        o.stroke()
//        
//        p3 = complexCoordinatesToViewCoordinates(Complex(0, -1), rect: rect)
//        p3.x = p3.x - 2
//        o =  NSBezierPath(rect: CGRect(origin: p3, size: CGSize(width: 4,height: 0.25)))
//        o.lineWidth = 0.25
//        o.stroke()
//        
//        p3 = complexCoordinatesToViewCoordinates(Complex(-2, 0), rect: rect)
//        p3.y = p3.y - 2
//        o =  NSBezierPath(rect: CGRect(origin: p3, size: CGSize(width: 0.25,height: 4)))
//        o.lineWidth = 0.25
//        o.stroke()
//
//        p3 = complexCoordinatesToViewCoordinates(Complex(0.5, 0), rect: rect)
//        p3.y = p3.y - 2
//        o =  NSBezierPath(rect: CGRect(origin: p3, size: CGSize(width: 0.25,height: 4)))
//        o.lineWidth = 0.25
//        o.stroke()
}
    
    func initializeColors() {
        if colorSet.count < 2 {
            colorSet = Array()
            for c in 0...colorCount {
                var c_f : CGFloat = CGFloat(c)
                colorSet.append(NSColor(hue: CGFloat(abs(sin(c_f/30.0))), saturation: 1.0, brightness: c_f/100.0 + 0.8, alpha: 1.0))
            }
        }
    }
    
    func computeMandelbrotPoint(C: Complex) -> NSColor {
        // Calculate whether the point is inside or outside the Mandelbrot set
        // Zn+1 = (Zn)^2 + c -- start with Z0 = 0
            
        var z = Complex()
        for it in 1...colorCount {
            z = z*z + C
            if modulus(z) > 2 {
                return colorSet[it] // bail as soon as the complex number is too big (you're outside the set & it'll go to infinity)
            }
        }
        // Yay, you're inside the set!
        return NSColor.blackColor()
    }
    
    func viewCoordinatesToComplexCoordinates(#x: Double, y: Double, rect: CGRect) -> Complex {
        let tl = mandelbrotRect.topLeft
        let br = mandelbrotRect.bottomRight
        let r = tl.real + (x/Double(rect.size.width * CGFloat(rectScale)))*(br.real - tl.real)
        let i = tl.imaginary + (y/Double(rect.size.height * CGFloat(rectScale)))*(br.imaginary - tl.imaginary)
        return Complex(r,i)
    }

    func complexCoordinatesToViewCoordinates(c: Complex, rect: CGRect) -> CGPoint {
        let tl = mandelbrotRect.topLeft
        let br = mandelbrotRect.bottomRight
        let x = (c.real - tl.real)/(br.real - tl.real)*Double(rect.size.width * CGFloat(rectScale))
        let y = (c.imaginary - tl.imaginary)/(br.imaginary - tl.imaginary)*Double(rect.size.height * CGFloat(rectScale))
        return CGPoint(x: x,y: y)
    }

    func drawMandelbrot(rect : CGRect) {
        var width:Double = Double(rect.size.width)
        var height:Double = Double(rect.size.height)
        let startTime = NSDate().timeIntervalSince1970
        initializeColors()
        for x in stride(from: 0, through: width, by: blockiness) {
            for y in stride(from: 0, through: height, by: blockiness) {
                let cc = viewCoordinatesToComplexCoordinates(x: x, y: y, rect: rect)
                computeMandelbrotPoint(cc).set()
                NSBezierPath.fillRect(CGRect(x: x, y: y, width: blockiness, height: blockiness))
            }
        }
        let elapsedTime = NSDate().timeIntervalSince1970 - startTime
        println("Calculation time: \(elapsedTime)")
    }
}