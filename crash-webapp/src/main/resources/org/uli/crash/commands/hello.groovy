def greeting = binding.args.size()==0 ? "world" : binding.args.join(",");
return "Hello ${greeting}!";
