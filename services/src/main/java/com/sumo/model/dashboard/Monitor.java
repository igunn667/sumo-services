package com.sumo.model.dashboard;

import com.sumologic.client.dashboard.model.DashboardMonitor;
import lombok.Data;

/**
 * @author ddctgregory
 * @since 6/4/16
 */
@Data
public class Monitor {
	private String title;
	private long id;
	private String query;

	public Monitor(DashboardMonitor monitor) {
		this.title = monitor.getTitle();
		this.id = monitor.getId();
		this.query = monitor.getQueryString();
	}
}
