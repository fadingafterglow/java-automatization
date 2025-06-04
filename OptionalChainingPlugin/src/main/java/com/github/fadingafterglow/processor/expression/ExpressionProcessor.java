package com.github.fadingafterglow.processor.expression;

import java.util.ArrayList;
import java.util.List;

public class ExpressionProcessor implements IExpressionProcessor {

    private static final char OPERATOR = '?';

    @Override
    public String process(String expression) {
        SplitResult splitResult = split(expression);
        StringBuilder processed = splitResult.expression();
        for (String check : splitResult.checks().reversed())
            addCheck(processed, check);
        return processed.toString();
    }

    private SplitResult split(String expression) {
        StringBuilder processed = new StringBuilder();
        List<String> checks = new ArrayList<>();
        int i = 0;
        for (char current : expression.toCharArray()) {
            if (current == OPERATOR)
                checks.add(processed.substring(0, i));
            else {
                processed.append(current);
                i++;
            }
        }
        return new SplitResult(checks, processed);
    }

    private void addCheck(StringBuilder expression, String check) {
        expression.insert(0, " == null ? null : ");
        expression.insert(0, check);
        expression.insert(0, '(');
        expression.append(')');
    }

    private record SplitResult(List<String> checks, StringBuilder expression) {}
}
