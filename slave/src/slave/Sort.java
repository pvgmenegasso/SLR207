package slave;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Sort {
	
	public static LinkedHashMap<String, Integer> sort(LinkedHashMap<String, Integer> words)
	{
		return words.entrySet().stream()
				.sorted(HashMap.Entry.comparingByKey())
				.sorted(HashMap.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldvalue, newvalue) -> oldvalue, LinkedHashMap::new)) ;
	}

}
