package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellSort {


    public enum GapSequence {
        SHELL,
        KNUTH,
        SEDGEWICK
    }


    public static void sort(double[] arr, GapSequence sequence) {
        if (arr == null || arr.length < 2) return;

        List<Integer> gaps = getGaps(arr.length, sequence);

        // Iterate through the generated gaps in descending order
        for (int gap : gaps) {

            for (int i = gap; i < arr.length; i++) {
                double temp = arr[i];
                int j = i;

                // Compare element arr[i] with the element gap positions behind it
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                // Place the current element (temp) into its correct position
                arr[j] = temp;
            }
        }
    }


    private static List<Integer> getGaps(int n, GapSequence type) {
        List<Integer> gaps = new ArrayList<>();
        int h = 1;

        switch (type) {
            case SHELL:
                // Shell's original sequence: h = n/2, n/4, ..., 1
                h = n / 2;
                while (h >= 1) {
                    gaps.add(h);
                    h /= 2;
                }
                break;

            case KNUTH:
                // Knuth's sequence: h = (3^k - 1) / 2, ensuring h < n/3.
                h = 1;
                while (h < n / 3) {
                    h = 3 * h + 1;
                }
                while (h >= 1) {
                    gaps.add(h);
                    h /= 3;
                }
                break;

            case SEDGEWICK:

                // formula 9*4^i - 9*2^i + 1 or 4^i - 3*2^i + 1

                h = 1;
                int k = 0;
                while (h < n) {
                    gaps.add(h);
                    k++;
                    // Sedgewick's combined formula: 4^(k+1) + 3*2^k + 1 (simplified)
                    if (k % 2 == 0) {
                        h = (int) (9 * (Math.pow(4, k / 2) - Math.pow(2, k / 2)) + 1);
                    } else {
                        h = (int) (8 * Math.pow(2, k / 2) - 6 * Math.pow(2, k / 2) + 1);
                    }

                }

                gaps.clear();
                k = 0;
                h = 1;
                while (h < n) {
                    gaps.add(h);
                    k++;
                    if (k % 2 == 0) {
                        // 9 * 4^i - 9 * 2^i + 1
                        h = (int) (9 * (Math.pow(4, k / 2) - Math.pow(2, k / 2)) + 1);
                    } else {
                        // 4^i - 3 * 2^i + 1
                        h = (int) (Math.pow(4, k / 2 + 1) + 3 * Math.pow(2, k / 2) + 1);
                    }
                }
                // Gaps are generated in ascending order, so reverse them for the sort loop
                java.util.Collections.reverse(gaps);

                // Remove any gaps that are larger than n
                gaps.removeIf(gap -> gap >= n);
                break;
        }

        // Ensure the last gap is 1
        if (gaps.isEmpty() || gaps.get(gaps.size() - 1) != 1) {
            if (!gaps.isEmpty()) gaps.removeIf(gap -> gap == 1);
            gaps.add(1);
        }

        return gaps;
    }


    public static void main(String[] args) {
        double[] original = {80.5, 64.1, 65.3, 70.0, 50.9, 30.2, 99.8, 12.3, 45.6, 88.7, 10.1, 55.4, 76.9, 21.0, 33.3, 67.2, 90.5, 11.2, 44.4, 25.5, 78.8};

        // --- Test 1: Shell's Sequence ---
        double[] arrShell = Arrays.copyOf(original, original.length);
        long start = System.nanoTime();
        sort(arrShell, GapSequence.SHELL);
        long timeShell = System.nanoTime() - start;
        System.out.println("--- Shell's Sequence (n/2, n/4, ...) ---");
        System.out.println("Sorted: " + Arrays.toString(arrShell));
        System.out.printf("Time: %.3f ms\n", timeShell / 1_000_000.0);
        System.out.println("Gaps Used: " + getGaps(original.length, GapSequence.SHELL));

        // --- Test 2: Knuth's Sequence ---
        double[] arrKnuth = Arrays.copyOf(original, original.length);
        start = System.nanoTime();
        sort(arrKnuth, GapSequence.KNUTH);
        long timeKnuth = System.nanoTime() - start;
        System.out.println("\n--- Knuth's Sequence (3k + 1) / 2 ---");
        System.out.println("Sorted: " + Arrays.toString(arrKnuth));
        System.out.printf("Time: %.3f ms\n", timeKnuth / 1_000_000.0);
        System.out.println("Gaps Used: " + getGaps(original.length, GapSequence.KNUTH));

        // --- Test 3: Sedgewick's Sequence ---
        double[] arrSedgewick = Arrays.copyOf(original, original.length);
        start = System.nanoTime();
        sort(arrSedgewick, GapSequence.SEDGEWICK);
        long timeSedgewick = System.nanoTime() - start;
        System.out.println("\n--- Sedgewick's Sequence (Optimized) ---");
        System.out.println("Sorted: " + Arrays.toString(arrSedgewick));
        System.out.printf("Time: %.3f ms\n", timeSedgewick / 1_000_000.0);
        System.out.println("Gaps Used: " + getGaps(original.length, GapSequence.SEDGEWICK));
    }
}