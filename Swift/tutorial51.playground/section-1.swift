import UIKit

let map = [[0, 0, 1, 0],
           [0, 0, 0, 0],
           [0, 0, 1, 0],
           [0, 1, 0, 0],
           [0, 0, 0, 1]]
let ROW_NUM = 5
let COL_NUM = 4
let TILE_WIDTH = 150
let TILE_HEIGHT = TILE_WIDTH
let TILE_SPACE = 15
let WALK_RIGHT = [0, 1]
let WALK_LEFT = [0, -1]
let WALK_UP = [-1, 0]
let WALK_DOWN = [1, 0]
let NEXT = [WALK_RIGHT, WALK_DOWN, WALK_LEFT, WALK_UP]

let entryPos = [0, 0]
let desPos = [3, 2]

var book = [[0, 0, 0, 0],
    [0, 0, 0, 0],
    [0, 0, 0, 0],
    [0, 0, 0, 0],
    [0, 0, 0, 0]]

let canWalkColor = UIColor.greenColor()
let blockColor = UIColor.redColor()
let walkingColor = UIColor.blueColor()

var mapView = UIView(frame: CGRect(x: 0, y:0 , width: 800, height: 800))
var tileViewArr = Array<UIView>()

for i in 0..<ROW_NUM {
    for j in 0..<COL_NUM {
        var tileView = UIView(frame: CGRect(x: 0+(TILE_WIDTH+TILE_SPACE)*j, y: 0+(TILE_HEIGHT+TILE_SPACE)*i, width: TILE_WIDTH, height: TILE_HEIGHT))
        
        if (map[i][j] == 0) {
            tileView.backgroundColor = canWalkColor
        } else {
            tileView.backgroundColor = blockColor
        }
        mapView.addSubview(tileView)
        tileViewArr.append(tileView)
    }
}

var min = INT_MAX
func dfs(x: Int, y: Int, step: Int32) -> Void {
    var tx:Int
    var ty:Int
    var k:Int
    if (x == desPos[0] && y == desPos[1]) {
        if(step < min) {
            min = step
        }
        
        return
    }
    
    for k in 0...3 {
        tx = x + NEXT[k][0]
        ty = y + NEXT[k][1]
        
        if (tx < 0 || tx > ROW_NUM-1 || ty < 0 || ty > COL_NUM-1) {
            continue
        }
        
        if (map[tx][ty] == 0 && book[tx][ty] == 0) {
            book[tx][ty] = 1
            tileViewArr[COL_NUM*tx+ty].backgroundColor = walkingColor
            mapView
            dfs(tx, ty, step+1)
            book[tx][ty] = 0
            tileViewArr[COL_NUM*tx+ty].backgroundColor = canWalkColor
        }
    }
}

book[entryPos[0]][entryPos[1]] = 1
dfs(entryPos[0], entryPos[1], 0)
min