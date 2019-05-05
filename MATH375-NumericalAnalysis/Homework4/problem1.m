clc

load('eiffel1.mat');
trussplot(xnod, ynod, bars, 'b');

B = zeros(522,1);
B(499) = 1/5;
    
X = A\B;

xload = xnod + X(1:2:end); 
yload = ynod + X(2:2:end);

trussplot(xload, yload, bars, 'r');

plot(xload(249), yload(249), 'g-*')