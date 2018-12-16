import Data.List
import Debug.Trace
import System.IO
import Control.Applicative
import Data.Char

{-
In Class/Lab:

Know how to check for primality
Know how to work with custom datatypes
Know how partial applications work - ex: what is type of take 3?
Know how to write out inductive proofs of haskell

In class review:
head     :: [a] -> a
tail     :: [a] -> [a]
map      :: (a -> b) -> [a] -> [b]
(+)      :: Num a => a -> a -> a
zip      :: [a] -> [b] -> [(a, b)]
map tail :: [[a]] -> [[a]]
const = \a b -> a,        const  :: a -> b -> a
triple x y z = (z, y, x), triple :: a -> b -> c -> (c, b, a)
hello = map ("Hello "++), hello  :: [String] -> [String]
fsts = map fst, fsts    :: [(a, b)] -> [a]
(3 == 4) == (5 == 6)    :: Bool
(\x -> show(x ++ "\n")) :: String -> String
[reverse, tail, take 3] :: [([a] -> [a])]
[reverse, filter even]  :: Num a => [([a] -> [a])]
(\x -> x + 1)           :: Num a => a -> a,
(\x -> x + 1) 1         :: Num a => a, ans=2
(\x -> x `div` 0)       :: Num a => a -> a
(\x -> x `div` 0) 1     :: Num a => a, Fails - Divide by zero
f 5                     :: Num a => a, ans = 2^5 = 32
  where f 0 = 1
        f n = 2*f (n-1)
f 5                     :: Num a => a, Diverges
  where f n = 2*f (n-1)
        f 0 = 1

-}
data Tree a = Leaf a | Node (Tree a) (Tree a) deriving (Eq, Show)

listTree :: Tree a -> [a]
listTree (Leaf x)     = [x]
listTree (Node l r) = (listTree l) ++ (listTree r)

leftLeanTree :: [a] -> Tree a
leftLeanTree [x]    = Leaf x
leftLeanTree (x:xs) = Node (leftLeanTree xs) (Leaf x)

rightLeanTree :: [a] -> Tree a
rightLeanTree [x]    = Leaf x
rightLeanTree (x:xs) = Node (Leaf x) (rightLeanTree xs)

mapTree :: (a -> b) -> Tree a -> Tree b
mapTree f (Leaf x)   = Leaf (f x)
mapTree f (Node l r) = Node (mapTree f l) (mapTree f r)

{-
Exams 1:
-}
inits' :: [a] -> [[a]]
inits' [] = []
inits' xs = init xs : inits' (init xs)
--inits' = foldr (\x y -> [] : (map (x:) y)) [[]]

{-
Exams 2:
-}
insert' :: Int -> [Int] -> [Int]
insert' i []     = [i]
insert' i (x:xs) = if (i < x) then i:x:xs else x:insert' i xs

isort :: [Int] -> [Int]
isort []     = []
isort (x:xs) = insert x (isort xs)

isPrime :: Int -> Bool
isPrime n
  | n == 2    = True
  | (even n)  = False
  | n > 2     = (length [x | x <- [2..(ceiling $ sqrt $ fromIntegral n)], n `mod` x == 0]) == 0
  | otherwise = False

primes :: Int -> [Int]
primes n = [x | x <- [2..n], isPrime x]

data Tree' = Leaf' Int | Node' Tree' Tree' deriving (Eq, Show, Ord)

size' :: Tree' -> Int
size' (Leaf' x)     = 1
size' (Node' l r) = (size' l) + (size' r)

depth' :: Tree' -> Int
depth' (Leaf' x)   = 0
depth' (Node' l r) = 1 + max (depth' l) (depth' r)

