package com.film.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movies_details")
public class MovieDetails {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "movie_id")
	private Long movieId;

	
	@Column(nullable = false)
	private String movieName;
	
	@Column(nullable = false)
	private String image;
	
	
	
	


	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	

	public MovieDetails(Long movieId, String movieName, String image) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.image = image;
		
	}

	public MovieDetails(String movieName, String image) {
		super();
		this.movieName = movieName;
		this.image = image;
		
	}

	public MovieDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MovieDetails [movieId=" + movieId + ", movieName=" + movieName + ", image=" + image + "]";
	}


}
