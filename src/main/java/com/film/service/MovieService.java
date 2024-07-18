package com.film.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.film.beans.MovieDetails;

@Service
public class MovieService {

    @Autowired
    private MovieRepo movieRepository;
    public MovieDetails addMovie(MovieDetails movie) {
        return movieRepository.save(movie);
    }


    public List<MovieDetails> getAllMovies1() {
        return movieRepository.findAll();
    }
    @Transactional
    public void deleteMovieByName(String movieName) {
        MovieDetails movie = movieRepository.findByMovieName(movieName);
        if (movie != null) {
            movieRepository.delete(movie);
        }
    }
    @Transactional
    public void deleteByMovieId(Long movieName) {
        MovieDetails movie = movieRepository.findByMovieId(movieName);
        if (movie != null) {
            movieRepository.delete(movie);
        }
    }

	public Object getAllMovies() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteMovieById(Long id) {
		// TODO Auto-generated method stub
		
	}
}
