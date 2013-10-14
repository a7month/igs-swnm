package igs.swnm.controller;

import igs.swnm.dao.SwnmDao;
import igs.swnm.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/area")
public class AreasController {

	@Autowired
	private SwnmDao swnmDao;

	public AreasController() {

	}

	/**
	 * 获取所有的区域
	 * 
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public List<Map<?, ?>> getAreas() {
		List<Map<?, ?>> l = swnmDao.selectRootAreas();
		for (int i = 0; i < l.size(); i++) {
			((Map<String, Object>) l.get(i)).put("parent_id", "");
		}
		return l;
	}

	/**
	 * 根据区域Id获取区域信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}")
	@ResponseBody
	public Map<?, ?> getAreasById(@PathVariable String areaId) {
		Map<String, Object> areaMap = (Map<String, Object>) swnmDao
				.selectAreaById(areaId);
		if (!areaMap.containsKey(Constants.T_HW_SWC_COLUMN_PARENTID)) {
			areaMap.put(Constants.T_HW_SWC_COLUMN_PARENTID, "");
		}
		List<?> children = swnmDao.selectAreasByParentId(areaId);
		areaMap.put(Constants.JSON_HW_SWC_PROP_CHILDREN,
				swnmDao.selectAreasByParentId(areaId));
		return areaMap;
	}

	/**
	 * 添加区域信息
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "")
	@ResponseBody
	public boolean addArea(@RequestBody Map<?, ?> paramMap) {
		swnmDao.insertArea(paramMap);
		return true;
	}

	/**
	 * 修改区域信息
	 * 
	 * @param body
	 * @param areaId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "{areaId}")
	public boolean modifyArea(@RequestBody Map<String, Object> paramMap,
			@PathVariable String areaId) {
		paramMap.put("id", areaId);
		swnmDao.updateArea(paramMap);
		return true;
	}

	/**
	 * 删除区域信息
	 * 
	 * @param areaId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "{areaId}")
	public boolean deleteArea(@PathVariable String areaId) {
		swnmDao.deleteArea(areaId);
		return true;
	}

	/**
	 * 获取区域下所有开关
	 * 
	 * @param areaId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}/swc")
	public List<Map<?, ?>> getSwcsByAreaId(@PathVariable String areaId,
			@RequestParam("offset") String offset,
			@RequestParam("limit") String limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaId", areaId);
		if (limit != null) {
			map.put("limit", Integer.parseInt(limit));
		}
		if (offset != null) {
			map.put("offset", Integer.parseInt(offset));
		}
		return swnmDao.selectSwcsByAreaId(map);
	}

	/**
	 * 根据swcId获取开关
	 * 
	 * @param areaId
	 * @param swcId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}/swc/{swcId}")
	public Map<?, ?> getSwcsById(@PathVariable String areaId,
			@PathVariable String swcId) {
		return swnmDao.selectSwcById(swcId);
	}

	/**
	 * 添加开关（关联开关到区域）
	 * 
	 * @param paramMap
	 * @param areaId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{areaId}/swc")
	public boolean addSwc(@RequestBody Map<String, Object> paramMap,
			@PathVariable String areaId) {
		paramMap.put("id", UUID.randomUUID().toString());
		paramMap.put("ctg_id", areaId);
		swnmDao.insertXSwcCtg(paramMap);
		return true;
	}

	/**
	 * 更新开关(更新开关到其他区域/更新开关的网管信息）
	 * 
	 * @param paramMap
	 * @param areaId
	 * @param swcId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{areaId}/swc/{swcId}")
	public boolean modifySwc(
			@RequestBody Map<String, Object> paramMap,
			@PathVariable String areaId,
			@PathVariable String swcId,
			@RequestParam(value = "operation", defaultValue = "move") String operation) {
		switch(operation){
		case "move":
			paramMap.put("swc_id", swcId);
			swnmDao.updateXSwcCtg(paramMap);
			break;
		case "clear":
			paramMap.put("swc_id", swcId);
			swnmDao.clearNmSwcBase(paramMap);
		}
		
		return true;
	}

	/**
	 * 删除开关,删除开关与ctg的关联关系
	 * 
	 * @param paramMap
	 * @param areaId
	 * @param swcId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{areaId}/swc/{swcId}")
	public boolean deleteSwc(@RequestBody Map<String, Object> paramMap,
			@PathVariable String areaId, @PathVariable String swcId) {
		swnmDao.deleteXSwcCtgBySwcId(swcId);
		return true;
	}

	/**
	 * 根据swcid获取继电器
	 * 
	 * @param areaId
	 * @param swcId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}/swc/{swcId}/relays")
	public List<Map<?, ?>> getRelaysBySwcId(@PathVariable String areaId,
			@PathVariable String swcId) {
		return swnmDao.selectRelaysBySwcId(swcId);
	}

	/**
	 * 根据id获取relays
	 * 
	 * @param areaId
	 * @param swcId
	 * @param relaysId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{areaId}/swc/{swcId}/relays/{relaysId}")
	public Map<?, ?> getRelaysById(@PathVariable String areaId,
			@PathVariable String swcId, @PathVariable String relaysId) {
		return swnmDao.selectRelaysById(relaysId);
	}

	/**
	 * 
	 * @param paramMap
	 * @param areaId
	 * @param swcId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{areaId}/swc/{swcId}/relays")
	public boolean addRelays(@RequestBody Map<String, Object> paramMap,
			@PathVariable String areaId, @PathVariable String swcId) {
		paramMap.put("swcId", swcId);
		swnmDao.insertRelays(paramMap);
		return true;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{areaId}/swc/{swcId}/relays/{relaysId}")
	public boolean modifyRelays(@RequestBody Map<String, Object> paramMap,
			@PathVariable String areaId, @PathVariable String swcId,
			@PathVariable String relaysId) {
		paramMap.put("relaysId", relaysId);
		swnmDao.updateRelays(paramMap);
		return true;
	}

	@ExceptionHandler(DataAccessException.class)
	public String handleDataAccessException(DataAccessException exception,
			HttpServletRequest request) {
		return exception.getMessage();
	}

	@ExceptionHandler(NullPointerException.class)
	public String handleNullPointerException(NullPointerException exception) {
		return "null";
	}
}
