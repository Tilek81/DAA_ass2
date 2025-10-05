package algorithms;

import java.util.ArrayList;
import java.util.List;


public class ShellSort {

    public enum GapSequence {
        SHELL, KNUTH, SEDGEWICK
    }

    // Метрики производительности
    private long comparisons;
    private long swaps;
    private long arrayAccesses;

    public ShellSort() {
        resetMetrics();
    }

    /**
     * Основной метод сортировки
     */
    public void sort(int[] array, GapSequence sequence) {
        if (array == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        if (array.length <= 1) {
            return;
        }

        resetMetrics();

        int[] gaps = generateGaps(array.length, sequence);

        for (int i = gaps.length - 1; i >= 0; i--) {
            int gap = gaps[i];
            gapInsertionSort(array, gap);
        }
    }


    private void gapInsertionSort(int[] array, int gap) {
        for (int i = gap; i < array.length; i++) {
            int temp = array[i];
            arrayAccesses++;

            int j;
            for (j = i; j >= gap; j -= gap) {
                comparisons++;
                arrayAccesses += 2;
                if (array[j - gap] > temp) {
                    array[j] = array[j - gap];
                    arrayAccesses += 2;
                } else {
                    break;
                }
            }

            if (j != i) {
                array[j] = temp;
                arrayAccesses++;
                swaps++;
            }
        }
    }


    private int[] generateGaps(int n, GapSequence sequence) {
        switch (sequence) {
            case SHELL:
                return generateShellGaps(n);
            case KNUTH:
                return generateKnuthGaps(n);
            case SEDGEWICK:
                return generateSedgewickGaps(n);
            default:
                return generateShellGaps(n);
        }
    }

    private int[] generateShellGaps(int n) {
        List<Integer> gaps = new ArrayList<>();
        int gap = n / 2;

        while (gap > 0) {
            gaps.add(gap);
            gap /= 2;
        }

        return gaps.stream().mapToInt(Integer::intValue).toArray();
    }


    private int[] generateKnuthGaps(int n) {
        List<Integer> gaps = new ArrayList<>();
        int k = 1;
        int gap;

        do {
            gap = (int)((Math.pow(3, k) - 1) / 2);
            if (gap <= Math.ceil(n / 3)) {
                gaps.add(gap);
            }
            k++;
        } while (gap <= Math.ceil(n / 3));

        return reverseList(gaps);
    }


    private int[] generateSedgewickGaps(int n) {
        List<Integer> gaps = new ArrayList<>();
        int k = 0;
        int gap;

        while (true) {
            if (k % 2 == 0) {
                gap = (int)(9 * (Math.pow(2, k) - Math.pow(2, k/2)) + 1);
            } else {
                gap = (int)(8 * Math.pow(2, k) - 6 * Math.pow(2, (k+1)/2) + 1);
            }

            if (gap > n) break;

            gaps.add(gap);
            k++;
        }

        return reverseList(gaps);
    }


    private int[] reverseList(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(list.size() - 1 - i);
        }
        return result;
    }

    public void resetMetrics() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
    }

    // Геттеры для метрик
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }


    public static String getSequenceName(GapSequence sequence) {
        switch (sequence) {
            case SHELL: return "Shell's Original";
            case KNUTH: return "Knuth's";
            case SEDGEWICK: return "Sedgewick's";
            default: return "Unknown";
        }
    }
}