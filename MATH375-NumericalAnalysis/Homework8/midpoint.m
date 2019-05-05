function integral = midpoint(f, a, b, n)
    h = (b-a)/(2*n);
    x = linspace(a+h, b-h, n);
    sum = 0.0;
    
    for i = 1:n
        sum = sum+feval(f, x(i));
    end

    integral = 2*h*sum;
%     h = (b-a)/n;
%     w = linspace(0, (n-1), n)*h+(h/2);
%     f = feval(f, w)./w;
%     integral = h*sum(f);
end