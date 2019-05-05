function p = lagrange(xl, yl)

syms x;
n = length(xl);
p = 0;

for i=1:n
  l = 1;
  for j=1:n
      if i~=j
          l = l*((x-xl(j))/(xl(i)-xl(j)));
      end
  end
  p = p + (yl(i)*l);
end

end