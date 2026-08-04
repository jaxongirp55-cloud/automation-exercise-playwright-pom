package sorting;

import java.util.List;
import java.util.ArrayList;

/**
 * Custom Merge Sort implementation.
 * Used to sort a collection of Sales Records (which we will represent as a helper class or double/decimal list).
 * We will design a generic or simple model for SalesRecords with an amount.
 *
 * Time Complexity (Worst, Average, Best): O(N log N)
 * Space Complexity: O(N) temporary array for merging.
 */
public class MergeSort {

    /**
     * Helper model class representing a sales transaction record to sort.
     */
    public static class SalesRecord {
        private String transactionId;
        private String customerName;
        private double saleAmount;

        public SalesRecord(String transactionId, String customerName, double saleAmount) {
            this.transactionId = transactionId;
            this.customerName = customerName;
            this.saleAmount = saleAmount;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public double getSaleAmount() {
            return saleAmount;
        }

        @Override
        public String toString() {
            return String.format("SalesRecord [ID=%s, Customer=%-15s, Amount=$%.2f]", transactionId, customerName, saleAmount);
        }
    }

    /**
     * Sorts a list of SalesRecord elements in-place by sale amount descending.
     * @param records List of records to sort.
     */
    public static void sort(List<SalesRecord> records) {
        if (records == null || records.size() < 2) {
            return;
        }
        mergeSort(records, 0, records.size() - 1);
    }

    private static void mergeSort(List<SalesRecord> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private static void merge(List<SalesRecord> list, int left, int mid, int right) {
        // Find sizes of two sub-lists to be merged
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Create temporary lists
        List<SalesRecord> leftList = new ArrayList<>(n1);
        List<SalesRecord> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) {
            leftList.add(list.get(left + i));
        }
        for (int j = 0; j < n2; ++j) {
            rightList.add(list.get(mid + 1 + j));
        }

        // Merge the temporary lists back (sorting DESCENDING)
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftList.get(i).getSaleAmount() >= rightList.get(j).getSaleAmount()) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftList if any
        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of rightList if any
        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
}
