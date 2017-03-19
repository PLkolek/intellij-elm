module Test exposing (SomeType, newName)

type SomeType
    = A
    | B

port newName : Cmd String

type alias Alias = SomeType