package com.sumo.rest;

import com.sumo.model.dashboard.DashboardVO;
import com.sumo.model.dashboard.Monitor;
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

	private Map<Long, Monitor> monitorMap;

	public DashboardService() {
		this.monitorMap = new HashMap<>();
	}

	@Cacheable("dashboardCache")
	public List<DashboardVO> getDashboardInfo(String hashedUsername, String hashedPassword) {
		SumoLogicClient sumoLogicClient = new SumoLogicClient(base64Decode(hashedUsername), base64Decode(hashedPassword));
		GetDashboardsResponse dashboards = sumoLogicClient.getDashboards(true);
		List<DashboardVO> dashboardVOs = dashboardUtils.buildDashboards(dashboards);
		for (DashboardVO dashboardVO : dashboardVOs) {
			for (Monitor monitor : dashboardVO.getMonitors()) {
				monitorMap.put(monitor.getId(), monitor);
			}
		}
		return dashboardVOs;
	}

	public String getQueryByMonitorId(long monitorId) {
		return monitorMap.get(monitorId).getQuery();
	}
}