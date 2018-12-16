f = @(x) (exp(sin(4*pi*x)));
a = -1;
b = 1;
n = 4;
I = integral(f, -1, 1, 'AbsTol', 1e-16);
errorIG = zeros(1, 6);
h = errorIG;

for i=1:6
    h(i) = 2/n;
    [x, w] = legendreknots(n, [a b]);
    IG = sum((feval(f, x)).*w);
    errorIG(i) = abs(I - IG);
    n = n * 2;
end

disp(errorIG);

title('Errors in approximating (exp(sin(4*pi*x)) on [-1, 1]')
plot(h, h.^(1:6), '--k', h, errorIG, '-or', 'LineWidth', 1.2)
xlabel('1/64 <= h <= 1/2')
ylabel('Errors in approximation')
legend('h raised to powers (1:6)', 'Gauss-Legendre Quadrature')