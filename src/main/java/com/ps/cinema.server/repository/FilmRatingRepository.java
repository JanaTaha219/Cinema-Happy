package com.ps.cinema.server.repository;

import com.ps.cinema.server.model.FilmRating;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRatingRepository extends JpaRepository<FilmRating, Integer> {
    public List<FilmRating> findByFilmId(int filmId);

    @Query(value = "SELECT * FROM film_rating WHERE film_id = :filmId AND viewer_id = :viewerId", nativeQuery = true)

    public Optional<FilmRating> findByFilmIdAndViewerId(@Param("filmId")int filmId, @Param("viewerId")int viewerId);

    public void deleteAllByFilmId(int filmId);

    @Transactional
    public void deleteAllByViewerId(int viewerId);

    public void deleteAllByViewerUsername(String viewerName);

    @Query(value = "SELECT AVG(rating) FROM film_rating WHERE film_id = ?1", nativeQuery = true)
    Double getAverageRatingByFilmId(int id);
}
