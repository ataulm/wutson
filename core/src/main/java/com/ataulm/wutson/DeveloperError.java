package com.ataulm.wutson;

public class DeveloperError extends RuntimeException {

    private DeveloperError(String reason) {
        super(reason);
    }

    public static DeveloperError methodCannotBeCalledOutsideThisClass() {
        return new DeveloperError("Calling this method will potentially one of this class's intrinsic properties.");
    }

}
