% Using a for-loop
clc;

tic;
x = 0:(pi/1e8):pi;
y = cos(x);
summation = 0;
for k = 1:length(x)
   summation = summation + (x(k) * y(k)); 
end
toc;

disp(summation);
