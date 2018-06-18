#!/usr/local/bin/python3

import wx

class App(wx.App):
    def OnInit(self):
        frame = wx.Frame(None, title="Hello World!!!")
        frame.Show()
        return True

app = App()
app.MainLoop()