function root = bisect(f, a, b, p)
if(a > b) 
    error('Invalid interval.');
end

n     = 0;
a0    = a;
b0    = b;
root  = (a + b)/2;
check = (b - a)/2;
tol   = 1*10^(-p);
syms x;

f0 = subs(f, x, a);
f1 = subs(f, x, b);

% by IVT if f(a) < 0 & f(b) > 0 there must be a root in [a,b]
% if root exists, then this means f(a)*f(b) < 0 since neg*pos=neg.
if(f0*f1 >= 0) 
    error('No roots exist for given function in interval.');
end

while abs(check) > tol    
  fa = subs(f, x, a);
  fc = subs(f, x, root);
  if(fa*fc < 0)
    b = root;
  else
    a = root;
  end
  n = n + 1;
  root = (a + b)/2;
  check = (b0 - a0)/2^(n + 1);   
end

fprintf('Root = %.15f, found in n = %d steps.', root, n);

end

