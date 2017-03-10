module Test exposing (SomeType(A, B))

type SomeType
    = A<caret>
    | B

type alias Alias = SomeType