h = logspace(-1, -16, 100);
f0 = 1;
fd = (exp(h)-exp(-h))./(2.*h);
error = abs(f0-fd);

loglog(h, h.*h, '-bo', 'MarkerSize', 5, 'MarkerFaceColor', [.6, .6, 1], 'MarkerEdgeColor', 'blue');
hold on;
loglog(h, error, '-ro', 'MarkerSize', 5, 'MarkerFaceColor', [1, .6, .6], 'MarkerEdgeColor', 'red');

set(gca, 'fontsize', 15);
legend('h^2', 'error');
xlabel('h');
ylabel('error');