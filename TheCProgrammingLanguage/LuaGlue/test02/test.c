#include <stdio.h>
#include <math.h>
#include "../lua/lua.h"
#include "../lua/lualib.h"
#include "../lua/lauxlib.h"


static int l_sin (lua_State* L)
{
    double d = lua_tonumber(L, 1);
    lua_pushnumber(L, sin(d));
    return 1;
}

int main()
{
    lua_State* L = lua_open();
    luaL_openlibs(L);

    lua_pushcfunction(L, l_sin);
    lua_setglobal(L, "mysin");

    luaL_dofile(L, "test.lua");
    return 0;
}
