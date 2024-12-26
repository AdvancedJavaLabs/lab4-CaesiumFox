import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main {
    public static void main(String[] args) throws Exception {
        Path inputDir = new Path("/user/csf/input");
        Path outputDir = new Path("/user/csf/output");

        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        if (hdfs.exists(outputDir)) {
            hdfs.delete(outputDir, true);
        }

        Job accountantJob = Job.getInstance(conf, "accountant");
        accountantJob.setJarByClass(Main.class);
        accountantJob.setMapperClass(AccountantMapper.class);
        accountantJob.setCombinerClass(AccountantCombiner.class);
        accountantJob.setReducerClass(AccountantReducer.class);
        accountantJob.setOutputKeyClass(Text.class);
        accountantJob.setOutputValueClass(CategoryStats.class);
        FileInputFormat.addInputPath(accountantJob, inputDir);
        FileOutputFormat.setOutputPath(accountantJob, outputDir);

        if (!accountantJob.waitForCompletion(true)) {
            System.exit(1);
        }
        System.exit(0);
    }
}
