{-
  Textbook Problems: Chapters 1 - 12, 15, & 16.
-}
--Chapter 1.
--1
quadruple :: Int -> Int    
quadruple x = x + x + x + x

-- double (double x)

--2
sum' :: [Int] -> Int
sum' [] = 0
sum' (x:xs) = x + sum' xs

-- sum' [x] = x + sum' [] (by definition) = x + 0 = x [QED]

--3
product' :: [Int] -> Int
product' [] = 1
product' (x:xs) = x * product' xs

-- product (2:[3, 4]) = 2 * product (3:[4]) = 2 * 3 * product (4:[]) = 2 * 3 * 4 * 1 = 24 [QED]

--4
qsort [] = []
qsort (x:xs) = qsort s ++ [x] ++ qsort l
  where s = [a | a <- xs, a <= x]
        l = [b | b <- xs, b > x]

-- you could just apply reverse to the output or even better, just swap s & l

--5
-- values that are equal to x in our comparison would be discarded
-- example -> [2, 2, 3, 1, 1] -> [1, 2, 3]

--Chapter 2.
--1
double :: Num a => a -> a
double x = x + x

quadruple' :: Num a => a -> a
quadruple' x = double (double x)

factorial :: Int -> Int
factorial n
  | (n <= 0) = 1
  | otherwise = n * (factorial (n-1))

average :: [Int] -> Int
average xs = sum xs `div` length xs

--2
--((2^3)*4), ((2*3)+(4*5)), (2+(3*(4^5)))

--3
n = a `div` (length xs)
  where a  = 10 
        xs = [1, 2, 3, 4, 5]

--4
last' :: [a] -> a
last' xs = head $ reverse xs

--5
init' :: [a] -> [a]
init' xs = take (length xs - 1) xs

init'' :: [a] -> [a]
init'' xs = reverse $ drop 1 (reverse xs)

--Chapter 3.
--1
-- :: [Char] or String
-- :: (Char, Char, Char)
-- :: [(Bool, Char)]
-- :: ([Bool], [Char])
-- :: [([a] -> [a])]

--2
bools :: [Bool]
bools = replicate 100 True

nums :: [[Int]]
nums = [[1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]]

add :: Int -> Int -> Int -> Int
add x y z = x + y + z

copy :: a -> (a, a)
copy x = (x, x)

apply' :: (a -> b) -> a -> b
apply' f a = f a

--3
-- :: [a] -> a
-- :: (a, b) -> (b, a)
-- :: a -> b -> (a, b)
-- :: Num a => a -> a
-- :: [a] -> Bool
-- :: (a -> b) -> a -> b

--4
-- checked

--5
-- it is much more difficult to determine if two functions are the same 
-- for all inputs as the amount of input possibilities could be endless.
-- additionally, there is the complexity of evaluating efficiency.

--Chapter 4
--1
halve :: [a] -> ([a], [a])
halve xs = splitAt (length xs `div` 2) xs

--2
third :: [a] -> a
--third xs = head (tail (tail xs))
--third xs = xs !! 2
third (x1:x2:x3:xs) = x3

--3
safetail :: [a] -> [a]
--safetail xs = if null xs then [] else tail xs
--safetail xs
-- | null xs = []
-- | otherwise = tail xs
safetail [] = []
safetail xs = tail xs

--4
--(||) :: Bool -> Bool -> Bool
--True || True   = True
--True || False  = True
--False || True  = True
--False || False = False

--True || _    = True
--_    || True = True
--_    || _    = False

--False || False = False
--_     || _     = True

--5
--if a then (if b then (True) else False) else False

--6
--if a then b else false

--7
mult :: Int -> Int -> Int -> Int
mult = (\x y z -> x * y * z)

--8
luhn :: Int -> Int -> Int -> Int -> Bool
luhn a b c d = ((luhnDouble a) + b + (luhnDouble c) + d) `mod` 10 == 0

luhnDouble :: Int -> Int
luhnDouble x = if (2*x > 9) then (2*x - 9) else (2*x)

--Chapter 5.
factors :: Integer -> [Integer]
factors n = [x | x <- [2..(floor $ sqrt $ fromIntegral n)], n `mod` x == 0]

factors' :: Int -> [Int]
factors' n = [x | x <- [1..n], n `mod` x == 0, n /= x]

primes :: [Integer]
primes = [x | x <- [2..], factors x == []]

