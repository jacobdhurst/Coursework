function root = nm(initial, p, N)

syms x;
f = x-4*sin(2*x)-3;
df = -8*cos(2*x)+1; 

xn = initial;
xn1 = 0;

n = 0;
tol = 1*10^(-p);
error = (xn1 - xn) + 2*tol;

while(abs(error) >= tol)
    if(n >= N)
        break;%error('Reached stopping criteria of n.');
    end
    xn1 = double(xn-(subs(f,x,xn))/((subs(df,x,xn))));
    
    error = (xn1 - xn);
    n = n + 1;
    
    xn = xn1; 
end

root = xn1;
fprintf('Root = %.15f, found in n = %d steps.', root, n);

end

%finds all roots with some irregularities given "in between" values
