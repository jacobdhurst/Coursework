f = @(x) (1+x.^2).^-1;
a = -1;
b = 1;
n = 4;

I = integral(f, -1, 1, 'AbsTol', 1e-16);

errorIT = zeros(1, 6);
errorIM = errorIT;
errorIS = errorIM;
h = errorIM;

for i=1:6
    h(i) = 2/n;
    
    errorIT(i) = abs(I - trapezoidal(f, a, b, n));
    errorIM(i) = abs(I - midpoint(f, a, b, n));
    errorIS(i) = abs(I - simpsons(f, a, b, n));
    
    n = n * 2;
end

title('Errors in approximating 1/(1+x^2) on [-1, 1]')
loglog(h, h.^2, '--k', h, h.^4, '-*k', h, errorIT, '-or', h, errorIM, '-sg', h, errorIS, '-^b', 'LineWidth', 1.2)
xlabel('1/64 <= h <= 1/2')
ylabel('Errors in approximation')
legend('h^2', 'h^4', 'Trapezoidal', 'Midpoint', 'Simpsons')