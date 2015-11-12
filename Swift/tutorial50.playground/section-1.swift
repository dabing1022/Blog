var a = [Int](count: 10, repeatedValue: 0)
var book = [Int](count: 10, repeatedValue: 0)
let cardNum: Int = 4

func dfs(step: Int) -> Void {
    if (step == cardNum + 1) {
        for i in 1...cardNum {
            print(a[i])
        }
        
        print("-------------")
        return
    }
    
    for i in 1...cardNum {
        if (book[i] == 0) {
            a[step] = i
            book[i] = 1
            
            dfs(step + 1)
            book[i] = 0
        }
    }
}


dfs(1)
