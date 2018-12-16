% my_mean function

function output = my_mean(fun, a, b, N)
if(a > b) 
    error('Invalid interval [' + a + ', ' + b + ']');
end

u = linspace(a, b, N);
v = fun(u);

output = (1/N)*(sum(v));
end

