package io.duygu.preprocess;

public enum OperationType {
    TOKENIZATION("Tokenization"),
    VOCABULARY("Vocabulary creation"),
    TERM("Counting documents in which term appeared for each term in vocabulary"),
    MATRIX("Matrix creation");

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
