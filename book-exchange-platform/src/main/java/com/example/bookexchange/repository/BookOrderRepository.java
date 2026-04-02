package com.example.bookexchange.repository;

import com.example.bookexchange.entity.BookOrder;
import com.example.bookexchange.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
	List<BookOrder> findAllByOrderByOrderedAtDesc();
	List<BookOrder> findAllByBuyer_IdOrderByOrderedAtDesc(Long buyerId);
	boolean existsByBook_IdAndStatusIn(Long bookId, Collection<OrderStatus> statuses);
	boolean existsByBuyer_IdAndBook_IdAndStatusIn(Long buyerId, Long bookId, Collection<OrderStatus> statuses);

	@Query("select o.book.id from BookOrder o where o.buyer.id = :buyerId and o.status in :statuses")
	List<Long> findBookIdsByBuyerAndStatusIn(@Param("buyerId") Long buyerId, @Param("statuses") Collection<OrderStatus> statuses);
}
