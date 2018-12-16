Jacob Hurst
Jhurst

5.1 Trees
I designed this solution by first processing the tree level-by-level, viewing each level as a list,
moving through the list of levels & marking each E as 0 & each T a (Tree a) (Tree a) as the current counter (which starts at 1), 
this deconstructs the tree into a list of list of ints, so, the last step is just to rebuild the tree from that list of list of ints, 
this is done by forming and connecting subtrees.

In an imperative setting I might've used some sort of queue-based implementation which utilized a stateful counter.
This approach seems equally efficient, however, a drawback might be the fact that I deconstruct and rebuild the tree (which could be bad for large trees).

\begin{code}

import Data.List
import Debug.Trace

--5.1 Trees
data Tree a = E
            | T a (Tree a) (Tree a)
            deriving (Eq, Show)

t :: Tree Char
t = T 'a' (T 'b' (E) (T 'c' (E) (E))) (T 'd' (E) (E))

-- Convert tree to list of lists, index, then rebuild.
bfnum :: Tree a -> Tree Int
bfnum t = rebuild $ levels t

-- Ignore E.
roots :: [Tree a] -> Int
roots []       = 0
roots (E : ts) = roots ts
roots (_ : ts) = 1 + roots ts

-- Descend a level into the subtrees.
descend :: [Tree a] -> [Tree a]
descend [] = []
descend (T _ l r : ts) = l : r : descend ts
descend (E : ts) = descend ts

-- Levels tree horizontally into a list of levels, replacing values with counts.
levels :: Tree a -> [[Int]]
levels t = traverse 1 [t]
  where
    traverse :: Int -> [Tree a] -> [[Int]]
    traverse _ [] = []
    traverse count ts = level count ts : traverse (count + roots ts) (descend ts)
 
-- Number the nodes breadth first, setting E as 0, and T as the count.
level :: Int -> [Tree a] -> [Int]
level _ [] = []
level count (E : ts) = 0 : level count ts
level count (T a l r : ts) = count : level (count + 1) ts

-- Rebuild a tree from a list of levels.
rebuild :: [[Int]] -> Tree Int
rebuild ts = head (traverse ts)
  where
    traverse :: [[Int]] -> [Tree Int]
    traverse [] = []
    traverse (level : levels) = connect level (traverse levels)

-- Connect a level to subtrees beneath it.
connect :: [Int] -> [Tree Int] -> [Tree Int]
connect [] [] = []
connect (0 : is) ts = E : connect is ts
connect (n : is) (tl : tr : ts) = T n tl tr : connect is ts

--5.2 Expression Trees
type Identifier = String

data Expr = Num Integer
          | Var Identifier
          | Let {var :: Identifier, value :: Expr, body :: Expr}
          | Add Expr Expr
          | Sub Expr Expr
          | Mul Expr Expr
          | Div Expr Expr
          deriving (Eq)

instance Show Expr where
  show (Num x)     = show x
  show (Var v)     = read $ show v
  show (Let v x e) = "let " ++ (read $ show v) ++ " = " ++ (show x) ++ " in: " ++ (show e) ++ ";"
  show (Add e1 e2) = "(" ++ (show e1) ++ " + " ++ (show e2) ++ ")"
  show (Sub e1 e2) = "(" ++ (show e1) ++ " - " ++ (show e2) ++ ")"
  show (Mul e1 e2) = "(" ++ (show e1) ++ " * " ++ (show e2) ++ ")"
  show (Div e1 e2) = "(" ++ (show e1) ++ " / " ++ (show e2) ++ ")"

type Env = Identifier -> Integer

e :: Expr
e = Let "x" (Num 3) (Add (Var "x") (Num 5))

e' :: Expr
e' = Let "x" (Num 3) (Add (Var "x") (Let "y" (Num 5) (Add (Var "y") (Num 0))))

e'' :: Expr
e'' = Let "x" (Num 3) (Add (Var "x") (Var "y"))

e''' :: Expr
e''' = Let "x" (Var "x") (Add (Var "x") (Num 5))

e'''' :: Expr
e'''' = Let "x" (Var "x") (Add (Var "x") ((Let "y" (Num 5) (Var "y"))))

emptyEnv :: Env
emptyEnv = \s -> error ("unbound: " ++ s)

extendEnv :: Env -> Identifier -> Integer -> Env
extendEnv oldEnv s n s' = if s' == s then n else oldEnv s'

evalInEnv :: Env -> Expr -> Integer
evalInEnv env exp = 
  case exp of
    Num x -> x
    Var v -> error ("unbound variable " ++ v)
    Let v (Num x) exp -> evalInEnv env (lookup v x exp)
    Add exp1 exp2 -> evalInEnv env exp1 + evalInEnv env exp2
    Sub exp1 exp2 -> evalInEnv env exp1 - evalInEnv env exp2
    Mul exp1 exp2 -> evalInEnv env exp1 * evalInEnv env exp2
    Div exp1 exp2 -> evalInEnv env exp1 `div` evalInEnv env exp2
    where lookup v x exp' = case exp' of
            Num x' -> Num x'
            Var v' -> if v == v' then Num x else Var v'
            Let v' (Num x') exp' -> lookup v' x' exp' 
            Add exp1' exp2' -> Add (lookup v x exp1') (lookup v x exp2')
            Sub exp1' exp2' -> Sub (lookup v x exp1') (lookup v x exp2')
            Mul exp1' exp2' -> Mul (lookup v x exp1') (lookup v x exp2')
            Div exp1' exp2' -> Div (lookup v x exp1') (lookup v x exp2')
    
eval :: Expr -> Integer
eval exp = evalInEnv emptyEnv exp

--5.3 Infinite Lists
diag :: [[a]] -> [a]
diag = concat . (map reverse . (tail . traverse [])) where
  traverse ns ds' = [heads | heads : _ <- ns] : tails' ds'
    where tails' []     = tails
          tails' (d:ds) = traverse (d : tails) ds 
          tails = [tail' | _ : tail' <- ns]

-- The standard table of all positive rationals, in three forms:
-- (1) as floats
rlist = [[i/j | i <- [1..]] | j <- [1..]]
-- (2) as strings
qlist1 = [[show i ++ "/" ++ show j | i <- [1..]] | j <- [1..]]
-- (3) as strings, in reduced form
qlist2 = [[fracString i j | i <- [1..]] | j <- [1..]]

-- Take a numerator and denominator, reduce, and return as string
fracString num den 
  | denominator == 1 = show numerator
  | otherwise = show numerator ++ "/" ++ show denominator
  where c = gcd num den
        numerator = num `div` c
        denominator = den `div` c

-- Take an n-by-n block from the top of a big list of lists
block n x = map (take n) (take n x)

\end{code}