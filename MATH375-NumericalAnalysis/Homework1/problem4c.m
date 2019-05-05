% Using vectorized implementation
clc;

tic;
summation=sum((0:(pi/1e8):pi).*cos((0:(pi/1e8):pi)));
toc;

disp(summation);