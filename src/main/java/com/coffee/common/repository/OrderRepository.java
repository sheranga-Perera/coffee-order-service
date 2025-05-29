package com.coffee.common.repository;

import com.coffee.common.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {
     /**
     * Find orders in a queue with position greater than the specified position
     *
     * @param queueId Queue ID
     * @param position Queue position
     * @return List of orders
     */
    @Query("SELECT o " +
            "FROM Orders o " +
            "WHERE o.queue.id = :queueId AND o.queuePosition > :position " +
            "ORDER BY o.queuePosition")
    List<Orders> findByQueueIdAndQueuePositionGreaterThan(@Param("queueId") UUID queueId, @Param("position") int position);
}
