import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AccountantReducer extends Reducer<Text,CategoryStats,Text,CategoryStats.Fancy> {
    private static class PreSortEntry implements Comparable<PreSortEntry> {
        String key;
        CategoryStats stats;

        PreSortEntry(String key, CategoryStats stats) {
            this.key = key;
            this.stats = stats;
        }

        @Override
        public int compareTo(PreSortEntry other) {
            int statsCompared = stats.compareTo(other.stats);
            if (statsCompared == 0) {
                return key.compareTo(other.key);
            }
            return -statsCompared;
        }
    };
    
    private List<PreSortEntry> list = new ArrayList<>();

    private static final CategoryStats ZERO = new CategoryStats(0.0, 0L);

    @Override
    public void reduce(Text key, Iterable<CategoryStats> values, Context context) throws IOException, InterruptedException {
        CategoryStats result = StreamSupport.stream(values.spliterator(), false)
                .reduce(ZERO, (a, b) -> a.add(b), (a, b) -> a.add(b));
        list.add(new PreSortEntry(key.toString(), result));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        Collections.sort(list);
        fancyFormat(context);
    }

    private void fancyFormat(Context context) throws IOException, InterruptedException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("###0.000", symbols);
        
        List<List<String>> table = new ArrayList<>();
        table.add(Arrays.asList("Category", "Revenue", "Quantity"));

        for (PreSortEntry entry : list) {
            table.add(Arrays.asList(
                    entry.key,
                    format.format(entry.stats.getRevenue()),
                    Long.toString(entry.stats.getCount())
                    ));
        }

        List<Integer> maxes = Arrays.asList(0, 0, 0);
        for (List<String> row : table) {
            for (int i = 0; i < 3; i++) {
                int length = row.get(i).length();
                if (length > maxes.get(i)) {
                    maxes.set(i, length);
                }
            }
        }

        for (List<String> row : table) {
            Text key = new Text(String.format(String.format("%%-%ds", maxes.get(0)), row.get(0)));
            CategoryStats.Fancy value = new CategoryStats.Fancy(
                    String.format(String.format("%%-%ds", maxes.get(1)), row.get(1)),
                    String.format(String.format("%%-%ds", maxes.get(2)), row.get(2))
                    );
            context.write(key, value);
        }
    }
}
