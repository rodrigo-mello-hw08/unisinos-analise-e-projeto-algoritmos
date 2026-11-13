package reporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Base para a geração dos relatórios CSV de métricas de busca. */
public abstract class SearchMetricsCsv {
    private final StringBuilder content;

    protected SearchMetricsCsv(String header) {
        content = new StringBuilder(header).append('\n');
    }

    protected final void appendRow(String... values) {
        for (int index = 0; index < values.length; index++) {
            if (index > 0) {
                content.append(',');
            }
            content.append(escape(values[index]));
        }
        content.append('\n');
    }

    public final void writeTo(Path output) throws IOException {
        Files.writeString(output, content);
    }

    private static String escape(String value) {
        boolean requiresQuotes = value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r");
        return requiresQuotes ? "\"" + value.replace("\"", "\"\"") + "\"" : value;
    }
}
