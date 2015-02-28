package com.ataulm.wutson;

public class ClassContractBrokenException extends RuntimeException {

    public ClassContractBrokenException() {
        super("Changing this property will breaking expectations about how this class behaves.");
    }

}
