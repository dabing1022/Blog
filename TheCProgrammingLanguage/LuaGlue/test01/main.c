#include <stdio.h>
#include "../lua/lua.h"
#include "../lua/lualib.h"
#include "../lua/lauxlib.h"

int main()
{
	lua_State* L = lua_open();
	luaL_openlibs( L );
	
	luaL_dofile( L, "test.lua" ); //执行配置文件
	int a = 1;
	int b = 2;
	lua_getglobal( L, "add" ); //查询函数
	lua_pushnumber( L, a ); //参数压栈
	lua_pushnumber( L, b );

	if( lua_pcall( L, 2, 1, 0 ) != 0 ) //错误检测
	{
		printf("error running function add: %s", lua_tostring(L, -1));
	}

	if( !lua_isnumber( L, -1 ) ) //检查返回值是否为数值
	{
		printf("add 必须返回一个数值\n");
	}

	int c = lua_tonumber( L, -1 );
	lua_pop( L, 1 ); //从栈中弹出返回值
	printf("a + b = %d\n", c);
	return 0;
}