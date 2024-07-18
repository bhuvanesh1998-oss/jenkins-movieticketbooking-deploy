package com.film;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.film.beans.Customer;
import com.film.beans.MovieDetails;
import com.film.beans.OrderHistory;
import com.film.beans.Seat;
import com.film.service.CustomerDao;
import com.film.service.EmailService;


@Controller
public class FilmController {

	@Autowired
	private CustomerDao dao;
	
	// Opening home page
	@GetMapping("/")
	public String home(Model m, HttpSession session) {

		String movie = (String) session.getAttribute("movieName");
		System.out.println(movie + "========Index");
		List<MovieDetails> movie2 = dao.getAllMovie();
		m.addAttribute("movieList", movie2);
		m.addAttribute("menu", "home");

		return "index";
	}

//second page
	@GetMapping("/booking")
	public String bookingCheck(@RequestParam("movieName") String movieName, Model m, HttpSession session) {
		List<MovieDetails> movie2 = dao.getAllMovie();
		List<String> checkMovie = new ArrayList<>();
		for (MovieDetails string : movie2) {
			checkMovie.add(string.getMovieName());
		}
		if (checkMovie.contains(movieName)) {
			session.setAttribute("movieName", movieName);
			System.out.println(movieName);
			LocalDate now = LocalDate.now();
			LocalDate monthLimit = LocalDate.now();
			String time = "10:45 am";
			List<String> seatNo1 = new ArrayList<String>();
			List<Seat> all = dao.getAllSeat(now, time);

			for (Seat s : all) {
				for (String s1 : s.getSeatNo()) {
					seatNo1.add(s1);
				}

			}

			m.addAttribute("date", now);
			m.addAttribute("max", monthLimit.plusMonths(1));
			m.addAttribute("min", monthLimit);
			m.addAttribute("time", time);
			m.addAttribute("seats", seatNo1);
			return "home";

		} else {
			return "redirect:/";

		}

	}

//Registration User
	@GetMapping("/register")
	public String register(Model m) {

		m.addAttribute("menu", "register");
		return "register";
	}

//	Login form
	@GetMapping("/loginForm")
	public String loginForm(Model m) {
		m.addAttribute("menu", "login");
		return "login";
	}

//	User save process
	@PostMapping("/save")
	public String save(@ModelAttribute("customer") Customer customer) {
		dao.save(customer);
		return "redirect:/register";

	}

//	Login process
	@PostMapping("/processing")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, Model m) {

		Customer object = (Customer) session.getAttribute("user");
		if (object != null) {
			return "redirect:/booking-seat";
		} else {

			Customer customer = dao.login(email, password);

			if (customer == null) {
				m.addAttribute("failed", "Invalied login");
				return "login";
			} else {
				session.setAttribute("user", customer);
			}
			return "redirect:/home";
		}
	}

	@GetMapping("/home")
	public String mainDashboard(HttpSession session, Model m) {
		session.removeAttribute("bookingdate");
		session.removeAttribute("bookingtime");
		session.removeAttribute("movieName");
		m.addAttribute("menu", "home");

		String message = (String) session.getAttribute("msg");
		m.addAttribute("message", message);
		session.removeAttribute("msg");
//		System.out.println(message);
		List<MovieDetails> movie2 = dao.getAllMovie();
		m.addAttribute("listMovie", movie2);

		return "main-dashboard";
	}

//	Dash board page
	@GetMapping("/booking-seat")
	public String getUser(@RequestParam("movieName") String movieName, HttpSession session, Model m) {
		List<MovieDetails> movie2 = dao.getAllMovie();
		List<String> checkMovie = new ArrayList<>();
		for (MovieDetails string : movie2) {
			checkMovie.add(string.getMovieName());
		}
		if (checkMovie.contains(movieName)) {
			session.setAttribute("movieName", movieName);

			LocalDate now = LocalDate.now();
			LocalDate monthLimit = LocalDate.now();
			String time = "10:45 am";

			Customer customer = (Customer) session.getAttribute("user");
			List<String> seatNo1 = new ArrayList<String>();
			List<Seat> seat = customer.getSeat();

			List<Seat> all = dao.getAllSeat(now, time);

			for (Seat s : all) {
				for (String s1 : s.getSeatNo()) {
					seatNo1.add(s1);
				}

			}

			m.addAttribute("date", now);
			m.addAttribute("time", time);
			m.addAttribute("max", monthLimit.plusMonths(1));
			m.addAttribute("min", monthLimit);
			m.addAttribute("seats", seatNo1);
			m.addAttribute("seat", seat);
			session.setAttribute("user", customer);
			return "dashboard";
		} else {
			return "redirect:/home";
		}

	}