--1
sum'' :: Integer
sum'' = sum [x*x | x <- [1..100]]

--2
grid :: Int -> Int -> [(Int, Int)]
grid m n = [(x, y) | x <- [0..m], y <- [0..n]]

--3
square :: Int -> [(Int, Int)]
square n = [(x, y) | (x, y) <- grid n n, x /= y]

--4
replicate' :: Int -> a -> [a]
replicate' n x = [x | _ <- [1..n]]

--5
pyths :: Int -> [(Int, Int, Int)]
pyths n = [(x, y, z) | z <- [1..n], y <- [1..(z-1)], x <- [1..(z-1)], x*x + y*y == z*z]

--6
perfects :: Int -> [Int]
perfects n = [x | x <- [1..n], x == (sum $ factors' x)]

--7
ex = concat [[(x, y) | x <- [1, 2]] | y <- [3, 4]]

--8 
positions :: Eq a => a -> [a] -> [Int]
positions x xs = find x (zip xs [0..])

find :: Eq a => a -> [(a, b)] -> [b]
find k t = [v | (k', v) <- t, k == k']

--9
scalarproduct :: [Int] -> [Int] -> Int
scalarproduct xs ys = sum [x*y | (x, y) <- zip xs ys]

--Chapter 6.
--1
-- completed earlier

--2
sumdown :: Int -> Int
sumdown n 
  | n <= 0    = 0
  | otherwise = n + sumdown (n-1)

--3
toPower :: Int -> Int -> Int
toPower a b
  | (b >= 1) = a * toPower a (b-1)
  | otherwise          = 1   

--4
euclid :: Int -> Int -> Int
euclid a b 
  | (a == b)  = a
  | otherwise = euclid smaller (larger - smaller) 
    where (smaller, larger) = if a > b then (b, a) else (a, b)
    
--5
--length (1:[2, 3]) = 1 + length (2:[3]) = 1 + 1 + length (3:[]) = 1 + 1 + 1 + length [] = 1 + 1 + 1 + 0 = 3
--drop 3 (1:[2, 3, 4, 5]) = drop 2 (2:[3, 4, 5]) = drop 1 (3:[4, 5]) = drop 0 (4:[5]) = [4, 5]
--init (1:[2, 3]) = 1:(init (2:[3])) = 1:(2:(init ([3]))) = [1, 2]

--6
and' :: [Bool] -> Bool
and' []     = True
and' (x:xs) = x && and' xs

concat' :: [[a]] -> [a]
concat' []       = []
concat' (xs:xss) = xs ++ (concat' xss)

replicate'' :: Int -> a -> [a]
replicate'' 0 x = []
replicate'' n x = x:(replicate'' (n-1) x)

select :: [a] -> Int -> a
select [] _     = undefined
select (x:xs) 0 = x
select (x:xs) n = select xs (n-1)

elem' :: Eq a => a -> [a] -> Bool
elem' x' []     = False
elem' x' (x:xs) = if (x' == x) then True else elem' x' xs 

--7
merge :: Ord a => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys)
  | (x > y)   = y:(merge (x:xs) ys)
  | otherwise = x:(merge xs (y:ys))

--8
msort :: Ord a => [a] -> [a]
msort []  = []
msort [x] = [x]
msort xs  = merge (msort l) (msort r)
  where (l, r) = halve xs

--9
sum''' :: [Int] -> Int
sum''' []     = 0
sum''' (x:xs) = x + sum''' xs

take' :: Int -> [a] -> [a]
take' 0 _      = []
take' n []     = []
take' n (x:xs) = x:(take' (n-1) xs) 

last'' :: [a] -> a
last'' [x]    = x
last'' (x:xs) = last' xs

--Chapter 7.
--1
fun f p = map f . filter p

--2
all' :: (a -> Bool) -> [a] -> Bool
all' p xs = length xs == (length . filter p) xs

any' :: (a -> Bool) -> [a] -> Bool
any' p xs = (length . filter p) xs > 0

takeWhile' :: (a -> Bool) -> [a] -> [a]
takeWhile' p [] = []
takeWhile' p (x:xs) = if p x then x : takeWhile' p xs
                      else []

dropWhile' :: (a -> Bool) -> [a] -> [a]
dropWhile' p [] = []
dropWhile' p (x:xs) = if p x then dropWhile' p xs
                      else (x:xs)  

--3
map' :: (a -> b) -> [a] -> [b]
map' f = foldr (\x xs -> f x : xs) []

filter' :: (a -> Bool) -> [a] -> [a]
filter' p = foldr (\x xs -> if p x then (x:xs) else xs) [] 

--4
dec2int :: [Int] -> Int
dec2int = foldl (\x xs -> 10*x + xs) 0 

--5
curry' :: ((a, b) -> c) -> a -> b -> c
curry' f x y = f (x, y)

uncurry' :: (a -> b -> c) -> (a, b) -> c
uncurry' f (x, y) = f x y

--6
unfold p h t x | p x       = []
               | otherwise = h x : unfold p h t (t x)

int2bin = unfold (==0) (`mod` 2) (`div` 2)

map'' f = unfold (null) (f. head) (tail)

iterate' :: (a -> a) -> a -> [a]
iterate' f = unfold (\_ -> False) id f

--7
altMap :: (a -> b) -> (a -> b) -> [a] -> [b]
altMap f g xs = alt f g True xs
  where alt f g p []     = []
        alt f g p (x:xs) = if p then (f x):(alt f g (not p) xs)
                           else (g x):(alt f g (not p) xs)

--8
luhn' :: [Int] -> Bool
luhn' xs = ((sum (altMap (luhnDouble) (id) (xs)) `mod` 10)) == 0

--Chapter 8.
data Tree a = Leaf a | Node (Tree a) a (Tree a) deriving (Show, Eq, Ord)

data Natural = Zero | Successor Natural deriving (Show, Eq)

nat2int :: Natural -> Int
nat2int Zero          = 0
nat2int (Successor n) = 1 + nat2int n

int2nat :: Int -> Natural
int2nat 0 = Zero
int2nat n = Successor (int2nat (n-1))

add' :: Natural -> Natural -> Natural
add' Zero n = n
add' (Successor m) n = Successor (add' m n)

