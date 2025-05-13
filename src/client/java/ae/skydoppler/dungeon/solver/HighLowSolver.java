package ae.skydoppler.dungeon.solver;

import java.util.*;

public class HighLowSolver {
    private Map<Short, Integer> data;

    public HighLowSolver(Map<Short, Integer> data) {
        this.data = new HashMap<>(data);
    }

    public void quicksort() {
        List<Map.Entry<Short, Integer>> entries = new ArrayList<>(data.entrySet());
        quicksort(entries, 0, entries.size() - 1);
        data.clear();
        for (Map.Entry<Short, Integer> entry : entries) {
            data.put(entry.getKey(), entry.getValue());
        }
    }

    private void quicksort(List<Map.Entry<Short, Integer>> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quicksort(list, low, pivotIndex - 1);
            quicksort(list, pivotIndex + 1, high);
        }
    }

    private int partition(List<Map.Entry<Short, Integer>> list, int low, int high) {
        int pivot = list.get(high).getValue();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public Map<Short, Integer> getSortedData() {
        return data;
    }

    public static void main(String[] args) {
        Map<Short, Integer> healthData = new HashMap<>();
        healthData.put((short) 1, 75);
        healthData.put((short) 2, 50);
        healthData.put((short) 3, 90);
        healthData.put((short) 4, 30);

        HighLowSolver solver = new HighLowSolver(healthData);
        solver.quicksort();
        System.out.println("Sorted data: " + solver.getSortedData());
    }
}
