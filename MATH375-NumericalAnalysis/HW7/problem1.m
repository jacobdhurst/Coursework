% Part a
load data1.mat;
figure(1);
set(gca, 'fontsize', 20);
plot(x, y, 'ro');
xlabel('x');
ylabel('y');

%Part b
%phi = [1, cos(x), exp(-x)];
A = ones(300, 3);
A(:,2) = cos(x);
A(:,3) = exp(-x);

%Part c
a = A\y;
model = A*a;

hold on;
plot(x, model, '-b')
legend('data points', 'model')

r = norm(y - model)/norm(y);
disp(r);

%The largest error in this model is 0.0256, this could
%likely be occurring in a gap of data points 
%i.e., the first few data points, or within the offset of 1
%all 0.0256 points occur in row 1 of model which is phi(x) = 1