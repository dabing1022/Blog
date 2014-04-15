print(g_test._test)

print(g_test:is_test())

print(g_test:ret_int())

temp = test(4)

print(temp._test)

a = g_test:get()


temp:set(a)

print(temp._test)

print(temp:is_base())

print(temp:is_test())

-------------------------------------------------------------------------------
function objinfo(obj)

	local meta = getmetatable(obj)
	if meta ~= nil then
		metainfo(meta)
	else
		print("no object infomation !!")
	end
end

function metainfo(meta)

	if meta ~= nil then
		local name = meta["__name"]
		if name ~= nil then
			metainfo(meta["__parent"])
			print("<"..name..">")
			for key,value in pairs(meta) do 
				if not string.find(key, "__..") then 
					if type(value) == "function" then
						print("\t[f] "..name..":"..key.."()") 
					elseif type(value) == "userdata" then
						print("\t[v] "..name..":"..key)
					end
				end
			end
		end
	end
end
-------------------------------------------------------------------------------

print("g_test	-> ", g_test)
print("temp	-> ", temp)
print("a	-> ", a)

print("objinfo(g_test)")
objinfo(g_test)

print("objinfo(temp)")
objinfo(temp)

print("objinfo(a)")
objinfo(a)

