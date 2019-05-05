figure

% Function
syms x;
n = [10, 20, 40];
f = 1/(1+(16*(x.^2)));

% Naive points
xa = zeros(3, 40);
for i=1:3
    temp = 0;
    range = n(i);
    for j=1:range
        xa(i,j) = (-1)+(2*(j-1))/(range-1);
        temp = temp + 1;
    end
    xc = xa(i, 1:range);
    yc = subs(f, x, xc);
    yla = lagrange(xc, yc);
    
    subplot(2, 3, i);
    fplot(yla, [-1 1], '-b');
    hold on;
    plot(xc, yc, '*r');
    axis([-1 1 -0.5, 1.5]);
    str = strcat('Evenly Spaced: n=', num2str(temp));
    title(str);
    hold on;
end

% Chebyshev points
xb = zeros(3, 40);
for i = 1:3
    temp = 0;
    range = n(i);
    for j=1:range
        xb(i, j) = cos(pi*((2*j)-1)/(2*range));
        temp = temp + 1;
    end
    xc = xb(i, 1:range);
    yc = subs(f, x, xc);
    ylb = lagrange(xc, yc);
    
    subplot(2, 3, (i+3));
    fplot(ylb, [-1 1], '-b');
    hold on;
    plot(xc, yc, '*r');
    axis([-1 1 -0.5, 1.5]);
    str = strcat('Chebyshev: n=', num2str(temp));
    title(str);
    hold on;
end