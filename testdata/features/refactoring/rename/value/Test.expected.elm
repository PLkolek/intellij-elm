module Test exposing (SomeType, somePort, newName)

type SomeType
    = A
    | B

port somePort : Cmd String

type alias Alias = SomeType

newName : Int -> Int
newName x =
    (\someValue otherVal -> otherVal)
        newName
        (let
            someValue = 2
        in
            someValue)

properUsage = newName