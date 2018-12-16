{-
	Jacob Hurst
	CS 357: Homework1 
-}
module Homework1 where

import Data.List (group, sort)


{- 1.1 Simple functions on numbers -}
test :: Int -> Int -> Bool
test a b = odd (a*b)


{- 1.2 List manipulation -}
{- Part 1 -}
stutter :: [Char] -> [Char]
stutter [] = []
stutter (x:xs) = x:x:stutter xs

{- Part 2 -}
compress :: [Char] -> [Char]
compress [] = []
compress [x] = [x]
compress (x1:x2:xs)
 | (x1 == x2) = x1:compress (xs)
 | otherwise  = x1:compress (x2:xs)

{- Part 3 -}
zipSum :: [Int] -> [Int] -> [Int]
zipSum [] [] = []
zipSum (x:xs) [] = (x:xs)
zipSum [] (y:ys) = (y:ys)
zipSum (x:xs) (y:ys) = map (\(i, j) -> i + j) (zip (x:xs) (y:ys))


{- 1.3 Using lists for sets: writing recursive functions over lists -}
{- Part 1 -}
setUnion :: [Integer] -> [Integer] -> [Integer]
setUnion [] [] = []
setUnion (x:xs) [] = (x:xs)
setUnion [] (y:ys) = (y:ys)
setUnion (x:xs) (y:ys) = map head $ group (z:zs)
 where (z:zs) = sort $ (x:xs) ++ (y:ys)

{- Part 2 -}
setIntersection :: [Integer] -> [Integer] -> [Integer]
setIntersection _ [] = []
setIntersection [] _ = []
setIntersection (x:xs) (y:ys) = keepDuplicates $ group (z:zs)
 where (z:zs) = sort $ (x:xs) ++ (y:ys)
       keepDuplicates :: [[Integer]] -> [Integer]
       keepDuplicates [] = []
       keepDuplicates (a:as)
        | (length a == 1) = keepDuplicates as
        | otherwise       = (head a):keepDuplicates as

{- Part 3 -}
setDifference :: [Integer] -> [Integer] -> [Integer]
setDifference xs [] = xs
setDifference [] ys = ys
setDifference (x:xs) (y:ys) = removeDuplicates $ group (z:zs)
 where (z:zs) = sort $ (x:xs) ++ (y:ys)
       removeDuplicates :: [[Integer]] -> [Integer]
       removeDuplicates [] = []
       removeDuplicates (a:as)
        | (length a == 1) = (head a):removeDuplicates as
        | otherwise       = removeDuplicates as

{- Part 4 -}
setEqual :: [Integer] -> [Integer] -> Bool
setEqual [] [] = True
setEqual _ [] = False
setEqual [] _ = False
setEqual (x:xs) (y:ys) = if (x == y) then setEqual xs ys else False


{- 1.4 More involved functions on numbers -}
{- Part 1 -}
dr :: Integer -> Int
dr n 
 | (n `mod` 10 == n) = fromInteger n
 | otherwise         = dr (toInteger (sum (toList n)))
 where toList :: Integer -> [Int]
       toList x = map (\xs -> read [xs] :: Int) (show x)