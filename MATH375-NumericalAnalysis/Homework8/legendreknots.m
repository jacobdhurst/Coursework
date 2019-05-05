function [x,w]=legendreknots(n, Interval)

% Written by Mohammad Motamed @ ICES, UT Austin on Aug 10, 2012

xL=Interval(1); xR=Interval(2);

a=zeros(n,1);
b=zeros(n,1); b(1)=2; b(2:n)=1./(4-1./(1:n-1).^2);
J=diag(a)+diag(sqrt(b(2:n)),1)+diag(sqrt(b(2:n)),-1);

[V,D] = eig(J);

x = diag(D); 
[x,ind] = sort(x);
x = (xR-xL)/2*x' + (xL+xR)/2;

w = V(1,ind).^2;
w=2*w;