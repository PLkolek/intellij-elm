module Test exposing (SomeType, somePort, someValu<caret>e)

type SomeType
    = A
    | B

port somePort : Cmd String

type alias Alias = SomeType

someValue : Int -> Int
someValue x =
    (\someValue otherVal -> otherVal)
        someValue
        (let
            someValue = 2
        in
            someValue)

properUsage = someValue