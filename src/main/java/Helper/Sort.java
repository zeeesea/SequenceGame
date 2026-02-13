package Helper;

/**
 * Sort utility class that provides efficient sorting for Lists containing Comparable elements.
 * Uses MergeSort .
 */
public class Sort {

    /**
     * Sorts a list of Comparable elements in descending order (highest first).
     * Uses MergeSort.
     *
     * @param list The list to sort
     * @param <T> The type extending Comparable
     * @return A new sorted list (original list is not modified)
     */
    public static <T extends Comparable> List<T> sort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }

        try {
            Object[] array = list.toArrayList().toArray();
            mergeSort(array, 0, array.length - 1);

            List<T> sorted = new List<>();
            for (Object obj : array) {
                sorted.append((T) obj);
            }
            return sorted;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }
    private static void mergeSort(Object[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            merge(array, left, mid, right);
        }
    }
    private static void merge(Object[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Object[] leftArray = new Object[n1];
        Object[] rightArray = new Object[n2];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            Comparable leftItem = (Comparable) leftArray[i];
            Comparable rightItem = (Comparable) rightArray[j];

            if (leftItem.compareTo(rightItem) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
