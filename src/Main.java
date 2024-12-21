import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("args.length < 2");
        }

        Path inputDir = new Path(args[0]);
        Path outputDir = new Path(args[1]);

        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        if (hdfs.exists(outputDir)) {
            hdfs.delete(outputDir, true);
        }

        Job job = Job.getInstance(conf, "par3");
        job.setJarByClass(Main.class);
        job.setMapperClass(AccountantMapper.class);
        job.setCombinerClass(AccountantReducer.class);
        job.setReducerClass(AccountantReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, inputDir);
        FileOutputFormat.setOutputPath(job, outputDir);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