//	Logout process
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");

		session.removeAttribute("bookingdate");
		session.removeAttribute("bookingtime");
		session.removeAttribute("movieName");

		return "redirect:/";
	}

//	Seat booking process
	@PostMapping("/book-seat")
    public String bookSeat(@ModelAttribute("Seat") Seat seat, @RequestParam("movieName") String movieName,
                           HttpSession session, Model m) {
        LocalDate currentDate = LocalDate.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date todayDate = Date.from(currentDate.atStartOfDay(defaultZoneId).toInstant());
        LocalDate date = (LocalDate) session.getAttribute("bookingdate");
        String time = (String) session.getAttribute("bookingtime");
        Customer object = (Customer) session.getAttribute("user");

        if (object == null) {
            return "redirect:/loginForm";
        } else if ((seat.getSeatNo().isEmpty()) || (movieName == null)) {
            return "redirect:/home";
        } else if (date == null) {
            date = currentDate;
            time = "10:45 am";

            if (((date.isAfter(currentDate) || date.equals(currentDate))
                    && (date.isBefore(currentDate.plusMonths(1)) || date.equals(currentDate.plusMonths(1))))) {
                Date date2 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
                List<Double> price = new ArrayList<Double>();
                double sum = 0;
                double p = 110d;
                for (String s : seat.getSeatNo()) {
                    sum += p;
                    price.add(p);
                }
                seat.setTotal(sum);
                seat.setPrice(price);

                OrderHistory history = new OrderHistory(seat.getSeatNo(), price, sum, movieName, todayDate, date2, time, object);
                dao.saveSeat(seat, object, date2, time);
                dao.saveHistory(history, object);

                String recipientEmail = object.getEmail();
                String dateStr = date.toString(); 
                String timeStr = time;
                String seatNumbers = String.join(", ", seat.getSeatNo());
                double totalPrice = seat.getTotal();
                String movieNameStr = movieName;
                String theaterName = "Sri Devi Cinemas";

                EmailService.sendTicketDetails(recipientEmail, dateStr, timeStr, seatNumbers, totalPrice, movieNameStr,theaterName);

                List<String> seatNo1 = new ArrayList<String>();
                List<Customer> all = dao.getAll();
                for (Customer c : all) {
                    for (Seat s : c.getSeat()) {
                        for (String s1 : s.getSeatNo()) {
                            seatNo1.add(s1);
                        }
                    }
                }

                m.addAttribute("seats", seatNo1);
                session.setAttribute("user", object);
                session.setAttribute("msg", "Your seat has been booked successfully");
                return "redirect:/home";
            } else {
                return "redirect:/booking-seat?movieName=" + movieName;
            }
        } else {
            if (((date.isAfter(currentDate) || date.equals(currentDate))
                    && (date.isBefore(currentDate.plusMonths(1)) || date.equals(currentDate.plusMonths(1))))) {
                Date date2 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
                List<Double> price = new ArrayList<Double>();
                double sum = 0;
                double p = 110d;
                for (String s : seat.getSeatNo()) {
                    sum += p;
                    price.add(p);
                }
                seat.setTotal(sum);
                seat.setPrice(price);

                OrderHistory history = new OrderHistory(seat.getSeatNo(), price, sum, movieName, todayDate, date2, time, object);
                dao.saveSeat(seat, object, date2, time);
                dao.saveHistory(history, object);

                String recipientEmail = object.getEmail();
                String dateStr = date.toString(); 
                String timeStr = time;
                String seatNumbers = String.join(", ", seat.getSeatNo());
                double totalPrice = seat.getTotal();
                String movieNameStr = movieName;
                String theaterName = "Sri Devi Cinemas";

                EmailService.sendTicketDetails(recipientEmail, dateStr, timeStr, seatNumbers, totalPrice, movieNameStr,theaterName);

                List<String> seatNo1 = new ArrayList<String>();
                List<Customer> all = dao.getAll();
                for (Customer c : all) {
                    for (Seat s : c.getSeat()) {
                        for (String s1 : s.getSeatNo()) {
                            seatNo1.add(s1);
                        }
                    }
                }

                m.addAttribute("seats", seatNo1);
                session.setAttribute("user", object);
                session.setAttribute("msg", "Your seat has been booked successfully");
                return "redirect:/home";
            } else {
                return "redirect:/booking-seat?movieName=" + movieName;
            }
        }
    }