full :: Tree' -> Bool
full (Leaf' x)   = True
full (Node' l r) = ((size' l) - (size' r)) == 0

grow :: Int -> Tree'
grow 0 = Leaf' 0
grow n = Node' (grow (n-1)) (grow (n-1))

{-
Textbook:
-}
--Chapter 9
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

simpleSolutions :: [Int] -> Int -> [Expr]
simpleSolutions ns n = sortOn simpleSolution (solutions' ns n)

--E1
choices' :: [a] -> [[a]]
choices' xs = [x | xss <- subs xs, x <- perms xss]

--E2
isChoice :: Eq a => [a] -> [a] -> Bool
isChoice [] _      = True
isChoice _ []      = False
isChoice (x:xs) ys = if not (elem x ys) then False
                     else isChoice xs (remove x ys)
  where remove _ []     = []
        remove x (y:ys) = if x == y then ys 
                          else y:(remove x ys)

--E3
--It would lead to non-termination, exprs would not necessarily reduce list

--Chapter 10
adder :: IO ()
adder = do putStr "How many numbers?"
           n <- getInt
           numbers <- sequence [getInt | _ <- [0..n-1]]
           putStr $ "Total: " ++ show (sum numbers) ++ "\n"

getInt :: IO Int
getInt = do n <- getLine
            return ((read n) :: Int)

getCh :: IO Char
getCh = do hSetEcho stdin False
           x <- getChar
           hSetEcho stdin True
           return x

readLine :: IO String
readLine = readLine' ""

readLine' :: [Char] -> IO [Char]
readLine' xs = do x <- getCh
                  case x of '\n' -> return xs
                            '\DEL' -> if null xs
                                      then readLine' ""
                                      else do putStr "\b \b"
                                              readLine' (init xs)
                            _ -> do putChar x
                                    readLine' (xs ++ [x])

--Chapter 12
inc :: Functor f => f Int -> f Int
inc = fmap (+1)

data Tree'' a = Leaf'' | Node'' (Tree'' a) a (Tree'' a)

instance Functor Tree'' where
  fmap _ Leaf''         = Leaf''
  fmap g (Node'' l x r) = Node'' (fmap g l) (g x) (fmap g r) 

newtype Curry a b = C (a -> b)

instance Functor (Curry a) where
  fmap g (C h) = C (g . h)

--Chapter 13
newtype Parser a = P (String -> [(a, String)])

parse :: Parser a -> String -> [(a, String)]
parse (P p) input = p input

item :: Parser Char
item = P (\input -> case input of
                    [] -> []
                    (x:xs) -> [(x, xs)])

sat :: (Char -> Bool) -> Parser Char
sat p = do x <- item
           if p x
               then return x
               else empty

char :: Char -> Parser Char
char x = sat (== x)

string :: String -> Parser String
string []       = return []
string (x : xs) = do _ <- char x
                     _ <- string xs
                     return (x : xs)

comment :: Parser ()
comment = do _ <- string "--"
             _ <- many (sat (/= '\n'))
             _ <- char '\n'
             return ()

instance Functor Parser where
  fmap g p = P (\input -> case parse p input of
             [] -> []
             [(v, out)] -> [(g v, out)])

instance Applicative Parser where
  pure v    = P (\input -> [(v, input)])
  pg <*> px = P (\input -> case parse pg input of
              [] -> []
              [(g, out)] -> parse (fmap g px) out) 

instance Monad Parser where
  p >>= f = P (\input -> case parse p input of
            [] -> []
            [(v, out)] -> parse (f v) out)

instance Alternative Parser where
    empty   = P (\_ -> [])
    p <|> q = P (\inp -> case parse p inp of
                              []         -> parse q inp
                              [(v, out)] -> [(v, out)]
                              _          -> undefined)
    many x = some x <|> pure []
    some x = pure (:) <*> x <*> many x

--Chapter 16 (Proofs)
{-
Proofs:

Prove by induction that length (replicate n x) == b where n >= 0
Inductive Hypothesis: length (replicate n x) = n
Base Case: 
length (replicate 0 x)     *apply replicate
length []                  *apply length
0                          *satisfied
Inductive Step:
length (replicate (n+1) x) *apply replicate
length (x:replicate n x)   *apply length
1 + length (replicate n x) *Inductive Hypothesis
1 + n = n + 1              *satisfied

Prove by induction that map id == id
Inductive Hypothesis: map id xs = id xs
Base Case: 
map id []        *apply map
[]               *satisfied
Inductive Step:
map id (x:xs)    *apply map
id x : map id xs *apply id
x : map id xs    *Inductive Hypothesis
x : xs           *satisfied

Prove that reverse [x] == [x]
reverse [x] = [x]
reverse [x]               *list notation
reverse x:[]              *apply reverse
reverse [] ++ reverse [x] *apply reverse
[] ++ [x]                 *apply concat
[x]                       *satisfied

Prove by induction that reverse distributes over (++)
Inductive Hypothesis: reverse (xs ++ ys) = reverse ys ++ reverse xs
Induction on xs:
Base Case:
reverse ([] ++ ys)                *apply ++
reverse ys                        *apply identity for ++
reverse ys ++ []                  *apply reverse 
reverse ys ++ reverse []          *satisfied
Inductive Step:
reverse ((x:xs) ++ ys)            *apply ++
reverse (x:(xs ++ ys))            *apply reverse
reverse (xs ++ ys) ++ [x]         *Inductive Hypothesis
(reverse ys ++ reverse xs) ++ [x] *associativity of ++
reverse ys ++ (reverse xs ++ [x]) *unapply reverse
reverse ys ++ reverse (x:xs)      *satisfied

Prove by induction that reverse (reverse xs) = xs
Inductive Hypothesis: reverse (reverse xs) = xs
Base Case:
reverse (reverse [])                *apply reverse
reverse []                          *apply reverse
[]                                  *satisfied
Inductive Step:
reverse (reverse (x:xs)) = (x:xs)
reverse (reverse (x:xs))            *apply inner reverse
reverse (reverse xs ++ [x])         *distributivity
reverse [x] ++ reverse (reverse xs) *Inductive Hypothesis
[x] ++ xs                           *apply ++
x:xs                                *satisfied

Consider:
data Nat = Zero | Succ Nat 
add :: Nat -> Nat -> Nat
add Zero m     = m
add (Succ n) m = Succ (add n m)
Prove by induction that add is associative *(x+y)+z == x+(y+z)
Inductive Hypothesis: add x (add y z) == add (add x y) z
Base Case:
add Zero (add y z)     *apply outer add
add y z                *unapply add
add (add Zero y) z     *satisfied
Inductive Step:
add (Succ x) (add y z) *apply outer add
Succ (add x (add y z)) *Inductive Hypothesis
Succ (add (add x y) z) *unapply add
add (Succ (add x y) z) *unapply add
add (add (Succ x) y) z *satisfied

Prove by induction that map id xs = xs
Inductive Hypothesis: map id xs = xs
Base Case: map id [] = []
Inductive Step: map id (x:xs) = id x : map id xs = x : map id xs = x : xs

Prove by induction that map (f . g) xs = map f (map g xs)
Inductive Hypothesis: map (f . g) xs = map f (map g xs)
Base Case: map (f . g) [] = []
Inductive Step: 
map (f . g) (x:xs) 
= (f . g) x : map (f . g) xs 
= (f . g) x : map f (map g xs)
= f (g x) : map f (map g xs) 
= map f ((g x) : map g xs) 
= map f (map g (x:xs))
-}

{-
Homework 3:
-}
church :: Int -> (a -> a) -> a -> a 
church n = foldr (.) id . replicate n

depth :: Tree a -> Int
depth (Leaf x)   = 1
depth (Node l r) = 1 + max (depth l) (depth r) 

{-
Homework 4:
-}
distribute' :: Int -> [a] -> [[a]]
distribute' n = takeWhile (not . null) . map (take n) . iterate (drop n)

bases = "AGCT"

insertions :: String -> [String]
insertions xs = concatMap (insertions' xs) bases

insertions' :: String -> Char -> [String]
insertions' [x] b    = [([b] ++ [x]), ([x] ++ [b])]
insertions' (x:xs) b = [b:x:xs] ++ (map (x:) (insertions' xs b)) 

{-
When do we say a binary operator is commutative?
if x + y = y + x, or x * y = y * x, etc.

When do we say a binary operator is associative?
if (x + y) + z = x + (y + z)

If given a list, how would you distribute its elements evenly into a number of lists?
-}
distribute :: Int -> [a] -> [[a]]
distribute n = takeWhile (not . null) . map (take n) . iterate (drop n)