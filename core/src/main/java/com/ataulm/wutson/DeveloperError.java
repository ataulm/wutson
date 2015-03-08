package com.ataulm.wutson;

public final class DeveloperError extends RuntimeException {

    private DeveloperError(String reason) {
        super(reason);
    }

    public static DeveloperError nonInstantiableClass() {
        return new DeveloperError("This class is not meant to be instantiated at all.");
    }

    public static DeveloperError methodCannotBeCalledOutsideThisClass() {
        return new DeveloperError("Calling this method will potentially one of this class's intrinsic properties.");
    }

}
