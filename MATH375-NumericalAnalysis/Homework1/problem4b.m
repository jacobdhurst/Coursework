% Using a built-in sum command
clc;

tic;
x = 0:(pi/1e8):pi;
y = cos(x);
summation = sum(x*y');
toc;

disp(summation);