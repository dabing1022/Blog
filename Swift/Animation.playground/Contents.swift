//: Playground - noun: a place where people can play

import XCPlayground
import UIKit

let redView: UIImageView = UIImageView(image: UIImage(named: "icon_red"))
XCPShowView("my view", redView)

redView.transform = CGAffineTransformMakeScale(0, 0)
UIView.animateWithDuration(0.5, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 10, options: UIViewAnimationOptions.CurveEaseInOut, animations: { () -> Void in
    redView.transform = CGAffineTransformIdentity
}, completion: nil)

