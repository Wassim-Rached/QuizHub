package org.wa55death405.quizhub.utils;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBErrorExtractorUtils {

    public static SQLErrorDetails extractSQLErrorDetails(String message) {
        String constraintName = extractConstraintName(message);
        String tableName = extractTableName(message);
        String entityId = extractEntityId(message);

        return new SQLErrorDetails(constraintName, tableName, entityId);
    }

    private static String extractConstraintName(String message) {
        // Pattern to find the constraint name in the format: "foreign key constraint \"fk_constraint_name\""
        Pattern pattern = Pattern.compile("foreign key constraint \"(\\w+)\"");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String extractTableName(String message) {
        // Pattern to find the table name in the format: "table \"table_name\""
        Pattern pattern = Pattern.compile("table \"(\\w+)\"");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return transformTableName(matcher.group(1));
        }
        return null;
    }

    private static String extractEntityId(String message) {
        // Pattern to find the entity ID in the format: "Key (entity_id)=(45)"
        Pattern pattern = Pattern.compile("Key \\(\\w+_id\\)=\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String transformTableName(String tableName) {
        String[] parts = tableName.split("_");
        StringBuilder transformedName = new StringBuilder();
        for (String part : parts) {
            transformedName.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
        }
        return transformedName.toString().trim();
    }

    @Data
    public static class SQLErrorDetails {
        private final String constraintName;
        private final String tableName;
        private final String entityId;

        public SQLErrorDetails(String constraintName, String tableName, String entityId) {
            this.constraintName = constraintName;
            this.tableName = tableName;
            this.entityId = entityId;
        }

        public boolean allFieldsPresent() {
            return constraintName != null && tableName != null && entityId != null;
        }
    }
}
