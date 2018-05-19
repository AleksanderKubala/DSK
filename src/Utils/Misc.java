package Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Misc {

    public static void getSubsets(List<Integer> superSet, int subsetSize, int startingIndex, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == subsetSize) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (startingIndex == superSet.size()) return;
        Integer x = superSet.get(startingIndex);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, subsetSize, startingIndex+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, subsetSize, startingIndex+1, current, solution);
    }
}
