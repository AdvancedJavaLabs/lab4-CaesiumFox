import java.io.IOException;

import java.util.stream.StreamSupport;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AccountantCombiner extends Reducer<Text,CategoryStats,Text,CategoryStats> {
    private static final CategoryStats ZERO = new CategoryStats(0.0, 0L);

    @Override
    public void reduce(Text key, Iterable<CategoryStats> values, Context context) throws IOException, InterruptedException {
        CategoryStats result = StreamSupport.stream(values.spliterator(), false)
                .reduce(ZERO, (a, b) -> a.add(b), (a, b) -> a.add(b));
        context.write(key, result);
    }
}

