clc

load('eiffel4.mat');
trussplot(xnod, ynod, bars, 'b');

displacement = zeros(length(A), 1);
maxX = 0;
node = 0;

start = cputime;
for i=1:length(A)
  B = zeros(length(A), 1);
  B(i) = 1/5;
    
  X = A\B;

  xload = xnod + X(1:2:end); 
  yload = ynod + X(2:2:end);
  
  if norm(X) > maxX 
    node=i;
    maxX= xload;
    maxY= yload;        
  end 
end
total = cputime - start;

trussplot(maxX, maxY, bars, 'r');

fprintf('Max displacement at node: %d\n', node);
fprintf('CPU time: %f\n', total);
hold on;
index = ceil(node/2);
plot(maxX(index), maxY(index), 'g-*')