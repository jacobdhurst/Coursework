xnew = [-1; 0; 2; 3; 5; 6; 8];
ynew = [-1; 2; 0; 5; 6; 9; 12];

a = interpvan(xnew, ynew); 
yv = vaninterp(a, xnew);

plot(xnew, yv, '-ob', xnew, ynew, '*r', 'MarkerSize', 10);
axis([-1.5, 8.5, -3, 17]);
%fplot(yv, [-1.5 8.5], '-b');
hold on;
plot(xnew, ynew, '*r');

xlabel('x');
ylabel('y');
legend('P(x)', 'Data Points');

% Constructing vandermonde
n = length(xnew);
V = zeros(n, n);
for i = 1:n
    V(i,:) = (xnew(i)*ones(1,n)).^(0:n-1);
end

%Finding condition number
condition = cond(V, inf);
disp(condition);

%Finding residual
residual = norm(V*a - ynew, 2);
disp(residual);



% x = linspace(0,2*pi,100);
% y = sin(x);
% 
% x1 = 2*pi*sort(rand(4,1));
% y1 = sin(x1);
% 
% a = interpvan(x1, y1);
% pval = vaninterp(a, x);
% 
% plot(x, y, x, pval, '--', x1, y1, '*');

% Plotting interpolating polynomial
% x = linspace(-1.5, 8.5, 7);
% y = linspace(-2, 14, 7);