package igs.swnm.controller;

import igs.swnm.dao.SwnmDao;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

//@Controller
//@RequestMapping(value = "/relays")
public class RelaysController {

	// @Autowired
	private SwnmDao swnmDao;

	// @ResponseBody
	// @RequestMapping(method = RequestMethod.GET, value = "/{relaysId}")
	public Map<?, ?> getRelaysById(@PathVariable String relaysId) {
		return swnmDao.selectRelaysById(relaysId);
	}
}
