% Some figures involving x, x^2, x^3, x^4.
clc;

N  = 1000;

x  = linspace(0, 1, N);

y1 = x;
y2 = x.^2;
y3 = x.^3;
y4 = x.^4;

% plot - standard plot, not a large range without cramming x & y axis, useful for linear functions
subplot(2, 2, 1);
hold on;
plot(x, y2, 'g-', 'Linewidth', 2);
plot(x, y3, 'b-', 'Linewidth', 2);
plot(x, y4, 'k-', 'Linewidth', 2);

legend('y = x', 'y = x^2', 'y = x^3', 'y = x^4');
axis([0 1 0 1]);

% semilogx - allows for very large scale on x axis, useful for substantial change in x axis
subplot(2, 2, 2);
set(gca, 'fontsize', 20);
semilogx(x, y1, 'r-', 'Linewidth', 2);
hold on;
semilogx(x, y2, 'g-', 'Linewidth', 2);
semilogx(x, y3, 'b-', 'Linewidth', 2);
semilogx(x, y4, 'k-', 'Linewidth', 2);
xlabel('x');
ylabel('y (semilogx)');
axis([0 1 0 1]);

% semilogy - allows for very large scale on y axis, useful for timescales
subplot(2, 2, 3);
set(gca, 'fontsize', 20);
semilogy(x, y1, 'r-', 'Linewidth', 2);
hold on;
semilogy(x, y2, 'g-', 'Linewidth', 2);
semilogy(x, y3, 'b-', 'Linewidth', 2);
semilogy(x, y4, 'k-', 'Linewidth', 2);
xlabel('x');
ylabel('y (semilogy)');
axis([0 1 0 1]);

% loglog - allows for very large scale on x & y axis, useful for exponential functions
subplot(2, 2, 4);
set(gca, 'fontsize', 20);
loglog(x, y1, 'r-', 'Linewidth', 2);
hold on;
loglog(x, y2, 'g-', 'Linewidth', 2);
loglog(x, y3, 'b-', 'Linewidth', 2);
loglog(x, y4, 'k-', 'Linewidth', 2);
xlabel('x');
ylabel('y (loglog)');
axis([0 1 0 1]);