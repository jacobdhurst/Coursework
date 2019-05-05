function root = fpi(initial, p, N)

syms x;
g = (-sin(2*x))+((5*x)/4)-(3/4);

xn = initial;
xn1 = 0;

n = 0;
tol = 1*10^(-p);
error = (xn1 - xn) + 2*tol;

while(abs(error) >= tol)
    if(n >= N)
        break;%error('Reached stopping criteria of n.');
    end
    xn1 = double(subs(g, x, xn));
    
    error = (xn1 - xn);
    n = n + 1;
    
    xn = xn1; 
end

root = xn1;
fprintf('Root = %.15f, found in n = %d steps.', root, n);

end