xnew = [-1; 0; 2; 3; 5; 6; 8];
ynew = [-1; 2; 0; 5; 6; 9; 12];

a = interpnewton(xnew, ynew); 
yn = newtoninterp(a, xnew, xnew);

plot(xnew, yn, '-ob', xnew, ynew, '*r', 'MarkerSize', 10); % x instead of xnew?
axis([-1.5, 8.5, -3, 17]);
%fplot(yn, [-1.5 8.5], '-b');
hold on;
plot(xnew, ynew, '*r');
xlabel('x');
ylabel('y');
legend('P(x)', 'Data Points');

residual = norm(yn - ynew);
disp(residual);

% residual of newtons is significantly smaller than residual of vandermonde 
% 1.3265x10^6 vs 6.2585x10^-14