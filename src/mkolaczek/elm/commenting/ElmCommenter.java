package mkolaczek.elm.commenting;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

public class ElmCommenter implements Commenter {
    @Nullable
    @Override
    public String getLineCommentPrefix() {
        return "--";
    }

    @Nullable
    @Override
    public String getBlockCommentPrefix() {
        return "{-";
    }

    @Nullable
    @Override
    public String getBlockCommentSuffix() {
        return "-}";
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentPrefix() {
        return "{-";
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentSuffix() {
        return "-}";
    }
}
