//

extern "C" 
{
	#include "lua.h"
	#include "lualib.h"
	#include "lauxlib.h"
};

#include "lua_tinker.h"

static int cpp_int = 100;

int main()
{
	lua_State* L = lua_open();

	luaopen_base(L);

	lua_tinker::set(L, "cpp_int", cpp_int);

	lua_tinker::dofile(L, "sample2.lua");

	int lua_int = lua_tinker::get<int>(L, "lua_int");

	printf("lua_int = %d\n", lua_int);

	lua_close(L);

	return 0;
}
