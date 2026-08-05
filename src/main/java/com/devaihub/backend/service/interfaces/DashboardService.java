package com.devaihub.backend.service.interfaces;

import com.devaihub.backend.response.DashboardStatsResponse;

public interface DashboardService {

    DashboardStatsResponse getDashboardStats(String username);
}
