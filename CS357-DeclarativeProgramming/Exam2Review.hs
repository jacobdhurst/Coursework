{-
In Lab:
drop       :: Int -> [a] -> [a]
drop 5     ::        [a] -> [a]

take       :: Int -> [a] -> [a]
take 3     :: Int -> [a] -> [a]
take 3 [1] :: [Int]

Int -> Int      :: Standard function - type constraint
a -> a          :: Polymorphic function - no constraint
Num a => a -> a :: Overloaded function (subset of polymorphic, ad-hoc polymorphic) - type class constraint
a -> a          :: !Curried function
a -> a -> a     :: Curried function

map :: (a -> b) -> [a] - [b]
map :: (a -> b) -> [] a -> [] b

map (take 3) :: [[a]] -> [[a]]

[ x + y  | x <- [1, 2], y <- [3, 4]] --> [4, 5, 5, 6]
[ even x | x < [1..10]]              --> [False, True, False,...]
[ odd x  | x <- [1..10], even x]     --> [False, False,...]
 
                                         (2 : (3 : (4 : []))
foldr (-) 1 [2, 3, 4]                --> (2 - (3 - (4 - 1)))
foldl (-) 1 [2, 3, 4]                --> (((1 - 2) - 3) - 4)

last :: [a] -> a
last [x] = x
last (x:xs) = last xs                --> or head . reverse $ xs

fourth [a] -> a
fourth xs = head . drop 3 $ xs

takeWhile :: (a -> Bool) -> [a] -> [a]
takeWhile p [] = []
takeWhile p (x:xs)
 | p x = x : takeWhile p xs
 | otherwise = []
-}
concatMap' :: (a -> [b]) -> [a] -> [b]
concatMap' f [] = []
concatMap' f (x:xs) = f x ++ concatMap' f xs

drop' :: Int -> [a] -> [a]
drop' 0 (x:xs) = (x:xs)
drop' n [] = []
drop' n (x:xs) = drop' (n-1) xs

take' :: Int -> [a] -> [a]
take' 0 _ = []
take' n [] = []
take' n (x:xs) = x : take' (n-1) xs

sum' :: [Int] -> Int
sum' [] = 0
sum' (x:xs) = x + sum' xs 
-- = foldr (+) 0 xs

data Color = Red | Green | Blue
data UndefinedTree = UndefinedLeaf | UndefinedNode | UndefinedInt
data DefinedTree = DefinedLeaf Int | DefinedNode DefinedTree DefinedTree
data MultipleTypes = Str String | Number Int | Boolean Bool
data T = A Char | B [T]

f (A c) = [c]
f (B ts) = concatMap' f ts

example = f (B [B [A 'r', A 'o'], B [A 's'], B [A 'e']])
--(['r'] ++ ['0'] ++ []) ++ (['s'] ++ []) ++ (['e'] ++ []) ++ [] --> "rose"

{- Chapter 07 -}
problem1 :: (a -> b) -> (a -> Bool) -> [a] -> [b]
problem1 f p = map f . filter p

all' :: (a -> Bool) -> [a] -> Bool
all' p [] = True
all' p (x:xs) = (p x) && all' p xs

any' :: (a -> Bool) -> [a] -> Bool
any' p [] = False
any' p (x:xs) = (p x) || any' p xs

takeWhile' :: (a -> Bool) -> [a] -> [a]
takeWhile' _ [] = []
takeWhile' p (x:xs)
 | (p x) = x : takeWhile' p xs
 | otherwise = []

dropWhile' :: (a -> Bool) -> [a] -> [a]
dropWhile' _ [] = []
dropWhile' p (x:xs)
 | (p x) = dropWhile' p xs
 | otherwise = x:xs

map' :: (a -> b) -> [a] -> [b]
map' f = foldr (\x xs -> f x : xs) []

filter' :: (a -> Bool) -> [a] -> [a]
filter' p = foldr (\x xs -> if (p x) then x:xs else xs) []

digitsToInt :: [Int] -> Int
digitsToInt = foldl (\x xs -> x*10 + xs) 0

curry' :: ((a, b) -> c) -> a -> b -> c
curry' f x y = f (x, y)

uncurry' :: (a -> b -> c) -> (a, b) -> c
uncurry' f (x, y) = f x y

unfold p h t x | p x       = []
               | otherwise = h x : unfold p h t (t x)

int2bin = reverse . unfold (==0) (`mod` 2) (`div` 2)

map'' f = unfold (null) (f . head) tail

altMap :: (a -> b) -> (a -> b) -> [a] -> [b]
altMap f g = alt True
 where alt p [] = []
       alt p (x:xs) = (if p then f else g) x : alt (not p) xs

{- Chapter 08 -}
data Natural = Zero | Successive Natural deriving (Eq, Ord, Show, Read)

int2Natural :: Int -> Natural
int2Natural 0 = Zero
int2Natural n = Successive (int2Natural (n-1))

natural2Int :: Natural -> Int
natural2Int Zero = 0
natural2Int (Successive n) = 1 + natural2Int n

add' :: Natural -> Natural -> Natural
add' Zero n = n
add' (Successive m) n = Successive (add' m n)

