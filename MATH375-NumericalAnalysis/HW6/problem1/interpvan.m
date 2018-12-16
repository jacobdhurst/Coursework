function a = interpvan(x, y)

n = length(x);
V =  zeros(n,n);

for i = 1:n
    V(i,:) = (x(i)*ones(1,n)).^(0:n-1);
end

if(isrow(y))
    y = transpose(y);
end
a = V\y;

end