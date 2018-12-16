module Exam1Review where

{-Chapter 1-}
double :: Int -> Int
double x = x + x

products :: [Int] -> Int
products [] = 1
products (x:xs) = x * products xs

qsort :: [Int] -> [Int]
qsort [] = []
qsort (x:xs) = qsort larger ++ [x] ++ qsort smaller
 where smaller = [a | a <- xs, a <= x]
       larger = [b | b <- xs, b > x]

{-Chapter 2-}
nN = a `div` length xs
     where 
         a = 10
         xs = [1, 2, 3, 4, 5]

final :: [Int] -> Int
final [] = 0
final xs = head(reverse xs)

shorten :: [Int] -> [Int]
shorten [] = []
shorten xs = reverse(drop 1 (reverse xs))
--shorten xs = reverse(tail(reverse xs))
{-Chapter 3-}
--no exercises require GHCi

{-Chapter 4-}
halve :: [a] -> ([a],[a])
halve [] = ([],[])
halve xs
 | length xs `mod` 2 == 0 = (take (length xs `div` 2) xs, drop (length xs `div` 2) xs)
 | otherwise          = (take (((length xs)+1) `div` 2) xs, drop (((length xs)+1) `div` 2) xs)

third :: [a] -> a
third (x1:x2:x3:xs) = x3 {-
 | length xs >= 3 = xs !! 2 --head(tail(tail xs))
 | otherwise = head xs -}

{-Chapter 5-}
grid :: Int -> Int -> [(Int, Int)]
grid m n = [(x, y) | x <- [0..m], y <- [0..n]]

square :: Int -> [(Int, Int)]
square n = [(x, y) | (x, y) <- (grid n n), x /= y]

replicator :: Int -> a -> [a]
replicator n a = [a | _ <- [1..n]]

pyths :: Int -> [(Int, Int, Int)]
pyths n = [(a, b, c) | a <- [1..n], b <- [1..n], c <- [1..n], ((a*a + b*b) == (c*c))]

factors :: Int -> [Int]
factors n = [x | x <- [1..n], n `mod` x == 0]

perfects :: Int -> [Int]
perfects n = [x | x <- [1..n], sum (factors x) == 2*x]

scalarProduct :: [Int] -> [Int] -> Int
scalarProduct xs ys = sum [x*y | (x,y) <- (zip xs ys)]

{-Chapter 6-}
fact :: Int -> Int
fact 0 = 1
fact n 
 | n > 0 = n*fact(n-1)
 | otherwise = 0

expuh :: (Num a, Integral b) => a -> b -> a
expuh _ 0 = 1
expuh x n = x * (expuh x (n-1))

nondec :: [Int] -> Bool
nondec [] = True
nondec xs = checkPairs (zip xs (tail xs))
 where  checkPairs [] = True
        checkPairs ((i,j):xs)
         | i <= j = True && checkPairs xs
         | otherwise = False
