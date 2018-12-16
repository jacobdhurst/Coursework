function ynew = newtoninterp(a, x0, xnew)

syms x;
n = length(a);
ynew = a(n)*ones(size(xnew));

for i=n-1:-1:1
    ynew = (xnew-x0(i)).*ynew + a(i);%x
end

end