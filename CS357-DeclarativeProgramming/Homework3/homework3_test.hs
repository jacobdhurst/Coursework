
{-

File for testing solutions to Homework 3. 
Peter Blemel
Based on Luke's idea

Tests are not exhaustive. I just picked some things that seemed reasonable.

testMakeCommand in particular may be brittle. I didn't have a lot of damaged
postscript sets to test it on. If you break it, you can either fix it yourself
or send me the bad text and I'll fix it.

To use this with Homework 3 add the following to the top of your homework3.hs file.

module Homework3 
( Tree(LeafT,NodeT),
  balance,
  goldbach,
  church,
  powerset,
  makeCommand,
  T(Leaf,Node),
  P(GoLeft,GoRight,This),
  allpaths,
  eval,
  satisfiable 
) where

And then run :

$ ghci homework3_test.hs homework3.hs
*Main> testAll

You can make this run faster by commenting or changing the lines that use random data.

They look like this:
    testForest 20 5 rndNums1
   
    Where :
        First argument is how many trees to grow
        Second is how tall to grow them.

    checkCommand' d (makeCommand d) | d <- drawings 10 30 1000 1000 rndNums2

    Where :
        First argument is how many random drawings to make
        Second is how complex the shapes in the drawing can get.
        Third and fourth are the max dimensions of the shapes
-}

import Data.List
import Data.List.Split
import System.Random
import Homework3

testAll = and [
    testBalance, 
    testGoldbach, 
    testChurch, 
    testPowerset, 
    testMakeCommand, 
    testAllPaths,
    testEval,
    testSatisfiable
  ]

-- Random number generator stuff :
--
-- Used manually to get a random seed to pase into the test function.
--
newRand = randomIO :: IO Int

-- Generates a pseudo-random sequece of doubles from 0 to 1.
randomList :: Int -> [Double]
randomList seed = randoms (mkStdGen seed) :: [Double]

-- A couple of random number seeds to play with. Can be generated by pasting the result
-- of newRand.
--
rndNums1 = randomList 3869386208656114178
rndNums2 = randomList 3078229163598965381

-- 2.1
-- Returns a list containing the number of leafs below every node in the tree.
leafs' (LeafT _) = [(0,0)] -- Singleton
leafs' (NodeT (LeafT _) (LeafT _)) = [(1,1)]
leafs' (NodeT (LeafT _) t2) = (1, lc'' + rc'') : rl where rl@((lc'',rc''):_) = leafs' t2
leafs' (NodeT t1 (LeafT _)) = (lc' + rc', 1) : ll where ll@((lc',rc'):_) = leafs' t1
leafs' (NodeT t1 t2) = (lc' + rc', lc'' + rc'') : (ll ++  rl)
                       where ll@((lc',rc'):_) = leafs' t1
                             rl@((lc'',rc''):_) = leafs' t2

-- Returns the number of leafs for each child of the given node.
leafs n = (l + r) where (l,r) = head $ leafs' n

-- Height balance - A binary tree is height balanced if for any two leaves the
-- difference of the depth is at most 1.
--
-- Weight balance - A binary tree is weight balanced if for each node it holds
-- the number of inner nodes in the left subtree and the number of inner nodes
-- in the right subtree differ by at most 1. Weight balance implies height
-- balance.
--
-- We are given problem 4 on Page 109. Using problem 3 for context, the tree
-- is balanced if the number of chilren in the left and right branches of
-- every node differs by no more than 1. This is basically the same as weight
-- balancing.
--
-- Trying to be more functional using and . map below.
-- balanced (LeafT _) = True -- Singleton
-- balanced n@(NodeT t1 t2) = fst $ balanced' n
--                            where balanced' (LeafT _) = (True,1) -- Singleton
--                                  balanced' (NodeT t1 t2) = (lb && rb && abs (lc - rc) <= 1, lc + rc)
--                                                            where (lb,lc) = balanced' t1
--
balanced :: Tree a -> Bool
balanced (LeafT _) = True -- Singleton
balanced n@(NodeT t1 t2) = and . map (\(l,r) -> (abs $ l - r) <= 1) $ leafs' n

