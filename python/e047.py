#!/usr/local/bin/python3

def test(index):
    inner_index = index
    try:
        if inner_index < 100:
            print(inner_index)
            if inner_index % 2 == 1:
                print("奇数退出")
                exit()
            inner_index += 1
            test(inner_index)
        else:
            print("finished")
            exit()
    except:
        test(inner_index)

if __name__ == "__main__":
    test(0)
