loader;

ej         = eig(Bj);
egs        = eig(Bgs);

spectralj  = max(abs(ej));
spectralgs = max(abs(egs));

fprintf('Jacobi = %f \nGauss-Seidel = %f \n', spectralj, spectralgs);

%since spectral radius of Bj and Bgs are both < 1, we can conclude that
%both will converge.