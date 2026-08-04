package sorting;

/**
 * Implementation of the Merge Sort Algorithm.
 * Satisfies the HND DS&A assignment to sort daily sales data manually.
 * Avoids any standard java library sorting calls.
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Best Case: O(N log N)
 *   - Average Case: O(N log N)
 *   - Worst Case: O(N log N)
 *   (Highly stable, predictable, divide-and-conquer strategy)
 * - Space Complexity: O(N) auxiliary space needed for split merges.
 *
 * @author Senior Java Software Architect
 */
public class MergeSort {

    /**
     * DTO to represent a daily sale transaction.
     */
    public static class SaleTransaction {
        private final String date;
        private final double salesAmount;

        public SaleTransaction(String date, double salesAmount) {
            if (date == null || date.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction date cannot be empty.");
            }
            if (salesAmount < 0.0) {
                throw new IllegalArgumentException("Sales amount cannot be negative.");
            }
            this.date = date.trim();
            this.salesAmount = salesAmount;
        }

        public String getDate() {
            return date;
        }

        public double getSalesAmount() {
            return salesAmount;
        }

        @Override
        public String toString() {
            return String.format("%s: $%.2f", date, salesAmount);
        }
    }

    /**
     * Sorts an array of SaleTransactions by sales amount (ascending order) using manual Merge Sort.
     * @param arr Array of transactions.
     */
    public static void sort(SaleTransaction[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(SaleTransaction[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Divide-and-conquer: recursively sort left and right halves
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            // Combine/merge sorted halves
            merge(arr, left, mid, right);
        }
    }

    private static void merge(SaleTransaction[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Allocate temporary tracking arrays
        SaleTransaction[] leftArr = new SaleTransaction[n1];
        SaleTransaction[] rightArr = new SaleTransaction[n2];

        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0, k = left;

        // Compare elements and merge in sorted order
        while (i < n1 && j < n2) {
            if (leftArr[i].getSalesAmount() <= rightArr[j].getSalesAmount()) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArr, if any
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        // Copy remaining elements of rightArr, if any
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
}
