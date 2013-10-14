package igs.swnm.dao;

import java.util.List;
import java.util.Map;

public interface SwnmDao {

	// area
	public List<Map<?, ?>> selectAllAreas();

	public List<Map<?, ?>> selectRootAreas();

	public Map<?, ?> selectAreaById(String id);

	public List<Map<?, ?>> selectAreasByParentId(String parentId);

	public int selectChildrenCountByAreaId(String areaId);

	public void insertArea(Map<?, ?> paramMap);

	public void updateArea(Map<?, ?> paramMap);

	public void deleteArea(String areaId);

	// swc
	public List<Map<?, ?>> selectAllSwcs();

	public List<Map<?, ?>> selectSwcsByAreaId(Map<?, ?> paramMap);

	public Map<?, ?> selectSwcById(String swcId);

	public void insertSwc(Map<?, ?> paramMap);

	public void updateSwc(Map<?, ?> paramMap);

	public void deleteSwcById(String swcId);

	public void clearNmSwcBase(Map<?, ?> paramMap);

	// relays

	public List<Map<?, ?>> selectRelaysBySwcId(String swcId);

	public Map<?, ?> selectRelaysById(String relaysId);

	public void insertRelays(Map<?, ?> paramMap);

	public void updateRelays(Map<?, ?> paramMap);

	public void deleteRelaysById(String relaysId);

	public void deleteRelaysBySwcId(String swcId);

	// x
	public void insertXSwcCtg(Map<?, ?> paramMap);

	public void updateXSwcCtg(Map<?, ?> paramMap);

	public void deleteXSwcCtgBySwcId(String swcId);

	public void deleteXSwcCtgByCtgId(String ctgId);

	public void deleteXSwcCtgById(String id);

	// q:swc:new
	public void insertNmSwcBaseBySwcId(String swcId);

	public void insertNmRelBaseByRelId(Map<?, ?> paramMap);
}
