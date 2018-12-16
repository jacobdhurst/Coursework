{-
Jacob Hurst
Jhurst
-}

module Homework4 where

  --No other imports allowed
  import qualified Data.List as L
 
  --4.1 Genome Lists (40pts)
  bases = "AGCT"

  insertions :: String -> [String]
  insertions xs = concatMap (insertions' xs) bases

  insertions' :: String -> Char -> [String]
  insertions' [x] base = [([base] ++ [x]), ([x] ++ [base])]
  insertions' (x:xs) base = [base:x:xs] ++ (map (x:) (insertions' xs base))
  
  deletions :: String -> [String]
  deletions xs = L.delete xs (deletions' xs)

  deletions' :: String -> [String]
  deletions' [] = [[]]
  deletions' (x:xs) = [xs] ++ (map (x:) (deletions xs))
  
  substitutions :: String -> [String]
  substitutions xs = concatMap (substitutions' xs) bases
  
  substitutions' :: String -> Char -> [String]
  substitutions' [x] base = [[base]]
  substitutions' (x:xs) base = [base:xs] ++ (map (x:) (substitutions' xs base))

  transpositions :: String -> [String]
  transpositions [x] = [[x]]
  transpositions (x1:x2:[]) = [[x2] ++ [x1]]
  transpositions (x1:x2:xs) = [x2:x1:xs] ++ (map (x1:) (transpositions (x2:xs)))
  
  --4.2 Sorting (20pts)
  insert :: Ord a => a -> [a] -> [a]
  insert = L.insert
  
  isort :: Ord a => [a] -> [a]
  isort [] = []
  isort (x:xs) = insert x (isort xs)
 
  fileisort :: String -> String -> IO ()
  fileisort fn1 fn2 = 
    do i <- readFile fn1
       let o = init $ unlines $ isort (lines i)
       writeFile fn2 o
       
  --4.3 Game Trees (40pts)
  {-Modified code from textbook (Chapter 11). 
    Strategy is to take the shortest path (to a win) of the best paths.-}
  size :: Int
  size = 3
  
  strategyForRed :: Board -> Int
  strategyForRed b = diff b (concat (bestmove b' R))
    where b' = convert b
    
  strategyForGreen :: Board -> Int
  strategyForGreen b = diff b (concat (bestmove b' G))
    where b' = convert b 
  
  diff :: Board -> Board -> Int
  diff b1 b2 = diff' b1' b2'
    where b1' = zip [0..] b1
          b2' = zip [0..] b2
  
  diff' :: [(Int, Field)] -> [(Int, Field)] -> Int
  diff' _ [] = -1
  diff' [] _ = -1
  diff' (f:fs) (f':fs') 
   | (snd f /= snd f') = fst f
   | otherwise = diff' fs fs'
  
  convert :: Board -> Board'
  convert = takeWhile (not . null) . map (take size) . iterate (drop size)
  
  type Board = [Field] 
  type Board' = [[Field]]
  data Field = B | R | G
                deriving (Eq, Ord, Show)
  
  next :: Field -> Field
  next B = B
  next R = G
  next G = R
  
  test :: Board
  test = [ B, R, G,
           B, R, G,
           B, B, B ]
  
  test' :: Board
  test' = [ R, B, G,
            B, B, B,
            G, B, R ]
  
  empty :: Board'
  empty = replicate size (replicate size B)
  
  full :: Board' -> Bool
  full = all (/= B) . concat
  
  turn :: Board' -> Field
  turn b = if rs <= gs then R else G
           where
              rs = length (filter (== R) fs)
              gs = length (filter (== G) fs)
              fs = concat b
  
  wins :: Field -> Board' -> Bool
  wins f b = any line (rows ++ cols ++ diags)
             where
                line = all (== f)
                rows = b
                cols = L.transpose b
                diags = [diag b, diag (map reverse b)]
  
  diag :: Board' -> [Field]
  diag b = [b !! n !! n | n <- [0..size-1]]
  
  won :: Board' -> Bool
  won b = wins R b || wins G b
  
  valid :: Board' -> Int -> Bool
  valid b i = 0 <= i && i < size^2 && concat b !! i == B
  
  move :: Board' -> Int -> Field -> [Board']
  move b i f =
      if valid b i then [chop size (xs ++ [f] ++ ys)] else []
      where (xs, B:ys) = splitAt i (concat b)
  
  chop :: Int -> [a] -> [[a]]
  chop n [] = []
  chop n xs = take n xs : chop n (drop n xs)
  
  data Tree a = Node a [Tree a]
                deriving Show
  
  gametree :: Board' -> Field -> Tree Board'
  gametree b f = Node b [gametree b' (next f) | b' <- moves b f]
  
  moves :: Board' -> Field -> [Board']
  moves b f
     | won b     = []
     | full b    = []
     | otherwise = concat [move b i f | i <- [0..((size^2)-1)]]
  
  prune :: Int -> Tree a -> Tree a
  prune 0 (Node x _) = Node x []
  prune n (Node x ts) = Node x [prune (n-1) t | t <- ts]
  
  treeDepth :: Tree a -> Int
  treeDepth (Node _ []) = 0
  treeDepth (Node _ ts) = 1 + minimum (map treeDepth ts)
  
  depth :: Int
  depth = 9
  
  minimaxR :: Tree Board' -> Tree (Board', Field)
  minimaxR (Node b [])
     | wins R b  = Node (b, R) []
     | wins G b  = Node (b, G) []
     | otherwise = Node (b, B) []
  minimaxR (Node b ts)
     | turn b == R = Node (b, minimum fs) ts'
     | turn b == G = Node (b, maximum fs) ts'
                     where
                      ts' = map minimaxR ts
                      fs = [f | Node (_, f) _ <- ts']
  
  minimaxG :: Tree Board' -> Tree (Board', Field)
  minimaxG (Node b [])
     | wins R b  = Node (b, R) []
     | wins G b  = Node (b, G) []
     | otherwise = Node (b, B) []
  minimaxG (Node b ts)
     | turn b == R = Node (b, maximum fs) ts'
     | turn b == G = Node (b, minimum fs) ts'
                     where
                      ts' = map minimaxG ts
                      fs = [f | Node (_, f) _ <- ts']
  
  bestmove :: Board' -> Field -> Board'
  bestmove b f = head [b' | Node (b', f') _ <- L.sortOn treeDepth ts, f' == best]
                 where
                    tree = prune depth (gametree b f)
                    Node (_, best) ts 
                       | f == R = minimaxR tree
                       | f == G = minimaxG tree

  --4.4 (Optional) Drawing Game Trees and Strategies (30pts EC)
  drawStrategy :: Bool -> String -> IO ()
  drawStrategy = undefined