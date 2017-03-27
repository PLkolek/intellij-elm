
someFu<caret>n : Int -> Int
someFun x =
    if x<= 0 then
        1
    else x * someFun (x-1)


funCall = someFun 100