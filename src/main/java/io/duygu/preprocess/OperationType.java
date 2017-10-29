package io.duygu.preprocess;

public enum OperationType {
    TOKENIZATION(Constants.TOKENIZATION_LOG_PREFIX),
    VOCABULARY(Constants.VOCABULARY_LOG_PREFIX),
    TERM(Constants.TERM_LOG_PREFIX),
    MATRIX(Constants.MATRIX_LOG_PREFIX);

    private String logPrefix;

    OperationType(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public String getLogPrefix() {
        return logPrefix;
    }

    private static class Constants {
        public static final String TOKENIZATION_LOG_PREFIX = "Tokenization";
        public static final String VOCABULARY_LOG_PREFIX = "Vocabulary creation";
        public static final String TERM_LOG_PREFIX = "Counting documents in which term appeared for each term in vocabulary";
        public static final String MATRIX_LOG_PREFIX = "Matrix creation";
    }
}
