package com.sumo.model.dashboard;

import com.sumologic.client.dashboard.model.Dashboard;
import lombok.Data;

import java.util.List;

/**
 * @author ddctgregory
 * @since 6/4/16
 */
@Data
public class DashboardVO {
	private String title;
	private long id;
	private List<Monitor> monitors;

	public DashboardVO(Dashboard dashboard) {
		this.title = dashboard.getTitle();
		this.id = dashboard.getId();
	}
}
