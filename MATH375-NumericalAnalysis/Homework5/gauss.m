loader;

iterations = 0;

x = zeros(6, 1);

n = size(x, 1);

error = inf;
tolerance = 0.001;

while error > tolerance
    oldx = x;
    x = Bgs*oldx + cgs;
    
    iterations = iterations + 1;
    error = norm((oldx - x), 2);
end
disp(x);
disp(iterations);