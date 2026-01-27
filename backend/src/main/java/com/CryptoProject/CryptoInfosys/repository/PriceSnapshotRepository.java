package com.CryptoProject.CryptoInfosys.repository;

import com.CryptoProject.CryptoInfosys.model.PriceSnapshot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceSnapshotRepository
        extends JpaRepository<PriceSnapshot, Long> {

    // Get price history for an asset
    List<PriceSnapshot> findByAssetSymbolOrderByCapturedAtAsc(String assetSymbol);

    // Get snapshots within a time range
    List<PriceSnapshot> findByAssetSymbolAndCapturedAtBetween(
            String assetSymbol,
            LocalDateTime start,
            LocalDateTime end
    );
    List<PriceSnapshot> findAllByOrderByCapturedAtAsc();
    @Query("""
            SELECT p FROM PriceSnapshot p
            WHERE p.capturedAt = (
                SELECT MAX(ps.capturedAt)
                FROM PriceSnapshot ps
                WHERE ps.assetSymbol = p.assetSymbol
            )
        """)
        List<PriceSnapshot> findLatestPrices();
    @Query("""
    	    SELECT p.price
    	    FROM PriceSnapshot p
    	    WHERE p.assetSymbol = :asset
    	    ORDER BY p.capturedAt DESC
    	""")
    	List<Double> findRecentPrices(
    	        @Param("asset") String asset,
    	        Pageable pageable
    	);
    @Query("""
    	    SELECT p
    	    FROM PriceSnapshot p
    	    WHERE p.assetSymbol = :asset
    	      AND p.capturedAt <= :time
    	    ORDER BY p.capturedAt DESC
    	""")
    	List<PriceSnapshot> findPriceBefore(
    	        @Param("asset") String asset,
    	        @Param("time") LocalDateTime time,
    	        Pageable pageable
    	);

}
