package com.sumo.controllers;

import com.sumo.model.dashboard.DashboardVO;
import com.sumo.services.DashboardService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboards")
@Log
public class DashboardController {
	@Autowired
	DashboardService dashboardService;

	@RequestMapping
	public ResponseEntity getDashboardInfo(@RequestHeader("username") String username, @RequestHeader("password") String password) {
		log.info("getting dashboard info");
		List<DashboardVO> dashboardInfo = dashboardService.getDashboardInfo(username, password);
		return new ResponseEntity<>(dashboardInfo, HttpStatus.OK);
	}

	@RequestMapping("/monitor/{id}")
	public ResponseEntity getQueryByMonitorId(@PathVariable("id") long id) {
		log.info("getting query by monitor id: " + id);
		String queryByMonitorId = dashboardService.getQueryByMonitorId(id);
		return new ResponseEntity<>(queryByMonitorId, HttpStatus.OK);
	}
}