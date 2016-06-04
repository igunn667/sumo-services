package com.sumo.rest;

import com.sumo.model.dashboard.DashboardVO;
import com.sumo.util.DashboardUtils;
import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.dashboard.model.GetDashboardsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "singleton")
public class DashboardService extends SumoRestHelper {
	@Autowired
	DashboardUtils dashboardUtils;

	private Map<Long, DashboardVO> dashboardVOMap;

	public DashboardService() {
		this.dashboardVOMap = new HashMap<>();
	}

	@Cacheable("dashboardCache")
	public List<DashboardVO> getDashboardInfo(String hashedUsername, String hashedPassword) {
		SumoLogicClient sumoLogicClient = new SumoLogicClient(base64Decode(hashedUsername), base64Decode(hashedPassword));
		GetDashboardsResponse dashboards = sumoLogicClient.getDashboards(true);
		List<DashboardVO> dashboardVOs = dashboardUtils.buildDashboards(dashboards);
		for (DashboardVO dashboardVO : dashboardVOs) {
			dashboardVOMap.put(dashboardVO.getId(), dashboardVO);
		}
		return dashboardVOs;
	}

	public String getQueryByMonitorId(long monitorId) {
		final String[] returnQuery = new String[1];
		dashboardVOMap.forEach((aLong, dashboardVO) -> dashboardVO.getMonitors().parallelStream().forEach(monitor -> {
			if (monitor.getId() == monitorId) {
				returnQuery[0] = monitor.getQuery();
			}
		}));
		return returnQuery[0];
	}
}