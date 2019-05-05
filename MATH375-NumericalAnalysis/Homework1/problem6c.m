clc;

a = -1;
b = 1;

N = [10 20 40 80 160 320 640 1280];

x = linspace(a, b, length(N));
real = 1/exp(1);
M = [0 0 0 0 0 0 0 0];
err = [0 0 0 0 0 0 0 0];

xlabel('N');
ylabel('Error');
set(gca, 'fontsize', 10);
hold on;

for n = N
    M(N==n) = my_mean(my_fun(x), a, b, n);
    err(N==n) = abs(M(N==n) - real);
    
    semilogy(n, err(N==n), '-s', 'MarkerSize', 10,...
        'MarkerFaceColor', [1, .6, .6],...
        'MarkerEdgeColor', 'red');
end

T = table(N', M', err');
T.Properties.VariableNames = {'N' 'M' 'Error'};