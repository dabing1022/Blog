print(haha)

print(haha.value)

print(haha.inside)

print(haha.inside.value)

haha.test = "input from lua"

function print_table(arg)
	print("arg = ", arg)
	print("arg.name = ", arg.name)
end

function return_table(arg)
	local ret = {}
	ret.name = arg
	return ret
end