testBalance' :: [Int] -> Bool
testBalance' xs@[x] = (balanced t) && (leafs t) == 0 where t = balance xs
testBalance' xs = (balanced t) && (leafs t) == length xs where t = balance xs

t1 = (NodeT (LeafT 0) (NodeT (LeafT 0) (NodeT (LeafT 0) (LeafT 0))))
t2 = (NodeT t1 t1)

-- Checks if trees created by 'balance' are in fact balanced.
testBalance = and [
    testBalance' [1],
    testBalance' [1..2],
    testBalance' [1..3],
    testBalance' [1..1025],
    balanced (NodeT (balance [1..1024]) (balance [1..1025]))
    -- This really just test that I haven't broken my balanced function.
    --, not $ balanced (NodeT (balance [1..4]) (balance [1..6])),
    --, not $ balanced (NodeT (NodeT (balance [1..13]) (balance [1..13])) (LeafT 0)),
    --, not $ balanced (NodeT (LeafT 0)(NodeT (balance [1..13]) (balance [1..13])))
  ]

-- 2.2
testGoldbach = and [
    goldbach 6 == [(3,3)], 
    goldbach 20 == [(3,17),(7,13)],
    goldbach 100 == [(3,97),(11,89),(17,83),(29,71),(41,59),(47,53)]
  ]

-- 2.3
testChurch = and [
    (church 4) tail "ABCDEFGH" == "EFGH",
    (church 5) (+1) 1 == 6,
    (church 5) (map (+1)) [1..10] == [6..15]
  ]

-- 2.4

-- A sequence of power sets for testing.
ps1 = [[1 :: Int], []]
ps2 = ps1 ++ [[2],[1,2]]
ps3 = ps2 ++ [[3],[2,3],[1,3],[1,2,3]]
ps4 = ps3 ++ [[4], [3, 4], [2, 4], [2, 3, 4], [1, 4], [1, 3, 4], [1, 2, 4], [1, 2, 3, 4] ]

 -- All xs are in ys (e.g. and [elem x ys | x <- xs])
contains' xs ys = foldl (\v x -> v && elem x ys) True xs

 -- Remove xs from ys
delete' xs ys = foldr delete ys xs

 -- Contains all xs and nothing else
just xs ys = contains' xs ys && delete' xs ys == []

-- Tests the powerset function.
testPowerset = and [
                    just ps1 (powerset [1]),
                    just ps2 (powerset [1..2]),
                    just ps3 (powerset [1..3]),
                    just ps4 (powerset [1..4])
               ]

-- 2.5

-- Types for readability.
type BoundingRect = (Double, Double, Double, Double)
type Shape = [(Double,Double)]
type Drawing = [Shape]

