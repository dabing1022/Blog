//: Playground - noun: a place where people can play

import UIKit
import XCPlayground
import CoreGraphics

struct DEMO_FRAME {
    static let width = 1800
    static let height = 500
}

let demoView = UIView(frame: CGRect(x: 0, y: 0, width: 1800, height: 500))
demoView.backgroundColor = UIColor.darkGrayColor()
XCPShowView("DEMO", view: demoView)

// 5--15--60--20--100--10--1200
// 5--70--70--70--70---70--70


let pathLineX = UIBezierPath()
pathLineX.moveToPoint(CGPoint(x: 0, y: DEMO_FRAME.height))
pathLineX.addLineToPoint(CGPoint(x: DEMO_FRAME.width, y: DEMO_FRAME.height))
let shapeLineX = CAShapeLayer()
shapeLineX.strokeColor = UIColor.whiteColor().CGColor
shapeLineX.lineWidth = 2.0
shapeLineX.path = pathLineX.CGPath
demoView.layer.addSublayer(shapeLineX)

let pathLineY = UIBezierPath()
pathLineY.moveToPoint(CGPoint(x: 0, y: DEMO_FRAME.height))
pathLineY.addLineToPoint(CGPoint(x: 0, y: 0))
let shapeLineY = CAShapeLayer()
shapeLineY.strokeColor = UIColor.whiteColor().CGColor
shapeLineY.lineWidth = 2.0
shapeLineY.path = pathLineY.CGPath
demoView.layer.addSublayer(shapeLineY)

let pathData = UIBezierPath()
var lastPoint = CGPoint(x: 0, y: DEMO_FRAME.height - 0)
pathData.moveToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x: 5, y: DEMO_FRAME.height - 5)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x:20, y: DEMO_FRAME.height - 150)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x:80, y: DEMO_FRAME.height - 145 - 75)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x:100, y: DEMO_FRAME.height - 215 - 75)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x:200, y: DEMO_FRAME.height - 285 - 75)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x:210, y: DEMO_FRAME.height - 355 - 75)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

lastPoint = CGPoint(x:1800, y: DEMO_FRAME.height - 425 - 75)
pathData.addLineToPoint(lastPoint)
pathData.addArcWithCenter(lastPoint, radius: 8, startAngle: 0.0, endAngle: CGFloat(M_PI) * 2, clockwise: true)

let shapePathData = CAShapeLayer()
shapePathData.strokeColor = UIColor.whiteColor().CGColor
shapePathData.fillColor = UIColor.clearColor().CGColor
shapePathData.lineWidth = 1.5
shapePathData.path = pathData.CGPath
demoView.layer.addSublayer(shapePathData)
demoView

