clc;

x = linspace(-10, 10);
y = linspace(0,0);
f = x - 4*sin(2*x) - 3;

hold on;
grid on;
set(gca, 'fontsize', 20);
plot(x, y, 'k-', 'Linewidth', 1);
plot(y, x, 'k-', 'Linewidth', 1);
plot(x, f, 'r-', 'Linewidth', 2);
xlabel('x');
ylabel('f(x)=x-4sin(2x)-3');
axis([-10 10 -10 10]);

% There are a total of 5 x-intercepts for the function.
% 2 somewhere between -1 & 0
% 3 somewhere between 0 & 5