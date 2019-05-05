function ynew = newtontest(x, y, xnew)

% Newton's Divided Differences
n = length(x);
A = zeros(n,n);
c = zeros(n);

for j = 1:n
  A(j,1) = y(j);
end

for i = 2:n
  for j = 1:(n+1-i)
    A(j,i) = (A(j+1,i-1)-A(j,i-1))/(x(j+i-1)-x(j));
  end
end

for i = 1:n
  c(i) = A(1,i);
end                   

d = length(c(i)-1);
b = zeros(d, 1);

ynew = c(d+1);
for i = d:-1:1
  ynew = ynew.*(xnew-b(i))+c(i);
end

% Plotting polynomial
plot(x, ynew, '-ok', 'MarkerSize', 5); % Definitely incorrect
axis([-1.5, 8.5, -2, 13]);
xlabel('x');
ylabel('y');
legend('P(x)');

% Computing the residual
residual = norm(A*c - y); % Seems incorrect
disp(residual);
end

% newtoninterp w/ values from lecture yielded approximately correct results

% function ynew = newtoninterp(x, y, xnew)
% if(length(x) ~= length(y))
%     error('Dimensions of x & y do not agree.');
% end
% 
% n = length(x);
% a = y;
% c = y;
% 
% % p_test = polyfit(x, y, n-1);
% % p_plot = polyval(p_test,x);
% % plot(x, p_plot)
% 
% % Finding the Newton Coefficient.
% for i=1:3
%     a(i) = (a(i+1)-a(i))/(x(i+1)-x(i));
% end
% 
% for i=4:n
%     a(i) = 
% end
% 
% % Finding the Newton Polynomial.
% c = a(n);
% for i=1:n-1
%   c = a(n-i) + (xnew(i) - x(n-i))*c;
% end
% 
% ynew = c;
% 
% end