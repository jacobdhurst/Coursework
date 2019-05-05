function integral = simpsons(f, a, b, n)
    h = (b-a)/n;
    x = linspace(a, b, n+1);
    eval = zeros(1, n);
    
    for i = 1:n+1
        eval(i) = feval(f, x(i));
    end
    
    w          = [1 zeros(1, n-1) 1];
    w(2:2:n)   = 4*ones(1, n/2);
    w(3:2:n-1) = 2*ones(1, n/2-1);

    integral = (h/3)*sum(w.*eval);
end