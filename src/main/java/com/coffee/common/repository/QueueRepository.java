package com.coffee.common.repository;

import com.coffee.common.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueueRepository extends JpaRepository<Queue, UUID> {

    /**
     * This method returns the queue with the smallest current size
     *
     *
     * @param shopId shop Id
     * @return Optional<Queue>
     */
    @Query("""
        SELECT q FROM Queue q
        WHERE q.shopId = :shopId
        AND q.status = 'ACTIVE'
        AND q.currentSize < q.maxSize
        ORDER BY q.currentSize ASC
        LIMIT 1
    """)
    Optional<Queue> findFirstAvailableQueue(@Param("shopId") UUID shopId);

    /**
     * This method finds all active queues for a shop
     *
     * @param shopId shop Id
     * @return List of queues
     */
    @Query("SELECT q FROM Queue q WHERE q.shopId = :shopId AND q.status = 'ACTIVE'")
    List<Queue> findActiveQueuesByShopId(@Param("shopId") UUID shopId);
}
