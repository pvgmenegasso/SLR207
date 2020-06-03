package slave;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class poormapping {
	
		Map<String, Integer> mapper(Map<String, Integer> map, String word)
		{
			map.put(word, 1) ;
			
			return map ;
		}
}
