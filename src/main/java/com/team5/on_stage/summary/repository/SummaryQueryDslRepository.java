package com.team5.on_stage.summary.repository;

import com.team5.on_stage.summary.dto.SummaryResponseDTO;
import com.team5.on_stage.summary.entity.Summary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SummaryQueryDslRepository {
    void softDeleteByUsername(String username);
    List<Summary> getSummaryByUsername(String username, Pageable pageable);
    long countSummaryByUsername(String username);
    List<String> findUsernamesWithOldSummaries(LocalDateTime timeToCompare);
}
