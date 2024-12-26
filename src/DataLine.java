public class DataLine {
    long transaction_id;
    long product_id;
    String category;
    double price;
    long quantity;

    DataLine(String csvLine) {        
        String[] cells = csvLine.split(",", 0);
        if (cells.length < 5) {
            throw new IllegalArgumentException("Line " + csvLine + " doesn't have 5 fields");
        }
        transaction_id = Long.parseLong(cells[0]);
        product_id = Long.parseLong(cells[1]);
        category = cells[2];
        price = Double.parseDouble(cells[3]);
        quantity = Long.parseLong(cells[4]);
    }

    CategoryStats calculateCategoryStats() {
        return new CategoryStats(price * quantity, quantity);
    }
}
