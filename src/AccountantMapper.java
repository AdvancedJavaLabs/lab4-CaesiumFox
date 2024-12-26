import java.io.IOException;
import java.util.stream.Stream;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AccountantMapper extends Mapper<Object, Text, Text, CategoryStats>{
    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        Stream.of(value.toString().split("\n", 0))
                .parallel()
                .filter((s) -> !s.equals("transaction_id,product_id,category,price,quantity"))
                .map((s) -> new DataLine(s))
                .sequential()
                .forEachOrdered((d) -> {
                    try {
                        context.write(new Text(d.category), d.calculateCategoryStats());
                    } catch (Exception e) {
                        // Do nothing
                    }
                });
    }
}
