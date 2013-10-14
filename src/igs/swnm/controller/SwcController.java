package igs.swnm.controller;

import igs.swnm.dao.SwnmDao;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
//@RequestMapping(value = "/swc")
public class SwcController {

	// @Autowired
	private SwnmDao swnmDao;

	public SwcController() {

	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Map<?, ?>> getAllSwcs() {
		return swnmDao.selectAllSwcs();
	}

	/**
	 * 根据SwcId获取swc
	 * 
	 * @param areaId
	 * @param swcId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{swcId}")
	public @ResponseBody
	Map<?, ?> getSwcBySwcId(@PathVariable String swcId) {
		return swnmDao.selectSwcById(swcId);
	}

	/**
	 * 获取swc下所有继电器
	 * 
	 * @param swcId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{swcId}/relays")
	public List<Map<?, ?>> getRelaysBySwcId(@PathVariable String swcId) {
		return swnmDao.selectRelaysBySwcId(swcId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{swcId}/relays/{relaysId}")
	public String getRelaysById(@PathVariable String swcId,
			@PathVariable String relaysId) {
		return "/relays/" + relaysId;
	}
}
