{-
Jacob Hurst
Jhurst
-}

--No other imports are allowed
import Data.List

--2.1
collatz :: [Int] -> Int
collatz = snd . head . reverse . sort . collatzTuple

collatzTuple :: [Int] -> [(Int, Int)]
collatzTuple [] = []
collatzTuple xs = zip (map collatzLength xs)  xs

collatzLength :: Int -> Int
collatzLength 1 = 1
collatzLength n 
 | n `mod` 2 == 0 = 1 + collatzLength (n `div` 2)
 | otherwise  = 1 + collatzLength (3*n + 1)

--2.2
haskellFileNames :: [String] -> [String]
haskellFileNames = filter validNames
 where validNames :: String -> Bool
       validNames s = isSuffixOf ".hs" (trim s) || isSuffixOf ".lhs" (trim s)

trim :: String -> String
trim = dropWhileEnd (== ' ') . dropWhile (== ' ')

--2.3
select :: (t -> Bool) -> [t] -> [a] -> [a]
select = indexSelection

indexSelection :: (t -> Bool) -> [t] -> [a] -> [a]
indexSelection p ts as = [as !! index | index <- findIndices p ts]

--2.4
prefixSum :: [Int] -> [Int]
prefixSum = filter (/= 0) . scanl (+) 0

--2.5
numbers :: [Int] -> Int
numbers = read . concatMap show

--2.6
type Numeral = (Int, [Int])
example = (10, [1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0])

--2.6 1
makeLongInt :: Integer -> Int -> Numeral
makeLongInt = toNumeral

toNumeral :: Integer -> Int -> Numeral
toNumeral n r = (r, reverse (inRadix n r))

inRadix :: Integer -> Int -> [Int]
inRadix 0 _ = []
inRadix n r = map fromInteger ((n `mod` toInteger r) : map toInteger (inRadix ( n `div` (toInteger r)) r))

--2.6 2
evaluateLongInt :: Numeral -> Integer
evaluateLongInt = inDecimal

inDecimal :: Numeral -> Integer
inDecimal n = sum $ zipWith (*) (map toInteger (reverse (snd n))) (iterate (*(toInteger (fst n))) 1)

--2.6 3 :/
changeRadixLongInt :: Numeral -> Int -> Numeral 
changeRadixLongInt = changeNumeral

changeNumeral :: Numeral -> Int -> Numeral
changeNumeral n r = (r, convert n r)

convert :: Numeral -> Int -> [Int]
convert n r
 | (fst n == r) = snd n
 | otherwise = snd n

--2.6 4
addLongInts :: Numeral -> Numeral -> Numeral
addLongInts n1 n2
 | (fst n1 == fst n2) = n1
 | (fst n1 > fst n2) = addLongInts n1 (changeRadixLongInt n2 (fst n1))
 | (fst n1 < fst n2) = addLongInts (changeRadixLongInt n1 (fst n2)) n2

--2.6 5
mulLongInts :: Numeral -> Numeral -> Numeral
mulLongInts n1 n2
 | (fst n1 == fst n2) = n1
 | (fst n1 > fst n2) = mulLongInts n1 (changeRadixLongInt n2 (fst n1))
 | (fst n1 < fst n2) = mulLongInts (changeRadixLongInt n1 (fst n2)) n2