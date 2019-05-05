%This is the MATLAB code for the example on page 18 of lecture notes on interpolation
%Written by M. Motamed @ UNM, Fall 2016 (MATH/CS 375)


%Data points
n=4;
X=linspace(0,pi/2,n);
Y=sin(X);


%Compute Newton divided differences
c1=Y(1);

a1=(Y(2)-Y(1))/(X(2)-X(1));
a2=(Y(3)-Y(2))/(X(3)-X(2));
a3=(Y(4)-Y(3))/(X(4)-X(3));
a4=(a2-a1)/((pi/3));
a5=(a3-a2)/((pi/2)-(pi/6));
a6=(a5-a4)/((pi/2));

c2=a1;
c3=a4;
c4=a6;


%Evaluate P at new base points using the nested formula
m=10000;
x_new=linspace(0,pi/2,m);
P=c1+(x_new-X(1)).*(c2+(x_new-X(2)).*(c3+(x_new-X(3)).*(c4)));


%Compute the eror
y_new=sin(x_new);
E=abs(P-y_new);


%Plot results
figure
plot(X,Y,'*k')
hold on
plot(x_new,y_new,'b',x_new,P,'--r')
%Do not forget to label axes, increase the font size, and put a legend

figure
plot(x_new,E)
%Do not forget to label axes and increase the font size