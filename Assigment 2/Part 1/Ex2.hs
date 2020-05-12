
module Ex2
( Foldable (..)
, ListBag (..)
, mapLB
) where 

import Ex1

-- creation of a Class Constructor Foldable for the data type ListBag

instance Foldable ListBag where
  foldr f z (LB []) = z
  foldr f z (LB ((a,b):xs)) = f a (foldr f z (LB xs))
  foldl f z (LB []) = z
  foldl f z (LB ((a,b):xs)) = foldl f (f z a) (LB xs)
  foldr1 _ (LB [(a,b)]) = a
  foldr1 f (LB ((a,b):xs)) = f a (foldr1 f (LB xs))
  foldl1 _ (LB [(a,b)]) = a
  foldl1 f (LB ((a,b):xs)) = f (foldl1 f (LB xs)) a
  length (LB xs) = length xs 


-- creation of a Class Constructor Functor for the data type ListBag

mapLB :: (t -> a) -> ListBag t -> ListBag a
mapLB f (LB xs) = 
  let map_couple_tl f [] curr = curr
      map_couple_tl f ((a,b):xs) curr = map_couple_tl f xs (curr++[(f a,b)])
      in (LB (map_couple_tl f xs []))

instance Functor ListBag where
  fmap f (LB xs) = mapLB f (LB xs)

