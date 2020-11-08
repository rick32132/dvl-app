package com.dvlcube.app.rest;

import static com.dvlcube.app.manager.data.e.Menu.MONITORING;
import static com.dvlcube.utils.query.MxQuery.$;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dvlcube.app.dto.StatDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvlcube.app.interfaces.MenuItem;
import com.dvlcube.utils.aspects.stats.Stat;
import com.dvlcube.utils.aspects.stats.Stats;
import com.dvlcube.utils.interfaces.MxService;

/**
 * Application performance stats.
 * 
 * @since 17 de abr de 2019
 * @author Ulisses Lima
 */
@RestController
@MenuItem(value = MONITORING, readOnly = true)
@RequestMapping("${dvl.rest.prefix}/stats")
public class StatService implements MxService {
	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * @param params
	 * @return stats
	 * @since 17 de abr de 2019
	 * @author Ulisses Lima
	 */
	@GetMapping

	public List<StatDto> get(@RequestParam Map<String, String> params) {
		List<Stat> values = Stats.values();
		values.sort(new Comparator<Stat>() {
			@Override
			public int compare(Stat o1, Stat o2) {
				if(o1.getTotal().equals(o2.getTotal())) {
					if(o1.avg().equals(o2.avg())){
						return 0;
					}
					return (o1.avg() < o2.avg() ? 1 : -1);
				}
				return (o1.getTotal() < o2.getTotal() ? 1 : -1);
			}
		});

		return values.stream().map(stat -> new StatDto(
				stat.getAction(),
				stat.getMin(),
				stat.getMax(),
				stat.getTotal(),
				stat.getCount(),
				stat.getErrors(),
				stat.savg()))
				.collect(Collectors.toList());
	}

	/**
	 * @param params
	 * @return stats
	 * @since 17 de abr de 2019
	 * @author Ulisses Lima
	 */
	@DeleteMapping
	public List<StatDto> delete(@RequestParam Map<String, String> params) {
		Stats.consume(item -> log.info("removing {}", item));
		return get(params);
	}

	/**
	 * @param id
	 * @return stat by action
	 * @since 17 de abr de 2019
	 * @author Ulisses Lima
	 */
	@GetMapping("/{id}")
	public Stat get(@PathVariable String id) {
		return $(Stats.values()).filterOne(stat -> stat.getAction().equals(id));
	}

	/**
	 * @param regex expression
	 * @return stats that match the regex
	 * @since 17 de abr de 2019
	 * @author Ulisses Lima
	 */
	@GetMapping("/matching/{regex}")
	public Collection<Stat> getMatching(@PathVariable String regex) {
		return $(Stats.values()).filter(stat -> stat.getAction().matches(regex)).o;
	}

	/**
	 * @param params
	 * @param id
	 * @return Iterable<Stat> stats like id, fallback to GET params
	 * @since 2 de mai de 2019
	 * @author Ulisses Lima
	 */
	@GetMapping("/like")
	public Iterable<StatDto> getLike(@RequestParam Map<String, String> params, @RequestParam(required = true) String id) {
		if ($(id).isBlank())
			return get(params);

		return $(Stats.values().stream().map(stat -> new StatDto(
				stat.getAction(),
				stat.getMin(),
				stat.getMax(),
				stat.getTotal(),
				stat.getCount(),
				stat.getErrors(),
				stat.savg()))
				.collect(Collectors.toList())).filter(stat -> stat.getAction().toLowerCase().contains(id.toLowerCase())).o;
	}
}
