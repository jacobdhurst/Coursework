function a = interpnewton(x, y)

n = length(x);

for i=1:(n-1)
    y(i+1:n) = (y(i+1:n)-y(i)) ./ (x(i+1:n) - x(i));
end

a = y;

end