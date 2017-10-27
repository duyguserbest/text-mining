package io.duygu.preprocess;

public enum OperationType {
    TOKENIZATION("Tokenization"),
    VOCABULARY("Tokenization"),
    TERM("Tokenization"),
    ASDS("Tokenization");

    private String logPrefix;

    OperationType(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public String getLogPrefix() {
        return logPrefix;
    }

    public OperationType setLogPrefix(String logPrefix) {
        this.logPrefix = logPrefix;
        return this;
    }
}
