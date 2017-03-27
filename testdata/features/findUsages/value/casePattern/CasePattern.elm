module Main exposing (..)


testVal : List Int
testVal =
    [ 1, 2, 3 ]


someFun testVal
    = case testVal of
        Just testVa<caret>l -> testVal
        Nothing -> 0