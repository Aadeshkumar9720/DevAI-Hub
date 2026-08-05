package com.devaihub.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private long totalProjects;

    private long completedProjects;

    private long totalTasks;

    private long completedTasks;

    private long todoTasks;

    private long inProgressTasks;
}
