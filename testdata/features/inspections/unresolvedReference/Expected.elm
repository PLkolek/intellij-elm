module Module exposing (Type(BadCons),
    (...), badPort)

import BadModule
import ModuleToImport

type alias Alias = BadType (List Int)

type Type = Cons ModuleToImport<caret>.

properValue = improperValue

infixl 2 <>>