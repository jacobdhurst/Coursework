% Part a
load data2.mat;
figure(1);
set(gca, 'fontsize', 20);
plot(x, c, 'ro');
xlabel('x');
ylabel('c');

%Part b
y = c.^-1;

A = ones(16, 2);
A(:,1) = x.^-1;
A(:,2) = x.^2;

%A = qr(A);

a = A\y;
model = A*a;

hold on;
plot(x, (1./model), '->b'); %(c-(1./model)./c) accurate

r = norm(c-(1./model))/norm(c); %(c-(1./model)./c) accurate
disp(r);

%Part c
y = log(c/x);

A = ones(16, 5);
for i = 1:5
    A(:,i) = x.^(i-1);
end

a = A\y;
a = a(:,16);
model = A*a;

plot(x, (c-x.*exp(model)./c), '-sk'); %(c-x.*exp(model)./c) accurate
legend('data points', 'model 1', 'model 2')

r = norm(c-x.*exp(model))/norm(c); %c-x.*exp(model)./c accurate
disp(r);

%The new model is a significantly closer fit than the first.