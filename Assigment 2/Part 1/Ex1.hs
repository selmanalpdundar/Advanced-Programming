module Ex1  
( ListBag  (..) 
, empty  
, singleton  
, fromList 
, wf  
, addToListBag 
, isEmpty
, mul
, toList
, sumBag
) where 

-- wf : well-formed
-- empty : return an empty ListBag
-- 
-- Implementation of Multisets --
{-|
    Type Declaration of List Bag  
-}
data ListBag a = LB [(a, Int)]
 deriving (Show, Eq)

-- ######### Constructors ######### --

{-|
---- Empty ListBag Constructor ----
Description: Returning an empty ListBag
-}
-- Type Declaration of Empty ListBag
empty :: ListBag a 
-- Definition of Empty ListBag Contructor
empty = (LB [])   -- Returns an empty bag


{-|
---- Sigleton ListBag Constructor ----
Description: Returning a ListBag containing just one occurence of element v.
-}
---- Type Declaration of Sigleton ListBag ----
singleton :: Eq a => a -> ListBag a
---- Definition of Sigleton Listbag
singleton v = LB [(v,1)]


{-|
---- fromList ListBag Constructor ----
Description: returning a ListBag containing all and only the elements of lst, 
each with the right multiplicity.
-}
-- Type Declaration of fromList
fromList :: (Foldable t1, Eq t2) => t1 t2 -> ListBag t2
-- Definition of fromList 
fromList lst = foldr addToListBag (LB []) lst

--- ######### Operations ######### ---
{-|
---- Well-formed Checking Operation ----
Description: check if it is well-formed or not return True it is else return False
-}
-- Type Declaration of wf
wf :: Eq a => ListBag a -> Bool
-- Definition of wf
wf (LB a) = 
 let wf_tl [] acc = acc 
     wf_tl (x:xs) acc = wf_tl xs (acc && not (isListBagContainsElement x (LB xs)))
     in wf_tl a True

{-|
---- isEmpty Operation ----
Description: returning True if and only if bag is empty.
-}
-- Type Declaration of isEmpty
isEmpty :: ListBag a -> Bool
-- Definition of isEmpty 
isEmpty bag= 
  case bag of  
    (LB []) -> True -- if list is empty return True
    (LB (x:xs)) -> False  -- if list has element return False



{-|
---- isListBagWellFormed Operations ----
Description:  return List itself it is well-formed otherwise it return an error
-}
isListBagWellFormed :: Eq a => ListBag a -> ListBag a
isListBagWellFormed listBag=
     if wf listBag then listBag
     else error "not well-formed ListBag" 

{-|
---- addToListBag Operation ----
Description: take an element and list  add that elment to list an
return the list. It calls addMultiplicityToListBag with v = 1 
which is number of repetation.
-}
-- addToListBag declaration
addToListBag :: Eq t => t -> ListBag t -> ListBag t
-- Definition of addToListBag
addToListBag a (LB xs) = addMultiplicityToListBag a 1 (LB xs)


{-|
---- addMultiplicityToListBag Operation ----
Description: -- lb_add_multiplicity el lb, 
returns the ListBag with the new element inserted with multiplicity

 a = element
 LB xs = ListBag
 m = multiplicity

-}
-- addMultiplicityToListBag el lb, returns the list lb with the new element el inserted with multiplicity m
addMultiplicityToListBag :: Eq a => a -> Int -> ListBag a -> ListBag a
addMultiplicityToListBag a m (LB xs) =
  let lb_add_tl prev [] to_add = LB (prev++[(to_add,m)])
      lb_add_tl prev ((a,b):xs) to_add
        |a == to_add = LB (prev++[(a,b+m)]++xs)
        |a /= to_add = lb_add_tl (prev++[(a,b)]) xs to_add 
      in isListBagWellFormed (lb_add_tl [] xs a)
 

 {-|
---- contains Operations ----
Description:  returns True if the list contains element otherwise returns False
-}
-- Declaration of isListBagContainsElement
isListBagContainsElement :: Eq a => (a, b) -> ListBag a -> Bool
-- Definition of isListBagContainsElement
isListBagContainsElement x (LB xs) =
 let contains' _ [] acc = acc
     contains' (a,b) ((y,z):xs) acc = contains' (a,b) xs (acc||y==a)
     in contains' x xs False    


{-|
---- mul Operations ----
Description: returning the multiplicity of v in the ListBag bag 
if v is an element of bag, and 0 otherwise
-}
-- Declaration of mul
mul :: Eq a => a -> ListBag a -> Int
-- Definition of mul v = elmeent  bag = listbag
mul v bag =
 case (isListBagWellFormed bag) of
  (LB []) -> 0 -- if list empty return 0
  -- If found v return b which is multicipty of b otherwise recursive remaining list to look for v.
  (LB ((a,b):xs)) -> if a==v then b else mul v (LB xs)


{-|
---- toList Operations ----
Description: that returns a list containing all the elements of the ListBag bag,
 each one repeated a number of times equal to its multiplicity
-}
-- Declaration of toList
toList :: Eq a => ListBag a -> [a]
-- Definition of toList
toList bag =
  let unwrap (a,b) = map (\_->a) [x | x <- [1..b]]
      toList_tl [] curr = curr
      toList_tl (x:xs) curr = toList_tl xs (curr++(unwrap x))
      in
        case (isListBagWellFormed bag) of
          (LB []) -> []
          (LB xs) -> toList_tl xs [] 


{-|
---- sumBag Operations ----
Description: Returning the ListBag obtained by adding all the elements of bag' to bag
first = bag and second = bag'
-}
-- Declaration of sumBag
sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
-- Definition of sumBag
sumBag first second =
  let sumBag_tl xa (LB []) = xa
      sumBag_tl xa (LB ((a,b):xb)) = sumBag (addMultiplicityToListBag a b xa) (LB xb)
      in isListBagWellFormed (sumBag_tl first second) 
