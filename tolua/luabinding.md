###[来源](https://groups.google.com/forum/#!topic/quick-x/kVDKbcMt2Ro)

我说下我的绑定做法，和[廖大的做法](http://cn.quick-x.com/?p=235)和[自留地的做法](http://www.codeo4.cn/archives/746)稍微有些不同。我尽可能描述清楚下。

拿个测试项目做个举例

先看看我的测试项目目录：

![目录](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20131224/folderRelated0.png)

##四个问题

1.自留地博文中用到的 bat 脚本是你说的位置

2.自定义类头文件和源文件的位置

-  廖大的做法：随便放在项目里，直接放也好，放在自建文件夹目录也好
-  自留地的做法：
  ![绑定在extra扩展中](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20131224/folderRelated.png)
  
  将.h/.cpp文件放在`extra/myClasses`下，tolua没有新建，而是追加到了`extra/luabinding/cocos2dx_extra_luabinding.tolua`最后，在运行build脚本后会重新生成`cocos2dx_extra_luabinding.h`和`cocos2dx_extra_luabinding.cpp`绑定文件，其中自定义的`MyTestClass.h/cpp`类的相关绑定代码被追尾添加到了`cocos2dx_extra_luabinding.cpp`中。
    
3.两者做法都可以。原理是一样的。都是

-  添加自定义类
-  按照tolua改写规则，改写头文件(也可以在头文件加tolua能够识别的代码)生成tolua文件，这里分为两个方式，一是生成单独对应的tolua文件，二是追加到已有的tolua文件中
-  运行脚本，生成绑定文件，根据第二步tolua生成方式的不同，生成的绑定也是两个方式：一是生成单独对应的绑定文件.h/.cpp，而是生成的内容分别追加到已有的绑定文件.h/.cpp中
-  载入`luabinding`接口文件

4.可能你没有正确的载入`luabinding`接口文件。

为了理解quick中的对一些`luabinding`接口文件的载入，我们可以看下目录 `quick-cocos2d-x/lib/cocos2d-x/scripting/lua/cocos2dx_support/CCLuaStack.cpp`，看`init`方法：

```C++
bool CCLuaStack::init(void)
{
    CCTime::gettimeofdayCocos2d(&m_lasttime, NULL);
    m_state = lua_open();
    CCAssert(m_state, "create Lua VM failed");

    s_map[m_state] = this;

    luaL_openlibs(m_state);
    toluafix_open(m_state);
    tolua_Cocos2d_open(m_state);

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_MAC)
    CCLuaObjcBridge::luaopen_luaoc(m_state);
#elif (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    CCLuaJavaBridge::luaopen_luaj(m_state);
#endif

    addLuaLoader(cocos2dx_lua_loader);

    // register lua print
    lua_pushcfunction(m_state, lua_print);
    lua_setglobal(m_state, "print");

    // register CCLuaLoadChunksFromZip
    lua_pushcfunction(m_state, lua_loadChunksFromZIP);
    lua_setglobal(m_state, "CCLuaLoadChunksFromZIP");

    // register CCLuaStackSnapshot
    luaopen_snapshot(m_state);

#if QUICK_MINI_TARGET == 0

    // chipmunk
    luaopen_CCPhysicsWorld_luabinding(m_state);
    // luaproxy
    luaopen_LuaProxy(m_state);
	// cocos-extensions
    register_all_cocos2dx_extension_manual(m_state);
    // cocosbuilder
    tolua_extensions_ccb_open(m_state);
    // cocos2dx_extra luabinding
    luaopen_cocos2dx_extra_luabinding(m_state);
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    luaopen_cocos2dx_extra_ios_iap_luabinding(m_state);
#endif
    // load WebSockets luabinding
    tolua_web_socket_open(m_state);
    // lua extensions
    luaopen_lua_extensions(m_state);

#endif // QUICK_MINI_TARGET

    return true;
}
```

像刚才提到的一些扩展类的luabinding接口都在这里面。

按照自留地那种追加的方式，因为自定义类的绑定代码被追加到了`cocos2dx_extra_luabinding.cpp`中，而`luaopen_cocos2dx_extra_luabinding(m_state);`在`init`函数里面已经实现了接口暴露，所以不需要做其余的工作了。但这种方式别忘记在`cocos2dx_extra_luabinding.cpp` include进自定义类的头文件。

按照廖大的方式，我们可以在`AppDelegate.cpp`中将自定义类的luabinding接口暴露出来给lua，详细阅读廖大的那篇介绍[载入 luabinding 接口文件](http://cn.quick-x.com/?p=235)