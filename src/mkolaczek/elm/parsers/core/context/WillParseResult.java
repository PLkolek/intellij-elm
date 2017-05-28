package mkolaczek.elm.parsers.core.context;

import com.google.common.base.Preconditions;

public class WillParseResult {

    private final boolean success;
    private final int remainingLookahead;


    private WillParseResult(boolean success, int remainingLookahead) {
        this.success = success;
        this.remainingLookahead = remainingLookahead;
    }

    public static WillParseResult success(int remainingLookahead) {
        Preconditions.checkArgument(remainingLookahead >= 0);
        return new WillParseResult(true, remainingLookahead);
    }

    public static WillParseResult failure() {
        return new WillParseResult(false, -1);
    }


    public static WillParseResult create(boolean success, int remainingLookahead) {
        return success ? success(remainingLookahead) : failure();
    }

    public boolean isSuccess() {
        return success;
    }

    public int remainingLookahead() {
        Preconditions.checkState(isSuccess());
        return remainingLookahead;
    }
}
