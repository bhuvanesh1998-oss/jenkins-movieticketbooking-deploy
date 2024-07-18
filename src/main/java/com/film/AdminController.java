package com.film;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.film.beans.MovieDetails;
import com.film.service.MovieService;
@Controller
public class AdminController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<MovieDetails> movies = movieService.getAllMovies1();
        model.addAttribute("movies", movies);
        model.addAttribute("movie", new MovieDetails()); 
        return "admin";
    }

    @PostMapping("/add-movie")
    public String addMovie(@ModelAttribute MovieDetails movie) {
        movieService.addMovie(movie);
        return "redirect:/admin";
    }

    @PostMapping("/delete-movie-by-name")
    public String deleteMovieByName(@RequestParam("name") String name) {
        movieService.deleteMovieByName(name);
        return "redirect:/admin";
    }
}
