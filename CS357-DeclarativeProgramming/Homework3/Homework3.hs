{-
Jacob Hurst
Jhurst
-}

-- module Homework3 
-- ( Tree(LeafT,NodeT),
--   balance,
--   goldbach,
--   church,
--   powerset,
--   makeCommand,
--   T(Leaf,Node),
--   P(GoLeft,GoRight,This),
--   allpaths,
--   eval,
--   satisfiable 
-- ) where

--No Other Imports Are Allowed
import Data.List
-- import Debug.Trace

--3.1 Lists And Trees (10pts)
data Tree a = LeafT a | NodeT (Tree a) (Tree a) deriving (Eq, Show)

balance :: [a] -> Tree a
balance [x] = LeafT x
balance xs = NodeT (balance l) (balance r)
 where (l, r) = splitAt (length xs `div` 2) xs

-- Tests for tree balance
balanced :: Tree a -> Bool
balanced (LeafT a) = True
balanced (NodeT l r) = (abs (count l - count r) <= 1) && balanced l && balanced r

-- Counts leafs in tree
count :: Tree a -> Int
count (LeafT a) = 1
count (NodeT l r) = count l + count r

--3.2 Simple Functions On Numbers (10pts)
goldbach :: Int -> [(Int, Int)]
goldbach n 
 | (even n && n > 2) = [(a, b) | a <- primes 0 (n `div` 2), b <- primes (n `div` 2) n, (a + b) == n]
 | otherwise = error "Given n was invalid."

-- Generates list of primes in given range
primes :: Int -> Int -> [Int]
primes start end = [x | x <- [start..end], isPrime x]

-- Checks for primality
isPrime :: Int -> Bool
isPrime n
 | (n == 2) = True
 | (even n) = False
 | (n > 2) = (length [x | x <- [2..(ceiling $ sqrt $ fromIntegral n)], (n `mod` x) == 0]) == 0
 | otherwise = False 

--3.3 Higher-Order Functions (10pts)
church :: Int -> (c -> c) -> c -> c
church n = foldr (.) id . replicate n

--3.4 Recursive Functions Over Lists (10pts)
type Set = [Int]

powerset :: [Int] -> [[Int]]
powerset = subsequences 

--3.5 Lists And Strings (10pts)
example :: [[(Double, Double)]]
example = [[(100.0,100.0),(100.0,200.0),(200.0,100.0)],
  [(150.0,150.0),(150.0,200.0),(200.0,200.0),(200.0,150.0)]]

out = "%!PS-Adobe-3.0 EPSF-3.0\n%%BoundingBox: 100.0 100.0 200.0 200.0\n\n100.0 100.0 moveto\n100.0 200.0 lineto\n200.0 100.0 lineto\nclosepath\nstroke\n\n150.0 150.0 moveto\n150.0 200.0 lineto\n200.0 200.0 lineto\n200.0 150.0 lineto\nclosepath\nstroke\n\nshowpage\n%%EOF\n"

--putStr $ makeCommand example
makeCommand :: [[(Double, Double)]] -> String
makeCommand zs = header ++ (getBounds zs) ++ (concatMap moveTo zs) ++ footer

header = "%!PS-Adobe-3.0 EPSF-3.0\n%%BoundingBox: "
footer = "showpage\n%%EOF"
close = "closepath\nstroke\n\n"

getBounds :: [[(Double, Double)]] -> String
getBounds = 
 let replace ',' = ' '
     replace c   = c
 in map replace . (filter (not . (`elem` "()")) . line)
 where line zs = (show (minimum (concat zs))) ++ " " ++ (show (maximum (concat zs))) ++ "\n\n"

moveTo :: [(Double, Double)] -> String
moveto [] = ""
moveTo ((x, y):zs) = (show x) ++ " " ++ (show y) ++ " moveto\n" ++ lineTo zs 

lineTo :: [(Double, Double)] -> String
lineTo [] = close
lineTo ((x, y):zs) = (show x) ++ " " ++ (show y) ++ " lineto\n" ++ lineTo zs 

--3.6 Trees (25pts)
data T = Leaf | Node T T deriving (Eq, Show)
data P = GoLeft P | GoRight P | This deriving (Eq, Show)

t1 = (Node (Node Leaf Leaf) (Node Leaf Leaf)) 
-- allpaths evaluates to [This, GoLeft This, GoLeft (GoLeft This), GoLeft (GoRight This), GoRight This, GoRight (GoLeft This), GoRight (GoRight This)]
t2 = (Node (Leaf) (Node Leaf Leaf)) 
-- allpaths evaluates to [This, GoLeft This, GoRight This, GoRight (GoLeft This), GoRight (GoRight This)]

allpaths :: T -> [P]
allpaths (Leaf) = This:[]
allpaths (Node l r) = This:((map GoLeft $ allpaths l) ++ (map GoRight $ allpaths r))

--3.7 Logic (25pts)
type Expr = [[Int]]

e1 :: Expr
e1 = [[-1],[1]]
-- satisfiable evaluates to False
e2 :: Expr
e2 = [[1..10],[(-10)..(-1)]]
-- satisfiable evaluates to True
e3 :: Expr
e3 = [[1,2,4],[-1,2]]
-- satisfiable evaluates to True
e4 :: Expr
e4 = [[-1, 2, 4], [-2, -3]]
-- satisfiable evaluates to True
e5 :: Expr
e5 = [[-1], [1,2]]
-- satisfiable evaluates to True
e6 :: Expr
e6 = [[-1, 3], [2, -3]]
-- satisfiable evaluates to True

eval :: (Int -> Bool) -> Expr -> Bool
eval p [] = True
eval p (l:ls) = (eval' p l) && (eval p ls)

eval' :: (Int -> Bool) -> [Int] -> Bool
eval' p [] = False
eval' p (l:ls)
 | (l > 0) = (p l) || (eval' p ls)
 | otherwise = (not $ p (abs l)) || (eval' p ls)

satisfiable :: Expr -> Bool
satisfiable xs = or [True | expression <- possible xs, eval (try expression) xs]
 where possible xs = permute $ nub $ (map abs) $ concat xs
       try xs x = xs !! ((abs x) - 1)

permute :: [Int] -> [[Bool]]
permute xs = convert [expression | expression <- sequence (choices xs), length expression == length xs]
 where choices [] = []
       choices (x:xs) = [x, -x] : choices xs
       convert [] = []
       convert (x:xs) = map (>=0) x : convert xs