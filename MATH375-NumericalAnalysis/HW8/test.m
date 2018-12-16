f = @(x) (sin(x)+cos(x));
a = 0;
b = 4*pi;
n = 2;
I = integral(f, -1, 1, 'AbsTol', 1e-16);
errorIG = zeros(1, 6);
h = errorIG;

for i=1:10
    h(i) = 2/n;
    [x, w] = legendreknots(n, [a b]);
    IG = sum((feval(f, x)).*w);
    errorIG(i) = abs(I - IG);
    n = n * 2;
end

disp(errorIG);

title('Errors in approximating 1/(1+x^2) on [-1, 1]')
plot(h, errorIG, '-or', 'LineWidth', 1.2)
xlabel('1/64 <= h <= 1/2')
ylabel('Errors in approximation')
legend('h^8', 'Gauss-Legendre Quadrature')