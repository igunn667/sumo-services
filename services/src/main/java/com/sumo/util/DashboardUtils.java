package com.sumo.util;

import com.sumo.model.dashboard.DashboardVO;
import com.sumo.model.dashboard.Monitor;
import com.sumologic.client.dashboard.model.GetDashboardsResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ddctgregory
 * @since 6/4/16
 */
@Component
public class DashboardUtils {

	public List<DashboardVO> buildDashboards(GetDashboardsResponse dashboardsResponse) {
		List<DashboardVO> dashboardVOs = new ArrayList<>();
		dashboardsResponse.getDashboards().parallelStream().forEach(dashboard -> {
			DashboardVO dashboardVO = new DashboardVO(dashboard);
			List<Monitor> monitors = new ArrayList<>();
			dashboard.getDashboardMonitors().parallelStream().forEach(dashboardMonitor -> {
				Monitor monitor = new Monitor(dashboardMonitor);
				monitors.add(monitor);
			});
			dashboardVO.setMonitors(monitors);
			dashboardVOs.add(dashboardVO);
		});
		return dashboardVOs;
	}
}