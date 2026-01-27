package com.CryptoProject.CryptoInfosys.service;

import com.CryptoProject.CryptoInfosys.dto.PriceHistoryResponse;
import com.CryptoProject.CryptoInfosys.model.PriceSnapshot;
import com.CryptoProject.CryptoInfosys.repository.PriceSnapshotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceSnapshotService {

    private final PriceSnapshotRepository repository;

    public PriceSnapshotService(PriceSnapshotRepository repository) {
        this.repository = repository;
    }

    public List<PriceHistoryResponse> getPriceHistory(String assetSymbol) {

        List<PriceSnapshot> snapshots =
                repository.findByAssetSymbolOrderByCapturedAtAsc(
                        assetSymbol.toUpperCase()
                );

        return snapshots.stream()
                .map(s ->
                    new PriceHistoryResponse(
                        s.getPrice(),
                        s.getCapturedAt()
                    )
                )
                .collect(Collectors.toList());
    }
    public Map<String, List<PriceHistoryResponse>> getAllPriceHistory() {

        List<PriceSnapshot> snapshots =
                repository.findAllByOrderByCapturedAtAsc();

        Map<String, List<PriceHistoryResponse>> result = new LinkedHashMap<>();

        for (PriceSnapshot s : snapshots) {
            result
                .computeIfAbsent(
                    s.getAssetSymbol(),
                    k -> new ArrayList<>()
                )
                .add(
                    new PriceHistoryResponse(
                        s.getPrice(),
                        s.getCapturedAt()
                    )
                );
        }

        return result;
    }

    public List<PriceHistoryResponse> getPriceHistoryByRange(
            String assetSymbol,
            String range
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;

        switch (range.toUpperCase()) {
            case "1D":
                start = now.minusDays(1);
                break;
            case "7D":
                start = now.minusDays(7);
                break;
            case "1M":
                start = now.minusMonths(1);
                break;
            default:
                start = now.minusDays(1);
        }

        List<PriceSnapshot> snapshots =
            repository.findByAssetSymbolAndCapturedAtBetween(
                assetSymbol.toUpperCase(),
                start,
                now
            );

        return snapshots.stream()
            .map(s ->
                new PriceHistoryResponse(
                    s.getPrice(),
                    s.getCapturedAt()
                )
            )
            .toList();
    }

}
