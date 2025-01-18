package day23;

import java.util.*;

public class Connected {

    private final List<String> asList;
    private final Set<String> asSet;

    public Connected(List<String> computers) {
        var sorted = new ArrayList<>(computers);
        Collections.sort(sorted);
        asList = Collections.unmodifiableList(sorted);
        asSet = new HashSet<>(computers);
    }

    public boolean contains(String computer) {
        return asSet.contains(computer);
    }

    public String get(int index) {
        return asList.get(index);
    }

    public List<String> getAll() {
        return asList;
    }
}
