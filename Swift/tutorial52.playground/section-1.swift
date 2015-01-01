import UIKit

class Box: UIView {
    override func drawRect(rect: CGRect) {
        //// Color Declarations
        let color = UIColor(red: 1.000, green: 0.996, blue: 0.328, alpha: 1.000)
        let color2 = UIColor(red: 0.645, green: 0.910, blue: 0.048, alpha: 1.000)
        
        //// Rectangle Drawing
        var rectanglePath = UIBezierPath()
        rectanglePath.moveToPoint(CGPointMake(10.06, 0))
        rectanglePath.addLineToPoint(CGPointMake(89.94, 0))
        rectanglePath.addCurveToPoint(CGPointMake(95.59, 0.82), controlPoint1: CGPointMake(92.84, 0), controlPoint2: CGPointMake(94.29, 0))
        rectanglePath.addLineToPoint(CGPointMake(95.85, 0.94))
        rectanglePath.addCurveToPoint(CGPointMake(99.51, 7.89), controlPoint1: CGPointMake(97.55, 2.11), controlPoint2: CGPointMake(98.89, 4.66))
        rectanglePath.addCurveToPoint(CGPointMake(100, 19.11), controlPoint1: CGPointMake(100, 10.86), controlPoint2: CGPointMake(100, 13.61))
        rectanglePath.addLineToPoint(CGPointMake(100, 80.89))
        rectanglePath.addCurveToPoint(CGPointMake(99.57, 91.63), controlPoint1: CGPointMake(100, 86.39), controlPoint2: CGPointMake(100, 89.14))
        rectanglePath.addLineToPoint(CGPointMake(99.51, 92.11))
        rectanglePath.addCurveToPoint(CGPointMake(95.85, 99.06), controlPoint1: CGPointMake(98.89, 95.34), controlPoint2: CGPointMake(97.55, 97.89))
        rectanglePath.addCurveToPoint(CGPointMake(89.94, 100), controlPoint1: CGPointMake(94.29, 100), controlPoint2: CGPointMake(92.84, 100))
        rectanglePath.addLineToPoint(CGPointMake(10.06, 100))
        rectanglePath.addCurveToPoint(CGPointMake(4.41, 99.18), controlPoint1: CGPointMake(7.16, 100), controlPoint2: CGPointMake(5.71, 100))
        rectanglePath.addLineToPoint(CGPointMake(4.15, 99.06))
        rectanglePath.addCurveToPoint(CGPointMake(0.49, 92.11), controlPoint1: CGPointMake(2.45, 97.89), controlPoint2: CGPointMake(1.11, 95.34))
        rectanglePath.addCurveToPoint(CGPointMake(0, 80.89), controlPoint1: CGPointMake(0, 89.14), controlPoint2: CGPointMake(0, 86.39))
        rectanglePath.addLineToPoint(CGPointMake(0, 19.11))
        rectanglePath.addCurveToPoint(CGPointMake(0.43, 8.37), controlPoint1: CGPointMake(0, 13.61), controlPoint2: CGPointMake(0, 10.86))
        rectanglePath.addLineToPoint(CGPointMake(0.49, 7.89))
        rectanglePath.addCurveToPoint(CGPointMake(4.15, 0.94), controlPoint1: CGPointMake(1.11, 4.66), controlPoint2: CGPointMake(2.45, 2.11))
        rectanglePath.addCurveToPoint(CGPointMake(10.06, 0), controlPoint1: CGPointMake(5.71, -0), controlPoint2: CGPointMake(7.16, 0))
        rectanglePath.closePath()
        color.setFill()
        rectanglePath.fill()
        
        
        //// Star Drawing
        var starPath = UIBezierPath()
        starPath.moveToPoint(CGPointMake(49.92, 19))
        starPath.addLineToPoint(CGPointMake(62.18, 36.45))
        starPath.addLineToPoint(CGPointMake(83, 42.43))
        starPath.addLineToPoint(CGPointMake(69.77, 59.2))
        starPath.addLineToPoint(CGPointMake(70.36, 80.35))
        starPath.addLineToPoint(CGPointMake(49.92, 73.26))
        starPath.addLineToPoint(CGPointMake(29.47, 80.35))
        starPath.addLineToPoint(CGPointMake(30.07, 59.2))
        starPath.addLineToPoint(CGPointMake(16.83, 42.43))
        starPath.addLineToPoint(CGPointMake(37.65, 36.45))
        starPath.addLineToPoint(CGPointMake(49.92, 19))
        starPath.closePath()
        color2.setFill()
        starPath.fill()
        
        var magentaColor = UIColor(red: 0.5, green: 0.0, blue: 0.5, alpha: 1.0)
        var helveticaBold = UIFont.boldSystemFontOfSize(12);
        var myString: String = "I Learn Really Fast"
        myString.drawInRect(CGRect(x:10, y:10, width:100, height:50),withAttributes:[NSFontAttributeName: helveticaBold,
            NSForegroundColorAttributeName:magentaColor])
    }
    
}

var box = Box(frame: CGRect(x:0, y:0, width:100, height:100))
box.backgroundColor = UIColor.clearColor()
box
