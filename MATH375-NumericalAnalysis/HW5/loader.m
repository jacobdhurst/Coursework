A   = [2 -1 0 0 0 0; -1 2 -1 0 0 0; 0 -1 2 -1 0 0; 0 0 -1 2 -1 0; 0 0 0 -1 2 -1; 0 0 0 0 -1 2];
b   = transpose([0 2 3 -1 2 1]);

actual = A\b;

I   = eye(6);
D   = diag(diag(A));
Di  = inv(D);
U   = triu(A, 1);
L   = tril(A, -1);

B = (-(Di))*(L + U);
c = Di*b;                        %#ok

Bj  = (-Di)*(L + U);               
cj  = Di*b;                      %#ok

Bgs = (-(inv(I + Di*L)))*(Di*U); %#ok
cgs = (inv(I + Di*L))*(Di*b);    %#ok