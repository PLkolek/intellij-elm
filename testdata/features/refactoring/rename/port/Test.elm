module Test exposing (SomeType, someP<caret>ort)

type SomeType
    = A
    | B

port somePort : Cmd String

type alias Alias = SomeType