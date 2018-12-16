clc;

times = [0 0 0 0];
lengths = [0 0 0 0];

load('eiffel1.mat')
total = 0;
for i = 1:100
    B = zeros(length(A));
    index = ceil((length(A))*rand);
    B(index) = 1/5;
    t = cputime;
    A\B;
    total = total + cputime - t;
end
disp('Finished eiffel1')
disp(total/100);
times(1) = total/100;
lengths(1) = length(A);


load('eiffel2.mat')
total = 0;
for i = 1:100
    B = zeros(length(A));
    index = ceil((length(A))*rand);
    B(index) = 1/5;
    t = cputime;
    A\B;
    total = total + cputime - t;
end
disp('Finished eiffel2')
disp(total/100);
times(2) = total/100;
lengths(2) = length(A);

load('eiffel3.mat')
total = 0;
for i = 1:100
    B = zeros(length(A));
    index = ceil((length(A))*rand);
    B(index) = 1/5;
    t = cputime;
    A\B;
    total = total + cputime - t;
end
disp('Finished eiffel3')
disp(total/100);
times(3) = total/100;
lengths(3) = length(A);

load('eiffel4.mat')
total = 0;
for i = 1:100
    B = zeros(length(A));
    index = ceil((length(A))*rand);
    B(index) = 1/5;
    t = cputime;
    A\B;
    total = total + cputime - t;
end
disp('Finished eiffel4')
disp(total/100);
times(4) = total/100;
lengths(4) = length(A);

loglog(lengths, times);
hold on;
loglog(lengths(1), times(1), 'o');
hold on;
loglog(lengths(2), times(2), 'o');
hold on;
loglog(lengths(3), times(3), 'o');
hold on;
loglog(lengths(4), times(4), 'o');

xlabel('N')
ylabel('CPU time')

%From theory, we can expect the cost to be roughly m*O(n^3)
%From the plot, we can observe that the CPU running time is approximately
%of order of n^3.