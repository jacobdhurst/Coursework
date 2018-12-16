function integral = trapezoidal(f, a, b, n)
    h = (b-a)/n;
    x = linspace (a, b, n+1);
    eval = zeros(1, n);

    for i = 1:n+1
        eval(i) = feval(f, x(i));
    end

    w = [0 ones(1,n)]+[ones(1,n) 0];

    integral = (h/2)*sum(w.*eval);
end