-- Max floating point value.  It's odd that this isn't defined in the prelude.
maxFloat :: RealFloat a => a -> a
maxFloat a = encodeFloat m n where
    b = floatRadix a
    e = floatDigits a
    (_, e') = floatRange a
    m = b ^ e - 1
    n = e' - e
rectMax = maxFloat (0 :: Double)
rectMin = - maxFloat (0 :: Double)
nullRect = (rectMax, rectMax, rectMin, rectMin)

-- Parses a statement after a shape. Can be another shape or 'showpage' '%%EOF'.
cmd :: Drawing -> [String] -> (Bool,String,BoundingRect)
cmd _ [] = (False,"cmd []", nullRect)
cmd _ [s] = (False,"cmd eror : " ++ s, nullRect)
cmd (p:ps) (s:ss) 
           | s == "" = cmd (p:ps) ss -- There may (should?) be an empty line before showpage.
           | s == "showpage" && head ss == "%%EOF" = (True, "EOF!", nullRect)
           | otherwise = moveTo (p:ps) (s:ss)

-- Parses a lineto statement.
lineTo :: Drawing -> [String] -> (Bool,String,BoundingRect)
lineTo _ [] = (False,"lineTo eror : []", nullRect)
lineTo _ [s] = (False,"lineTo eror : " ++ s, nullRect)
lineTo (p:ps) (s:ss) 
           | s == "closepath" = if head ss == "stroke" then cmd (p:ps) (tail ss) else (False,"lineTo eror : " ++ s, nullRect)
           | c == [] = (False,"lineTo eror : " ++ s, nullRect)
           | head c == "lineto" = lineTo'
           | otherwise =  (False,"lineTo eror : " ++ s, nullRect)
           where args = splitOn " " s
                 pos = take 2 args
                 c = drop 2 args
                 lineTo' = if stat == True then (True, msg, ((min x x1, min y y1, max x x2, max y y2))) else rest
                           where rest@(stat, msg, (x1,y1,x2,y2)) = lineTo (p:ps) ss -- Recurse to next statement.
                                 x = read $ head pos 
                                 y = read $ last pos 

-- Parses a moveto statement.
moveTo :: Drawing -> [String] -> (Bool,String,BoundingRect)
moveTo _ [] = (False,"moveTo eror : []", nullRect)
moveTo (p:ps) (s:ss) 
           | s == "" = moveTo (p:ps) ss
           | c == [] = (False,"moveTo eror : " ++ s, nullRect)
           | head c == "moveto" = moveTo'
           | otherwise =  (False,"moveTo eror : " ++ s, nullRect)
           where args = splitOn " " s
                 pos = take 2 args
                 c = drop 2 args
                 moveTo' = if stat == True then (True, msg, ((min x x1, min y y1, max x x2, max y y2))) else rest
                           where rest@(stat, msg, (x1,y1,x2,y2)) = lineTo (p:ps) ss -- Recurse to next statement.
                                 x = read $ head pos 
                                 y = read $ last pos 

-- Parses the bounding rectangle coordinates.
rect :: [String] -> BoundingRect
rect (x1:y1:x2:y2:[]) = (read x1, read y1, read x2, read y2)

-- Parses the bounding box statement
bounding :: Drawing -> [String] -> (Bool,String,BoundingRect)
bounding _ [] = (False,"bounding eror : []", nullRect)
bounding (p:ps) (s:ss) 
           | args == [] = (False,"bounding eror : " ++ s, nullRect)
           | c == "%%BoundingBox:" = if x1 == x' && y1 ==  y' && x2 == x'' && y2 == y'' then rest else (False,"bounding box eror : " ++ s, bb)
           | otherwise =  (False,"bounding eror : " ++ s, nullRect)
           where args = splitOn " " s
                 c = head args
                 pos = tail args
                 (x', y', x'', y'') = rect pos
                 rest@(stat, msg, bb@(x1,y1,x2,y2)) = moveTo (p:ps) ss -- Recurse to next statement.

-- Parses the header
header :: Drawing -> [String] -> (Bool,String,BoundingRect)
header _ [] = (False,"header []", nullRect)
header (p:ps) (s:ss) 
           | s == "%!PS-Adobe-3.0 EPSF-3.0" = bounding (p:ps) ss
           | otherwise =  (False,"header eror : " ++ s, nullRect)

-- Handy interface to figure out what's wrong in a command.
checkCommand :: Drawing -> String -> (Bool,String,BoundingRect)
checkCommand ps t = header ps $ splitOn "\n" t

checkCommand' :: Drawing -> String -> Bool
checkCommand' ps t = v where (v,e,r) = header ps $ splitOn "\n" t

-- Creates a random shape for testing makeCommand.
path :: Int -> Double -> Double -> [Double] -> (Shape,[Double])
path 0 _ _ rs = ([], rs)
path n h w (r1:r2:rs) = ((x, y) : s, rs')
                        where x = r1 * h
                              y = r2 * w
                              (s, rs') = path (n-1) h w rs

-- Creates a list of random shapes comprising a drawing.
-- drawing :: d -> n -> h -> w -> rs -> (Drawing, [Double])
-- where d = # of shapes in drawing.
--       n = max # of segments in a shape.
--       h = max height of shape.
--       w = max width of shape.
--       rs = random number sequence.
--
draw :: Int -> Int -> Double -> Double -> [Double] -> (Drawing, [Double])
draw 0 _ _ _ rs = ([],rs)
draw d n h w (r1:rs) = (s'' : s', rs'')
                       where count = ceiling (r1 * fromIntegral n) -- Number of segments int the shape
                             (s', rs') = draw (d-1) n h w rs  -- Recursive call 1st by using rs
                             (s'', rs'') = path count h w rs' -- Shape call 2nd by using rs'

-- Recursively compiles a list of drawings.
drawings' :: Int -> Int -> Double -> Double -> [Double] -> ([Drawing], [Double])
drawings' 0 _ _ _ rs = ([],rs)
drawings' d n h w (r1:rs) = (s'' : s', rs'')
                            where count = ceiling (r1 * fromIntegral n) -- Number of shapes in the drawing.
                                  (s', rs') = drawings' (d-1) n h w rs  -- Recursive call 1st by using rs
                                  (s'', rs'') = draw count n h w rs' -- Drawing call 2nd by using rs'

-- Compiles a list of drawings.
drawings :: Int -> Int -> Double -> Double -> [Double] -> [Drawing]
drawings 0 _ _ _ rs = []
drawings d n h w rs = fst $ drawings' d n h w rs

testCmdData = [[(100.0,100.0),(100.0,200.0),(200.0,100.0)],[(150.0,150.0),(150.0,200.0),(200.0,200.0),(200.0,150.0)]]
testCmd = "%!PS-Adobe-3.0 EPSF-3.0\n%%BoundingBox: 100.0 100.0 200.0 200.0\n\n100.0 100.0 moveto\n100.0 200.0 lineto\n200.0 100.0 lineto\nclosepath\nstroke\n\n150.0 150.0 moveto\n150.0 200.0 lineto\n200.0 200.0 lineto\n200.0 150.0 lineto\nclosepath\nstroke\n\nshowpage\n%%EOF"

-- Tests makeCommand
testMakeCommand = and [
    makeCommand testCmdData == testCmd
  ]


-- 2.6

-- Synthesize a tree from a path.
growBranch :: P -> T
growBranch This = Leaf
growBranch (GoRight r) = Node Leaf (growBranch r)
growBranch (GoLeft l) = Node (growBranch l) Leaf

-- Grow a branch from the root along a path.
growPath :: T -> P -> T
growPath Leaf This = Leaf -- Root
growPath Leaf (GoLeft l) = Node (growBranch l) Leaf -- Grow tree to left
growPath Leaf (GoRight r) = Node Leaf (growBranch r) -- Grow tree to right
growPath (Node t' t'') (GoLeft l) = Node (growPath t' l) t'' -- Visit tree to left
growPath (Node t' t'') (GoRight r) = Node t' (growPath t'' r) -- Visit tree to right
growPath (Node t' t'') This = Node t' t'' -- Keep

-- Grow a tree from a set of paths.
growTree :: [P] -> T
growTree ps = foldr (\p t' -> growPath t' p) Leaf ps

-- Random path generation is messy because we need to consume the random number list
-- in order by continuously traversing it recursively in one long operation.
--
--
-- Makes both left and right subtrees. Kind of a kludge, but I couldn't nest this in mkPaths.
mkLeftRight :: Int -> [Double] -> (P -> P)  -> (P -> P) -> (P -> P)  -> [P] -> ([P], [Double])
mkLeftRight d xs p leftP rightP ps =
              (p This : left, rnd)
              where (right, r') = mkPaths (d-1) xs rightP ps
                    (left,rnd) = mkPaths (d-1) r' leftP right 

-- Makes a random set of paths that comprise a tree.
mkPaths :: Int -> [Double] -> (P -> P)  -> [P] -> ([P], [Double])
mkPaths 0 xs p ps = (p This : ps, xs)
mkPaths d (x:xs) p ps 
        | x < 0.25 = (p This : leftP This : (rightP This : ps), xs)
        | x < 0.50 = let (right,rnd) = mkPaths (d-1) xs rightP ps in (p This : leftP This : right, rnd)
        | x < 0.75 = let (right,rnd) = mkPaths (d-1) xs leftP (rightP This : ps) in  (p This : right, rnd)
        | otherwise = mkLeftRight d (x:xs) p leftP rightP ps
        where rightP = (p . GoRight)
              leftP = (p . GoLeft) 

-- This wrapper makes a set of paths that comprise a tree. It is necessary because mkPaths
-- needs to start with a valid path for recursion. I choose 'GoLeft' and then strip it off
-- when I'm done.
--
mkTreePaths :: Int -> Int -> [Double] -> [[P]] -> ([[P]], [Double])
mkTreePaths 0 d xs ps = (ps,xs)
mkTreePaths n d xs ps =
        let
           (p'',r') = mkPaths d xs GoLeft [] -- Causes everythingto be rooted at 'GoLeft'.
           p' = map (\(GoLeft p) -> p) p''  -- Strip the 'GoLeft' roots out.
        in
           mkTreePaths (n-1) d r' (p':ps)

-- Makes a forest of trees
mkForest :: Int -> Int -> [Double] -> [([P],T)]
mkForest n d xs = zip paths $ mkTrees paths
                  where paths = (fst $ mkTreePaths n d xs [])
                        mkTrees ps = foldr (\x p -> growTree x  : p) [] ps

-- Tests allpaths on each tree in a forest of trees
testForest :: Int -> Int -> [Double] -> Bool
testForest n d xs = and [just (allpaths t) p | (p,t) <- mkForest n d xs]

-- Manually build up a tree.
p1 = [This] -- Root (Or leaf)
p2 = p1 ++ [GoLeft This, GoRight This] -- Node Leaf Leaf
p3 = p2 ++ [GoRight (GoLeft This),GoRight (GoRight This)] -- Node Leaf (Node Leaf Leaf)
p4 = p3 ++ [GoRight (GoRight (GoLeft This)),GoRight (GoRight (GoRight This))] -- Node Leaf (Node Leaf (Node Leaf Leaf))

-- Test allPaths
testAllPaths = and [
    allpaths Leaf ==  p1,
    allpaths (Node Leaf Leaf) ==  p2,
    allpaths (Node Leaf (Node Leaf Leaf)) == p3,
    allpaths (Node Leaf (Node Leaf (Node Leaf Leaf))) == p4
    , testForest 20 5 rndNums1 -- Random forest of short trees
    , testForest 10 10 rndNums2 -- Random forest of taller trees
  ]


-- Test eval
testEval = and [
    eval (\x -> if x <= 0 then undefined else even x) [[1, -1]],  -- Fail even -x (should be (not even x))
    eval even [[1, -1]],  -- False V True
    not $ eval even [[1], [-1]],  -- False ^ True
    not $ eval even [[-1, 3], [1]], -- (True V False) ^ False
    not $ eval even [[1],[-1, 3]], -- False ^ (True V False)
    eval even [[-1, 3], [4]], -- (True V False) ^ True
    not $ eval even [[4], [1, 3]], -- True ^ False
    eval even [[4], [1, -3]], -- True ^ (False V True)
    eval even [[-1, 3], [1, -3]], -- (True V False) ^ (False V True)
    eval even [[5, 3, -1], [1, -3]], -- (False V False V True) ^ (False V True)
    not $ eval even [[1, 3, 5], [-2, -3]], -- False ^ (True V True)
    not $ eval even [[1, 3, 5], [2, 4], [6, 8]] -- False ^ True ^ True
  ]

-- Test satisfiable
testSatisfiable = and [
  satisfiable [[-1, 2, 4], [-2, -3]],
  satisfiable [[-1], [1,2]],
  not $ satisfiable [[-1], [1]],
  not $ satisfiable [[-1],[-1, 2, 4],[1]],
  satisfiable [[1, -1]],
  not $ satisfiable [[1], [-1]],
  satisfiable [[-1, 2], [1, -2]],
  satisfiable [[1,-1], [1]],
  satisfiable [[1, 4, 5], [-2, -3]],
  satisfiable [[1, 3, 5], [2, 4], [6, 8]]]
