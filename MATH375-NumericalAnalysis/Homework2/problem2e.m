%Comparing convergence properties of Fixed Point Iteration, Newton's
%Method, and Secant Method for the function f(x)=x-4*sin(2*x)-3 in finding
%the root r = -0.54444... Plotting the error of each as a function of
%iterations i = 1,2,...,n

root          = nm(-.5, 15, 50);

guess         = -.5;
precision     = 10;
iterations    = linspace(1, 25, 25);

fpi_errors    = iterations;
nm_errors     = iterations;
secant_errors = iterations;

clc; 

for i = iterations
    fpi_errors(iterations==i)    = abs(root - fpi(guess, precision, i));
    nm_errors(iterations==i)     = abs(root - nm(guess, precision, i));
    secant_errors(iterations==i) = abs(root - secant(guess-.1, guess+.1, precision, i));
end

set(gca, 'fontsize', 15);
xlabel('Iteration');
ylabel('Error');
hold on;

semilogy(iterations, fpi_errors, '-ro', 'MarkerSize', 5, 'MarkerFaceColor', [1, .6, .6], 'MarkerEdgeColor', 'red');
semilogy(iterations, nm_errors, '-go', 'MarkerSize', 5, 'MarkerFaceColor', [.6, 1, .6], 'MarkerEdgeColor', 'green');
semilogy(iterations, secant_errors, '-bo', 'MarkerSize', 5, 'MarkerFaceColor', [.6, .6, 1], 'MarkerEdgeColor', 'blue');

legend('Fixed Point Iteration', 'Newtons Method', 'Secant Method');