module Module exposing (Type(<error descr="Unresolved type constructor">BadCons</error>),
    (<error descr="Unresolved operator">...</error>), <error descr="Unresolved port">badPort</error>)

import <error descr="Unresolved module">BadModule</error>

type alias Alias = <error descr="Unresolved type">BadType</error>

type Type = Cons <error descr="Unresolved module">ModuleToImport<caret></error>.<EOLError descr="[Token.CAP_VAR] expected"></EOLError>
