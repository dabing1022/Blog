import Foundation

public struct Complex: Equatable, Printable {
    public var real: Double
    public var imaginary: Double
    public init() {
        self.init(0, 0)
    }
    public init(_ real: Double,_ imaginary:Double) {
        self.real = real
        self.imaginary = imaginary
    }
    public var description: String {
        get {
            let r = String(format: "%.2f", real)
            let i = String(format: "%.2f", abs(imaginary))
            var result = ""
            let p = -2
            switch (real,imaginary) {
            case _ where imaginary == 0:
                result = "\(r)"
            case _ where real == 0:
                result = "\(i)𝒊"
            case _ where imaginary < 0:
                result = "\(r) - \(i)𝒊"
            default:
                result = "\(r) + \(i)𝒊"
            }
            return result
        }
    }
}
public func ==(lhs: Complex, rhs: Complex) -> Bool {
    return lhs.real == rhs.real && lhs.imaginary == rhs.imaginary
}
public func +(lhs: Complex, rhs: Complex) -> Complex {
    return Complex(lhs.real + rhs.real, lhs.imaginary + rhs.imaginary)
}
public func -(lhs: Complex, rhs: Complex) -> Complex {
    return Complex(lhs.real - rhs.real, lhs.imaginary - rhs.imaginary)
}
public prefix func -(c1: Complex) -> Complex {
    return Complex( -c1.real, -c1.imaginary)
}
public func *(lhs: Complex, rhs: Complex) -> Complex {
    return Complex(lhs.real * rhs.real - lhs.imaginary * rhs.imaginary, lhs.real * rhs.imaginary + rhs.real * lhs.imaginary)
}
public func /(lhs: Complex, rhs: Complex) -> Complex {
    let denom = (rhs.real * rhs.real + rhs.imaginary * rhs.imaginary)
    return Complex((lhs.real * rhs.real + lhs.imaginary * rhs.imaginary)/denom, (lhs.imaginary * rhs.real - lhs.real * rhs.imaginary)/denom)
}
public func abs(lhs:Complex) -> Double {
    return sqrt(lhs.real*lhs.real + lhs.imaginary*lhs.imaginary)
}
public func modulus(lhs:Complex) -> Double {
    return abs(lhs)
}
public func +(lhs: Double, rhs: Complex) -> Complex { // Real plus imaginary
    return Complex(lhs + rhs.real, rhs.imaginary)
}
public func -(lhs: Double, rhs: Complex) -> Complex { // Real minus imaginary
    return Complex(lhs - rhs.real, -rhs.imaginary)
}
public func *(lhs: Double, rhs: Complex) -> Complex { // Real times imaginary
    return Complex(lhs * rhs.real, lhs * rhs.imaginary)
}

public struct ComplexRect: Equatable, Printable {
    public var topLeft: Complex
    public var bottomRight: Complex
    private(set) var bottomLeft: Complex
    private(set) var topRight: Complex
    public init(_ c1: Complex, _ c2: Complex) {
        let tlr = min(c1.real, c2.real)
        let tli = max(c1.imaginary, c2.imaginary)
        let brr = max(c1.real, c2.real)
        let bri = min(c1.imaginary, c2.imaginary)
        topLeft = Complex(tlr, tli)
        bottomRight = Complex(brr, bri)
        bottomLeft = Complex(tlr, bri)
        topRight = Complex(brr, tli)
    }
    public var description: String {
        return "tl:\(topLeft), br:\(bottomRight), bl:\(bottomLeft), tr:\(topRight)"
    }
}
public func ==(lhs: ComplexRect, rhs: ComplexRect) -> Bool {
    return (lhs.topLeft == rhs.topLeft) && (lhs.bottomRight == rhs.bottomRight)
}
