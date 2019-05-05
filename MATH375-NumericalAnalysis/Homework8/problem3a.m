f = @(x) (1+x.^2).^-1;
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

title('Errors in approximating 1/(1+x^2) on [-1, 1]')
loglog(h, h.^8, '--k', h, errorIG, '-or', 'LineWidth', 1.2)
xlabel('1/64 <= h <= 1/2')
ylabel('Errors in approximation')
legend('h^8', 'Gauss-Legendre Quadrature')