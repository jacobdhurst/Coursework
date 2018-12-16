function root = secant(guess1, guess2, p, N)

syms x;
x_old = guess1;
x_new = guess2;

f = x-4*sin(2*x)-3;
f_old = double(subs(f, x, x_old));
f_new = double(subs(f, x, x_new));

n = 0;
tol = 1*10^(-p);

while abs(f_new) >= tol
    if(n >= N)
        break;
    end
    df = f_new - f_old;
    dx = x_new - x_old;
    
    x_old = x_new;
    f_old = f_new;
    
    x_new = x_new - f_new*dx/df;
    f_new = double(subs(f, x, x_new));
    
    n = n + 1;
end

root = x_new;
fprintf('Root = %.15f, found in n = %d steps.', root, n);

end
