import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class CategoryStats implements WritableComparable<CategoryStats> {
    public static class Fancy implements Writable {
        private String revenue;
        private String count;

        public Fancy(String revenue, String count) {
            this.revenue = revenue;
            this.count = count;
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(revenue);
            out.writeUTF(count);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            revenue = in.readUTF();
            count = in.readUTF();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(revenue);
            builder.append("   ");
            builder.append(count);
            return builder.toString();
        }
    }

    private double revenue;
    private long count;

    public CategoryStats() {
        revenue = 0.0;
        count = 0L;
    }

    public CategoryStats(double revenue, long count) {
        this.revenue = revenue;
        this.count = count;
    }

    public void set(double revenue, long count) {
        this.revenue = revenue;
        this.count = count;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(revenue);
        out.writeLong(count);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        revenue = in.readDouble();
        count = in.readLong();
    }

    @Override
    public int compareTo(CategoryStats o) {
        if (revenue < o.revenue) return -1;
        if (revenue > o.revenue) return 1;
        if (count < o.count) return -1;
        if (count > o.count) return 1;
        return 0;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(revenue).hashCode() ^ Long.valueOf(count).hashCode();
    }

    public CategoryStats add(CategoryStats o) {
        return new CategoryStats(revenue + o.revenue, count + o.count);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(revenue);
        builder.append(" ");
        builder.append(count);
        return builder.toString();
    }

    public double getRevenue() {
        return revenue;
    }

    public long getCount() {
        return count;
    }
}