mult' :: Natural -> Natural -> Natural
mult' Zero n = Zero
mult' (Successive m) n = add' n (mult' m n)

data Tree1 a = Leaf1 a | Node1 (Tree1 a) a (Tree1 a) deriving (Eq, Ord, Show, Read)

occurs :: Ord a => a -> Tree1 a -> Bool
occurs x (Leaf1 y) = x == y
occurs x (Node1 l y r)
 | x == y = True
 | x < y = occurs x l
 | otherwise = occurs x r

data Tree2 a = Leaf2 a | Node2 (Tree2 a) (Tree2 a) deriving (Eq, Ord, Show, Read)

count :: Tree2 a -> Int
count (Leaf2 a) = 1
count (Node2 l r) = count l + count r
 
balanced :: Tree2 a -> Bool
balanced (Leaf2 a) = True
balanced (Node2 l r) = (abs (count l - count r) <= 1) && balanced l && balanced r
 
balance :: [a] -> Tree2 a
balance [x] = Leaf2 x
balance xs = Node2 (balance l) (balance r)
 where (l, r) = halves xs
 
halves :: [a] -> ([a], [a])
halves xs
 | length xs `mod` 2 == 0 = (take (length xs `div` 2) xs, reverse $ take (length xs `div` 2) $ reverse xs)
 | otherwise = (take (((length xs)+1)`div`2) xs, reverse $ take (((length xs)-1)`div`2) $ reverse xs)
 
data Expr = Val Int | Add Expr Expr deriving (Eq, Ord, Show, Read)

folde :: (Int -> a) -> (a -> a -> a) -> Expr -> a
folde f g (Val x) = f x
folde f g (Add y z) = g (folde f g y) (folde f g z)

eval :: Expr -> Int
eval = folde (id) (+)

size' :: Expr -> Int
size' = folde (\_ -> 1) (+)

{- Exam 1 review -}
nondec :: Ord a => [a] -> Bool
nondec xs = checkPairs (zip xs (tail xs))
 where checkPairs [] = True
       checkPairs ((i,j):xs)
        | (i <= j) = True && checkPairs xs
        | otherwise = False 

nondec' xs = and [ i <= j | (i, j) <- (zip xs (tail xs))]

{- Trees with folds, binary heap trees, rose trees -}
foldr' :: (a -> b -> b) -> b -> [a] -> b
foldr' f v [] = v
foldr' f v (x:xs) = f x (foldr f v xs)

foldt' :: (b -> b -> b) -> (a -> b) -> Tree2 a -> b
foldt' f g (Leaf2 a) = g a
foldt' f g (Node2 l r) = f (foldt' f g l) (foldt' f g r)

{- Homework 3: 3.1-4 -}
--3.1 Lists & Trees
{-data Tree3 a = Leaf3 a | Node3 (Tree3 a) (Tree3 a) deriving (Eq, Ord, Show, Read)

count1 :: Tree3 a -> Int
count1 (Leaf3 a) = 1
count1 (Node3 l r) = count1 l + count1 r

balanced1 :: Tree3 a -> Bool
balanced1 (Leaf3 a) = True
balanced1 (Node3 l r) = (abs (count l - count r) <= 1) && balanced1 l && balanced1 r

balance1 :: [a] -> Tree3 a
balance1 [x] = Leaf3 x
balance1 xs = Node3 (balance1 l) (balance1 r)
 where (l, r) = halves xs

halves1 :: [a] -> ([a], [a])
halves1 xs
 | length xs `mod` 2 == 0 = (take (length xs `div` 2) xs, reverse $ take (length xs `div` 2) $ reverse xs)
 | otherwise = (take (((length xs)+1)`div`2) xs, reverse $ take (((length xs)-1)`div`2) $ reverse xs)-}
 
 --3.2 Goldbach's Conjecture - List Comprehension
--any even number > 2 can be written as the sum of two prime numbers
--when given an even number n, returns a list
--of all pairs of primes which sum to n
goldbach :: Int -> [(Int, Int)]
goldbach n 
 | (even n && n > 2) = [(a, b) | a <- primes n, b <- primes n, (a+b) == n]
 | otherwise = error "Given n was invalid."

--function to test if number is prime
--implemented using list comprehension
isPrime :: Int -> Bool
isPrime n
 | (n == 2) = True
 | (even n) = False
 | (n > 2) = (length [x | x <- [2..(ceiling $ sqrt $ fromIntegral n)], (n `mod` x) == 0]) == 0
 | otherwise = False 

--https://oeis.org/A000040 list of primes
--function to generate list of primes up to n, inf if n == 0
primes :: Int -> [Int]
primes n = [ x | x <- [2..n], isPrime x ]

--3.3 Higher-order functions
church :: Int -> (c -> c) -> c -> c
church n = foldr (.) id . replicate n

--3.4 Powersets
--[Int] representing sets of integers
powerset :: [Int] -> [[Int]]
powerset [] = [[]]
powerset (e:s) = map (e:) (powerset s) ++ powerset s