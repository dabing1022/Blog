#! python3
# coding:utf-8

import npyscreen

# This application class serves as a wrapper for the initialization of curses
# and also manages the actual forms of the application

class MyTestApp(npyscreen.NPSAppManaged):
    def onStart(self):
        self.registerForm("MAIN", MainForm())

# This form class defines the display that will be presented to the user.

class MainForm(npyscreen.Form):
    def create(self):
        self.add(npyscreen.TitleText, name = "Text:", value= "Hellow World!" )

    def afterEditing(self):
        self.parentApp.setNextForm(None)


class myEmployeeForm(npyscreen.Form):
    def create(self):
        self.myName        = self.add(npyscreen.TitleText, name='Name')
        self.myDepartment = self.add(npyscreen.TitleSelectOne, scroll_exit=True, max_height=3, name='Department', values = ['Department 1', 'Department 2', 'Department 3'])
        self.myDate        = self.add(npyscreen.TitleDateCombo, name='Date Employed')

def myFunction(*args):
    F = myEmployeeForm(name = "New Employee")
    F.edit()
    return "Created record for " + F.myName.value

if __name__ == '__main__':
    print(npyscreen.wrapper_basic(myFunction))
