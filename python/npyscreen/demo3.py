#! python3
# coding:utf-8

import npyscreen
import random

class MyGrid(npyscreen.GridColTitles):
    # You need to override custom_print_cell to manipulate how
    # a cell is printed. In this example we change the color of the
    # text depending on the string value of cell.
    def custom_print_cell(self, actual_cell, cell_display_value):
        if cell_display_value =='FAIL':
           actual_cell.color = 'DANGER'
        elif cell_display_value == 'PASS':
           actual_cell.color = 'GOOD'
        else:
           actual_cell.color = 'DEFAULT'

def myFunction(*args):
    # making an example Form
    F = npyscreen.Form(name='Example viewer', lines=10, )
    myFW = F.add(npyscreen.TitleText)
    gd = F.add(MyGrid)

    # Adding values to the Grid, this code just randomly
    # fills a 2 x 4 grid with random PASS/FAIL strings.
    gd.values = []
    for x in range(2):
        row = []
        for y in range(4):
            if bool(random.getrandbits(1)):
                row.append("PASS")
            else:
                row.append("FAIL")
        gd.values.append(row)
    F.edit()

if __name__ == '__main__':
    npyscreen.wrapper_basic(myFunction)
