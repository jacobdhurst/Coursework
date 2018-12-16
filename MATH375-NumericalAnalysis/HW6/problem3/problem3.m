clf;

axis([-1 1 -1 1]);
[pts] = ginput;

x = pts(:,1);
y = pts(:,2);

figure;

% Part a
a = interpnewton(x, y);
p = newtoninterp(a, x, y);

subplot(2, 1, 1);
axis([-1 1 -1 1]);
plot(x, y, '*r', x, p, '-ob');
title('High Order Interpolation:');

% Part b
ccs = spline(x, [0 transpose(y) 0]);
xx = linspace(min(x), max(x), length(x));
yy = ppval(ccs, xx);

subplot(2, 1, 2);
axis([-1 1 -1 1]);
plot(x, y, '*r', xx, yy, '-ob');
title('Spline:');