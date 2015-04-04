// Playground - noun: a place where people can play


class OptionFactory {
    
    func createOption(option: String) -> Option? {
        switch option {
        case "+": return OptionAdd()
        case "-": return OptionSub()
        case "*": return OptionMul()
        case "/": return OptionDiv()
        default:
            println("Option is invalid.")
            return nil
        }
    }
}

class Option {
    var numberA: Double? = 0
    var numberB: Double? = 0
    
    func getResult() -> Double {
        return Double.NaN
    }
}

class OptionAdd : Option {
    override func getResult() -> Double {
        return numberA! + numberB!
    }
}

class OptionSub: Option {
    override func getResult() -> Double {
        return numberA! - numberB!
    }
}

class OptionMul: Option {
    override func getResult() -> Double {
        return numberA! * numberB!
    }
}

class OptionDiv: Option {
    override func getResult() -> Double {
        if numberB! != 0 {
            return numberA! / numberB!
        } else {
            return Double.NaN
        }
    }
}

var factory = OptionFactory()
var optionAdd = factory.createOption("+")
optionAdd?.numberA = 1
optionAdd?.numberB = 1
optionAdd?.getResult()

var optionSub = factory.createOption("-")
optionSub?.numberA = 100
optionSub?.numberB = 20
optionSub?.getResult()

var optionMul = factory.createOption("*")
optionMul?.numberA = 2
optionMul?.numberB = 30
optionMul?.getResult()

var optionDiv = factory.createOption("/")
optionDiv?.numberA = 20
optionDiv?.numberB = 4
optionDiv?.getResult()