module Test exposing ((<->), SomeType)

type SomeType
    = A
    | B

infixl 2 <->

type alias Alias = SomeType