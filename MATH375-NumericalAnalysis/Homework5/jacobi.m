loader;

%check strictly diagonally dominant
disp(A);
for m = 1 : length(A)
    row = abs( A(m,:) );
    d = sum(row) - row(m);
    if(row(m) <= d)
        fprintf('Warning: A is not strictly diagonally dominant.\n');
        break;
    end
end

iterations = 0;
x = zeros(6, 1);
n = size(x, 1);
error = inf;
tolerance = 0.001;

while error > tolerance
    oldx = x;
    x = Bj*oldx + cj;

    iterations = iterations + 1;
    error = norm((oldx - x), 2);
end

disp(x);
disp(iterations);