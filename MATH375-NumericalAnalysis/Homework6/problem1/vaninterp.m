function ynew = vaninterp(a, xnew)

syms x;
n = length(a);
ynew = a(n)*ones(size(xnew));

for i=(n-1):-1:1
    ynew = (xnew.*ynew) + a(i);%x
end

end

