module Main exposing (..)


testVal : List Int
testVal =
    [ 1, 2, 3 ]


someFun testVal =
    let
        testVal = 3
        otherVal = testVal
    in
        testVa<caret>l + 2
