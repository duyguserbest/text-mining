package io.duygu.preprocess;

public enum OperationType {
    PARSE(Constants.PARSE_LOG_PREFIX),
    ELIMINATION(Constants.ELIMINATION_LOG_PREFIX),
    TOKENIZATION(Constants.TOKENIZATION_LOG_PREFIX),
    VOCABULARY(Constants.VOCABULARY_LOG_PREFIX),
    TERM(Constants.TERM_LOG_PREFIX),
    MATRIX(Constants.MATRIX_LOG_PREFIX),
    CATEGORY(Constants.CATEGORY_LOG_PREFIX),
    CENTROID(Constants.CENTROID_LOG_PREFIX),
    CENTROIDS(Constants.CENTROIDS_LOG_PREFIX),
    CLUSTERING(Constants.CLUSTERING_LOG_PREFIX),
    CLUSTERING_ITERATION(Constants.CLUSTERING_ITERATION_LOG_PREFIX),
    PURITY(Constants.PURITY_LOG_PREFIX);


    private String logPrefix;

    OperationType(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public String getLogPrefix() {
        return logPrefix;
    }

    private static class Constants {
        public static final String PARSE_LOG_PREFIX = "Json parse";
        public static final String ELIMINATION_LOG_PREFIX ="Filtering out thesis with null abstracts";
        public static final String TOKENIZATION_LOG_PREFIX = "Tokenization";
        public static final String VOCABULARY_LOG_PREFIX = "Vocabulary creation";
        public static final String TERM_LOG_PREFIX = "Counting documents in which term appeared for each term in vocabulary";
        public static final String MATRIX_LOG_PREFIX = "Matrix creation";
        public static final String CATEGORY_LOG_PREFIX = "Category parse";
        public static final String CENTROID_LOG_PREFIX = "Calculating new centroid for cluster %s at clustering iteration %s by using %s vectors";
        public static final String CENTROIDS_LOG_PREFIX = "Centroid calculation";
        public static final String CLUSTERING_LOG_PREFIX ="Clustering";
        public static final String CLUSTERING_ITERATION_LOG_PREFIX ="Iteration %s of clustering";
        public static final String PURITY_LOG_PREFIX ="Purity calculation";
    }
}