--1
mul :: Natural -> Natural -> Natural 
mul Zero n = Zero
mul (Successor m) n = add' n (mul m n)

--2
occurs :: Ord a => a -> Tree a -> Bool
occurs x' (Leaf x)     = x' == x
occurs x' (Node l x r) = case compare x' x of EQ -> True
                                              LT -> occurs x' l
                                              GT -> occurs x' r

--3
data Tree' a = Leaf' a | Node' (Tree' a) (Tree' a) deriving Show

leaves :: Tree' a -> Int
leaves (Leaf' a) = 1
leaves (Node' l r) = leaves l + leaves r

balanced :: Tree' a -> Bool
balanced (Leaf' x)   = True
balanced (Node' l r) = abs (leaves l - leaves r) <= 1
                       && (balanced l) && (balanced r)

--4
balance :: [a] -> Tree' a
balance [x] = Leaf' x
balance xs  = Node' (balance l) (balance r)
  where (l, r) = halve xs

--5  
data E = V Int | A E E

ex' = A (V 2) (V 2)

folde :: (Int -> a) -> (a -> a -> a) -> E -> a
folde f g (V x)     = f x
folde f g (A e1 e2) = g (folde f g e1) (folde f g e2)

--6
eval' :: E -> Int
eval' = folde (id) (+)

size :: E -> Int
size = folde (\_ -> 1) (+)

--Chapter 9.
data Op = Add | Sub | Mul | Div | Exp

instance Show Op where
  show Add = "+"
  show Sub = "-"
  show Mul = "*"
  show Div = "/"
  show Exp = "^"

valid :: Op -> Int -> Int -> Bool
valid Add x y = x <= y
valid Sub x y = x >  y
valid Mul x y = x /= 1 && y /= 1 && x <= y
valid Div x y = y > 1 && x `mod` y == 0
valid Exp x y = x > 1 && y > 1

apply :: Op -> Int -> Int -> Int
apply Add = (+) 
apply Sub = (-) 
apply Mul = (*) 
apply Div = (div) 
apply Exp = (^) 

data Expr = Val Int | App Op Expr Expr

instance Show Expr where
  show (Val n)     = show n
  show (App o l r) = brak l ++ show o ++ brak r
    where brak (Val n) = show n
          brak e       = "(" ++ show e ++ ")"

values :: Expr -> [Int]
values (Val n)     = [n]
values (App o l r) = values l ++ values r

eval :: Expr -> [Int]
eval (Val n)     = [n | n > 0]
eval (App o l r) = [apply o x y | x <- eval l, y <- eval r, valid o x y]

subs :: [a] -> [[a]]
subs []     = [[]]
subs (x:xs) = yss ++ map (x:) yss
  where yss = subs xs

sew :: a -> [a] -> [[a]]
sew x []     = [[x]]
sew x (y:ys) = (x:y:ys):(map (y:) (sew x ys))

perms :: [a] -> [[a]]
perms []     = [[]]
perms (x:xs) = concat (map (sew x) (perms xs))

choices :: [a] -> [[a]]
choices = concat . map perms . subs

solution :: Expr -> [Int] -> Int -> Bool
solution e ns n = elem (values e) (choices ns) && eval e == [n]

split :: [a] -> [([a], [a])]
split []       = []
split [_]      = []
split (x : xs) = ([x], xs) : [(x : ls, rs) | (ls, rs) <- split xs]

exprs :: [Int] -> [Expr]
exprs []  = []
exprs [n] = [Val n]
exprs ns  = [e | (ls, rs) <- split ns, l <- exprs ls, r <- exprs rs, e <- combine l r]

combine :: Expr -> Expr -> [Expr]
combine l r = [App o l r | o <- ops]

ops :: [Op]
ops = [Add, Sub, Mul, Div, Exp]

solutions :: [Int] -> Int -> [Expr]
solutions ns n = [e | ns' <- choices ns, e <- exprs ns', eval e == [n]]

type Result = (Expr, Int)

results :: [Int] -> [Result]
results []  = []
results [n] = [(Val n, n) | n > 0]
results ns  = [res | (ls, rs) <- split ns,
                      lx      <- results ls,
                      ry      <- results rs,
                      res     <- combine' lx ry]

combine' :: Result -> Result -> [Result]
combine' (l, x) (r, y) = [(App o l r, apply o x y) | o <- ops, valid o x y]

solutions' :: [Int] -> Int -> [Expr]
solutions' ns n = [e | ns'   <- choices ns, (e, m) <- results ns', m == n]

nearSolution :: [Int] -> Int -> (Maybe Expr)
nearSolution ns n = if not (null answer)
                    then Just (head answer)
                    else Nothing
  where answer = solutions' ns n

simpleSolution :: Expr -> Int
simpleSolution (Val _) = 1
simpleSolution (App o x y) = simplicity o + simpleSolution x + simpleSolution y
  where simplicity Add = 2
        simplicity Sub = 2
        simplicity Mul = 4
        simplicity Div = 4
        simplicity Exp = 5

--simpleSolutions :: [Int] -> Int -> [Expr]
--simpleSolutions ns n = sortOn simpleSolution (solutions' ns n)

--Chapter 10.
-- "IO will not be on exam." -Darko

--Chapter 11.
-- Problems in this chapter involve Tic-Tac-Toe game from Homework 4, will just go over this again in Homework4.
t = Node' (Leaf' 0) (Node' (Leaf' 0) (Leaf' 0))

depth :: Tree' a -> Int
depth (Leaf' x) = 0
depth (Node' l r) = 1 + max (depth l) (depth r)

--Chapter 12.
-- "Monads will not be on exam." -Joe

--instance Functor Tree where
--    fmap g Leaf x      = Leaf (g x)
--    fmap g (Node l x r) = Node (fmap g l) (g x) (fmap g r)

--Chapter 15.
--1
fibonaccis :: [Integer]
fibonaccis = fibonacci 0 1
  where fibonacci a b = a:(fibonacci b (a+b))

--2
newtonsqrt :: Double -> Double
newtonsqrt n = head . filter (\x -> abs (x*x - n) < delta) $ iterate next approximation
  where next a = (a + n/a) / 2

approximation :: Double
approximation = 1.0

delta :: Double
delta = 0.00001

--Chapter 16.
--1
{-
add' :: Natural -> Natural -> Natural
add' Zero n = n
add' (Successor m) n = Successor (add' m n)

Proof by induction
Base case add Zero (Succ m) = Succ m = Succ (add Zero m)
Assume that add n (Succ m) = Succ (add n m)
Show that add (Succ n) (Succ m) = Succ (add (Succ n) m)
Begin add (Succ n) (Succ m) = 
       Succ (add n (Succ m)) = 
       Succ (Succ (add n m)) = 
       Succ (add (Succ n) m) [QED]
-}

--2
{-
Proof by induction
Base case add Zero m = m = add m Zero
Assume that add n m = add m n
Show that add (Succ n) m = add m (Succ n) 
Begin add (Succ n) m =
      Succ (add n m) = 
      Succ (add m n) = 
      add m (Succ n) [QED]
-}

--3
{-
Proof by induction
Base case (n=0) all (==x) (replicate 0 x) = all (==x) [] = True
Assume that all (==x) (replicate n x) == True
Show that all (==x) (replicate n+1 x) == True
Begin all (==x) (replicate n+1 x) =
      all (==x) (x:replicate n x) =
      x == x && all (==x) replicate n x = 
      True && all (==x) replicate n x = 
      True && True = 
      True [QED]
-}

--4
{-
[] ++ ys = ys
(x:xs) ++ ys = x:(xs ++ ys)

Proof by induction
Base case [] ++ [] = []
Assume that xs ++ [] = xs
Show that x:xs ++ [] = x:xs
Begin x:xs ++ [] = 
      x:(xs ++ []) = 
      x:xs [QED]

Proof by induction
Base case [] ++ (ys ++ zs) = ys ++ zs = ([] ++ ys) ++ zs
Assume that xs ++ (ys ++ zs) = (xs ++ ys) ++ zs
Show that x:xs ++ (ys ++ zs) = (x:xs ++ ys) ++ zs
Begin x:xs ++ (ys ++ zs) = 
      x:(xs ++ (ys ++ zs)) = 
      x:((xs ++ ys) ++ zs) = 
      (x:(xs ++ ys) ++ zs) = 
      (x:xs ++ ys) ++ zs [QED] 
-}

--5
{-
Proof by induction
Base cases (1) take 0 xs ++ drop 0 xs =
               [] ++ drop 0 xs = 
               [] ++ xs =
               xs
           (2) take n [] ++ drop n [] =
               [] ++ drop n [] = 
               [] ++ [] =
               []
           (3) take 0 [] ++ drop 0 [] =
               [] ++ drop 0 [] =
               [] ++ [] =
               []
Assume that take n xs ++ drop n xs = xs
Show that take (n+1) x:xs ++ drop (n+1) x:xs = x:xs
Begin take (n+1) x:xs ++ drop (n+1) x:xs = 
      x:(take n xs) ++ drop (n+1) x:xs = 
      x:(take n xs) ++ drop n xs = 
      x:(take n xs ++ drop n xs) = 
      x:xs [QED]
-}

--6
{-
data Tree = Leaf Int | Node Tree Tree

countLeaves :: Tree -> Int
countLeaves (Leaf x)   = 1
countLeaves (Node l r) = (countLeaves l) + (countLeaves r)

countNodes :: Tree -> Int
countNodes (Leaf x)   = 0
countNodes (Node l r) = 1 + (CountNodes l) + (countNodes r)

Proof by induction
Base case (Leaf x) => countLeaves -> 1, countNodes -> 0
Assume that countLeaves tree == countNodes tree + 1
Show that countLeaves (Node l r) == countNodes (Node l r) + 1
Begin countLeaves (Node l r) = 
      countLeaves l + countLeaves r = 
      (countNodes l + 1) + (countNodes r + 1) = 
      1 + (countNodes l + countNodes r) + 1 = 
      coundNodes (Node l r) + 1 [QED]
-}

{-
  Previous Exams.
-}
--Exam 1.
--1
{-
[1,2,3,4] :: [Int]
[[1,2,3,4]] :: [[Int]]
[1, [2,3], 4] :: Error
[[1,2], [3,4]] :: [[Int]]
[[1], [2,3], [4]] :: [[Int]]
[[1], [2], [3], [4]] :: [[Int]]
-}

--2
{-
(True, True:[]) :: (Bool, [Bool])
[[True]] :: [[Bool]]
\x -> \y -> (x && y, y || x) :: Bool -> Bool -> (Bool, Bool)
"a" ++ "b" :: String
tail :: [a] -> [a]
map tail ["a", "ab", "abc"] :: [[Char]]
-}

--3
{-
Prove that reverse (map f xs) == map f (reverse xs)
Proof by induction
Base Case: reverse (map f []) = reverse [] = map f (reverse [])
Assume reverse (map f xs) = map f (reverse xs)
Show that reverse (map f (x:xs)) = map f (reverse (x:xs))
reverse (map f (x:xs)) = reverse (f x : map f xs) = reverse (map f xs):(f x) = map f (reverse xs):(f x) = map f (reverse (f x):xs) = map f (reverse x:xs)
-}

--4
{-
\n -> n `div` 0 :: Int -> Int, function
(\n -> n `div` 0) 6 :: Int, exception: div by 0
(\n -> n `div` 2) 6 :: Int, 3
f (2, [1,2,3]) -- :: [Int], exception: non-exhaustive patterns (needs case for [])
where
  f (n, x:xs) = f(n-1, xs)
  f (0 xs) = xs
f 5 :: Int, diverges, n values = 5, 3, 1, -1, ...
where
  f 0 = 2
  f n = 2*f(n-2)
-}

--5
nondec :: Ord a => [a] -> Bool
nondec xs = checkPairs $ zip xs (tail xs)
  where checkPairs ((x1, x2):xs) = (x1 <= x2) && checkPairs xs
        checkPairs [] = True

nondec' :: Ord a => [a] -> Bool
nondec' xs = and [(x1 <= x2) | (x1, x2) <- zip xs (tail xs)]

--6
inits :: [a] -> [[a]]
inits [] = [[]]
inits (x:xs) = [] : map (x:) (inits xs)

--Exam 2.
--1
--take 3 :: [a] -> [a]

--2
--[(x, y) | x <- [1, 2], y <- [1, 2]] :: [(Int, Int)]
-- --|> [(1, 1), (1, 2), (2, 1), (2, 2)]  

--3
{-
polymorphic function - takes any type and produces same result
overloaded function - takes any type but produces different result
curried function - takes multiple arguments and evaluates in sequence
recursive function - references itself, output based on previous input
high-order function - takes a function and/or returns a function
-}

--4
product'' :: [Int] -> Int
product'' [] = 1
product'' (x:xs) = x * product'' xs

length'' :: [a] -> Int
length'' [] = 0
length'' (x:xs) = 1 + length'' xs

reverse'' :: [a] -> [a]
reverse'' [] = []
reverse'' (x:xs) = reverse'' xs ++ [x]

map''' :: (a -> b) -> [a] -> [b]
map''' f [] = []
map''' f (x:xs) = (f x) : map'' f xs

filter'' :: (a -> Bool) -> [a] -> [a]
filter'' p [] = []
filter'' p (x:xs) = if p x then x:(filter'' p xs) else filter'' p xs

foldr'' :: (a -> b -> b) -> b -> [a] -> b
foldr'' f b [] = b
foldr'' f b (a:as) = (f a) (foldr'' f b as)

--5
insert' :: Int -> [Int] -> [Int]
insert' a [] = [a]
insert' a (x:xs)
  | (a <= x) = a:x:xs
  | otherwise = x:(insert' a xs)

--insert 3 [1, 2, 4, 5] -> (3 < 1)? 1:(insert 3 [2, 4, 5]) -> (3 < 2)? 1:2:(insert 3 [4, 5]) -> (3 < 4)? 1:2:(3:4:5:[]) done

isort :: [Int] -> [Int]
isort [] = []
isort (x:xs) = insert' x (isort xs)

--6
type Factorized = [(Int, Int)]

factorize :: Int -> Factorized
factorize n = factorfilter $ [(a, b) | a <- primes'' n, b <- [1..n], (a^b) /= 0, n `mod` (a^b) == 0]

factorfilter :: Factorized -> Factorized
factorfilter [] = []
factorfilter [(x1, x2)] = [(x1, x2)]
factorfilter ((x1, x2):(x3, x4):xs) = if (x1 == x3) then factorfilter ([(x3, x4)] ++ xs)
                                      else (x1, x2):(factorfilter ([(x3, x4)] ++ xs))

primes'' :: Int -> [Int]
primes'' n = [x | x <- [2..n], factors'' x == [x]]

factors'' :: Int -> [Int]
factors'' n = [x | x <- [2..n], n `mod` x == 0]

--7
data Tree1 = Leaf1 Int | Node1 Tree1 Tree1 deriving Show
--Leaf 0
--Node (Leaf 0) (Leaf 0)
--Node (Leaf 0) (Node (Leaf 0) (Leaf 0)) 
--Node (Node (Leaf 0) (Leaf 0)) (Leaf 0)
--Node1 (Node1 (Leaf1 0) (Leaf1 0)) (Node1 (Leaf1 0) (Leaf1 0))

size1 :: Tree1 -> Int
size1 (Leaf1 x) = 1
size1 (Node1 l r) = size1 l + size1 r

depth1 :: Tree1 -> Int
depth1 (Leaf1 x) = 0
depth1 (Node1 l r) = 1 + max (depth1 l) (depth1 r)

full1 :: Tree1 -> Bool
full1 (Leaf1 x) = True
full1 (Node1 l r) = (size1 l - size1 r == 0)

grow1 :: Int -> Tree1
grow1 0 = Leaf1 0
grow1 n = Node1 (grow1 (n-1)) (grow1 (n-1))

--Exam 3.
--1
{-
last :: [a] -> a
last = head $ reverse

two :: a -> (a, a)
two x = (x, x)

append :: [a] -> [a] -> [a]
append xs ys = xs ++ ys

square :: Num a => a -> a
square n = n * n

id :: a -> a
id = \x -> x
-}

--2
genprimes :: Int -> [Int]
genprimes n = [x | x <- [2..n], factors x == [x]]
  where factors n = [x | x <- [2..n], n `mod` x == 0]

--3
{-
\n -> n `div` 0 :: Num a => a -> a, function
\n -> n `div` 0 5 :: Num a => a, fails - div by 0
j j :: [[[j :: (a -> a) => j i :: a]]] a
  where 
    j x = j x
f (2, [1,2,3]) :: [Int], fails - non-exhaustive patterns
  where f (n, x:xs) = f(n-1, xs)
        f (0, xs) = xs
g 5 :: Diverges, n values = 5, 3, 1, -1,...
  where g 0 = 1
        g n = 2*g(n-2)
d a :: Num a => a, evaluates to 32
data T a = A1 [T a]
a = A1 [A1 [A1 [A1 [], A1 [A1 [A1 []]]], A1 [A1 [], A1 []]]]
m = foldr max 0
d (A1 []) = 1
d (A1 c)  = 2*m (map d c)  
-}

--4
merge' :: [Int] -> [Int] -> [Int]
merge' [] [] = []
merge' [] ys = ys
merge' xs [] = xs
merge' (x:xs) (y:ys)
  | (x < y) = x:(merge' xs (y:ys))
  | otherwise = y:(merge' (x:xs) ys)

msort' :: [Int] -> [Int]
msort' [] = []
msort' [x] = [x]
msort' xs = merge' (msort' l) (msort' r)
  where (l, r) = splitAt (length xs `div` 2) xs

--5
data Tree2 a = Leaf2 a | Fork2 [Tree2 a]

t2 = Fork2 [Fork2 [Leaf2 1, Fork2 [Leaf2 2, Leaf2 3], Fork2 []], Fork2 [Leaf2 4]]

dfs :: Tree2 a -> [a]
dfs (Leaf2 x) = [x]
dfs (Fork2 []) = []
dfs (Fork2 (x:xs)) = dfs x ++ concatMap dfs xs

--6
{-
(++) is said to be associative if the property:
  (xs ++ ys) ++ zs = xs ++ (ys ++ zs)
holds for any lists xs, ys, and zs

Proof by Induction
Base Case
  ([] ++ ys) ++ zs = ys ++ zs = [] ++ (ys ++ zs)
Inductive Hypothesis
  (xs ++ ys) ++ zs = xs ++ (ys ++ zs)
Inductive Step
  ((x:xs) ++ ys) ++ zs = (x:xs) ++ (ys ++ zs)
Begin
  ((x:xs) ++ ys) ++ zs = (x:(xs ++ ys)) ++ zs = 
  x:((xs ++ ys) ++ zs) = x:(xs ++ (ys ++ zs)) = 
  (x:xs) ++ (ys ++ zs) [QED]
-}

data Tree3 a = Leaf3 Int | Fork3 [Tree3 a]

bfs :: Tree3 a -> [a]
bfs t = concat $ (traverse (descend t))
  where descend (Leaf3 x) = [[Leaf3 x]]
        descend (Fork3 []) = [[]]
        descend (Fork3 (x:xs)) = [[x]] ++ descend xs 
        traverse [[]] = [[]]
        traverse (xs:xss) = levels xs ++ levels (traverse xss)
        levels (Leaf3 x) = [[x]]
        levels (Fork3 []) = [[]]
        levels (Fork3 (x:xs)) = levels x ++ concatMap levels xs



