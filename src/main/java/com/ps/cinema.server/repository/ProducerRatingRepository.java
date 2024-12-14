package com.ps.cinema.server.repository;

import com.ps.cinema.server.model.ProducerRating;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRatingRepository extends JpaRepository<ProducerRating, Integer> {

    @Query(value = "SELECT * FROM producer_rating WHERE producer_id = :producerId AND viewer_id = :viewerId", nativeQuery = true)
    public Optional<ProducerRating> findByProducerIdAndViewerId(@Param("producerId")int producerId,@Param("viewerId") int viewerId);

    @Transactional
    public void deleteAllByProducerId(int producerId);

    @Transactional
    public void deleteAllByViewerId(int viewerId);

    @Query(value = "SELECT AVG(rating) FROM producer_rating WHERE producer_id = ?1", nativeQuery = true)
    Double getAverageRatingByProducerId(int id);
}