//	Order history
	@GetMapping("/order-history")
	public String history(HttpSession session, Model m) {
		Date todayDate = new Date();
		Customer object = (Customer) session.getAttribute("user");
		session.setAttribute("user", object);
		List<OrderHistory> list = dao.getAllHistory(object.getBid());
		m.addAttribute("hList", list);
		m.addAttribute("todaydate", todayDate);

		LocalDate date = (LocalDate) session.getAttribute("bookingdate");
		System.out.println(date);
		m.addAttribute("menu", "order");
		return "history";
	}


//	User Setting
	@GetMapping("/setting")
	public String getSetting(Model m, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("user");
		m.addAttribute("user", customer);
		m.addAttribute("menu", "setting");
		return "setting";
	}

//	User update form
	@GetMapping("/setting/update/{id}")
	public String updateForm(@PathVariable("id") long id, Model m) {
		System.out.println(id);
		m.addAttribute("menu", "setting");
		return "update-details";

	}

//	update Details
	@PostMapping("/setting/update-details")
	public String updateDetails(@ModelAttribute("customer") Customer cust, HttpSession session) {
		String name = cust.getName();
		String email = cust.getEmail();
		Customer customer = (Customer) session.getAttribute("user");
		customer.setName(name);
		customer.setEmail(email);
		dao.updateDetail(customer);

		return "redirect:/setting";

	}

	@PostMapping("/check")
	public String checkDate(@RequestParam("localdate") String date, @RequestParam("localtime") String time, Model m,
			HttpSession session) {
		Customer object = (Customer) session.getAttribute("user");
		String movie = (String) session.getAttribute("movieName");
		LocalDate monthLimit = LocalDate.now();
		if (movie.equals(null)) {
			return "home";

		} else if (object == null) {
			LocalDate now = LocalDate.parse(date);
			List<String> seatNo1 = new ArrayList<String>();
			List<Seat> all = dao.getAllSeat(now, time);

			for (Seat s : all) {
				for (String s1 : s.getSeatNo()) {
					seatNo1.add(s1);
				}

			}

			session.setAttribute("bookingdate", now);
			session.setAttribute("bookingtime", time);
			m.addAttribute("date", now);
			m.addAttribute("max", monthLimit.plusMonths(1));
			m.addAttribute("min", monthLimit);
			m.addAttribute("time", time);
			m.addAttribute("seats", seatNo1);

			return "home";
		} else {
			LocalDate now = LocalDate.parse(date);
			List<String> seatNo1 = new ArrayList<String>();
			List<Seat> all = dao.getAllSeat(now, time);

			for (Seat s : all) {
				for (String s1 : s.getSeatNo()) {
					seatNo1.add(s1);
				}

			}

			session.setAttribute("bookingdate", now);
			session.setAttribute("bookingtime", time);
			m.addAttribute("date", now);
			m.addAttribute("max", monthLimit.plusMonths(1));
			m.addAttribute("min", monthLimit);
			m.addAttribute("time", time);
			m.addAttribute("seats", seatNo1);

			return "dashboard";
		}

	}
	
	@GetMapping("/aboutus")
	public String aboutUs(Model m) {
	    m.addAttribute("menu", "aboutus");
	    return "aboutus";
	}
	
	@GetMapping("/contactus")
	public String contactUs(Model m) {
	    m.addAttribute("menu", "contactus");
	    return "contactus";
	}

//	Exception handling
	@ExceptionHandler(Exception.class)
	public String handleError(Exception ex, Model m, HttpSession session) {
		Customer object = (Customer) session.getAttribute("user");
		if (object == null) {
			return "redirect:/loginForm";
		} else {
			return "redirect:/home";
		}
	}

}
