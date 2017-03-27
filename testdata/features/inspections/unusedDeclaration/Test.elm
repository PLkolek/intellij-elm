module <warning descr="Unused module"><caret>Test</warning> exposing (..)

type <warning descr="Unused type">X</warning> = <warning descr="Unused type constructor">A1</warning>

type PortType = <warning descr="Unused type constructor">Cons</warning> | UsedCons

infix 7 <warning descr="Unused operator">.-></warning>

port <warning descr="Unused port">somePort</warning> : PortType

port usedPort : () -> PortType

useThings =
    ( usedPort ()
    , UsedCons
    )