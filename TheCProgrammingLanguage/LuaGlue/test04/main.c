#include <stdio.h>
#include "../lua/lua.h"
#include "../lua/lualib.h"
#include "../lua/lauxlib.h"

// gcc main.c ../lua/liblua.a
// gcc main.c ../lua/liblua.a -o test
// ./a.out
// ./test

// table process
void loadfile(lua_State* L, const char* fname, int* w, int* h)
{
	if (luaL_dofile(L, fname))
	{
		printf("Error msg is %s\n", lua_tostring(L, -1));
		return;
	}

	lua_getglobal(L, "width");
	lua_getglobal(L, "height");
	if(!lua_isnumber(L, 1) || !lua_isnumber(L, 2))
	{
		printf("ERROR:Invalid type!");
	}

	*w = lua_tointeger(L, 1);
	*h = lua_tointeger(L, 2);
}

int main()
{
	lua_State* L = lua_open();
	luaL_openlibs( L );

	int width;
	int height;
	loadfile(L, "config.lua", &width, &height);
	printf("width is: %d\n", width);
	printf("height is: %d\n", height);

	lua_close(L);
	return 0;
}