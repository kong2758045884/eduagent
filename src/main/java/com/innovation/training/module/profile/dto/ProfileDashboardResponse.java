package com.innovation.training.module.profile.dto;

import com.innovation.training.module.growth.dto.GrowthEventResponse;
import com.innovation.training.module.user.dto.UserResponse;

import java.util.List;
import java.util.Map;

public class ProfileDashboardResponse {

    private UserResponse user;
    private Map<String, Long> counters;
    private List<GrowthEventResponse> recentEvents;

    public ProfileDashboardResponse() {
    }

    public ProfileDashboardResponse(UserResponse user, Map<String, Long> counters,
                                    List<GrowthEventResponse> recentEvents) {
        this.user = user;
        this.counters = counters;
        this.recentEvents = recentEvents;
    }

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
    public Map<String, Long> getCounters() { return counters; }
    public void setCounters(Map<String, Long> counters) { this.counters = counters; }
    public List<GrowthEventResponse> getRecentEvents() { return recentEvents; }
    public void setRecentEvents(List<GrowthEventResponse> recentEvents) { this.recentEvents = recentEvents; }
}
