def greeting = args.size()==0 ? "world" : args.join(",");
return "Hello ${greeting}!";
