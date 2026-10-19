package com.cinebook.moviebooking.config;

import com.cinebook.moviebooking.entity.*;
import com.cinebook.moviebooking.entity.enums.*;
import com.cinebook.moviebooking.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            MovieRepository movieRepository,
            TheatreRepository theatreRepository,
            ShowRepository showRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Checking database initialization...");

        // 1. Seed Users
        User admin = userRepository.findByEmail("admin@cinebook.com").orElse(null);
        if (admin == null) {
            admin = new User(
                    "System Administrator",
                    "admin@cinebook.com",
                    passwordEncoder.encode("Admin@123"),
                    "0771234567",
                    Role.ADMIN
            );
            admin = userRepository.save(admin);
            logger.info("Admin user created: admin@cinebook.com / Admin@123");
        }

        User customer = userRepository.findByEmail("customer@cinebook.com").orElse(null);
        if (customer == null) {
            customer = new User(
                    "Jane Doe",
                    "customer@cinebook.com",
                    passwordEncoder.encode("Customer@123"),
                    "0779876543",
                    Role.CUSTOMER
            );
            customer = userRepository.save(customer);
            logger.info("Customer user created: customer@cinebook.com / Customer@123");
        }

        // 2. Seed Movies
        if (movieRepository.count() == 0) {
            Movie movie1 = new Movie(
                    "Dune: Part Two",
                    "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
                    166,
                    "English",
                    "Sci-Fi",
                    LocalDate.of(2024, 3, 1),
                    "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
                    "Denis Villeneuve",
                    8.6,
                    MovieStatus.NOW_SHOWING
            );

            Movie movie2 = new Movie(
                    "Oppenheimer",
                    "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.",
                    180,
                    "English",
                    "Biography / Drama",
                    LocalDate.of(2023, 7, 21),
                    "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=800&auto=format&fit=crop&q=80",
                    "Christopher Nolan",
                    8.9,
                    MovieStatus.NOW_SHOWING
            );

            Movie movie3 = new Movie(
                    "Spider-Man: Across the Spider-Verse",
                    "Miles Morales catapults across the Multiverse, encountering a team of Spider-People charged with protecting its very existence.",
                    140,
                    "English",
                    "Animation / Action",
                    LocalDate.of(2023, 6, 2),
                    "https://images.unsplash.com/photo-1635805737707-575885ab0820?w=800&auto=format&fit=crop&q=80",
                    "Joaquim Dos Santos",
                    8.7,
                    MovieStatus.NOW_SHOWING
            );

            Movie movie4 = new Movie(
                    "Interstellar",
                    "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot is tasked to pilot a spacecraft along with a team of researchers.",
                    169,
                    "English",
                    "Adventure / Drama",
                    LocalDate.of(2014, 11, 7),
                    "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
                    "Christopher Nolan",
                    8.7,
                    MovieStatus.NOW_SHOWING
            );

            movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4));
            logger.info("Sample movies seeded.");
        }

        // 3. Seed Theatres
        if (theatreRepository.count() == 0) {
            Theatre theatre1 = new Theatre(
                    "Savoy 3D Cinema",
                    "Wellawatte, Colombo 06",
                    96,
                    TheatreStatus.ACTIVE,
                    "0112361234",
                    "Dolby Atmos, 4K Laser Projection, VIP Recliner Seats, Parking"
            );

            Theatre theatre2 = new Theatre(
                    "Majestic Cineplex - Platinum",
                    "Galle Road, Colombo 04",
                    96,
                    TheatreStatus.ACTIVE,
                    "0112581234",
                    "Dolby 7.1 Surround, Air Conditioned, Food Court, Arcade"
            );

            Theatre theatre3 = new Theatre(
                    "Scope Cinemas - CCC",
                    "Colombo City Centre, Colombo 02",
                    96,
                    TheatreStatus.ACTIVE,
                    "0112083064",
                    "Sri Lanka's 1st IMAX, Dolby Atmos, Gourmet Concessions"
            );

            theatreRepository.saveAll(Arrays.asList(theatre1, theatre2, theatre3));
            logger.info("Sample theatres seeded.");
        }

        // 4. Seed Shows
        if (showRepository.count() == 0) {
            List<Movie> movies = movieRepository.findAll();
            List<Theatre> theatres = theatreRepository.findAll();

            if (!movies.isEmpty() && !theatres.isEmpty()) {
                LocalDate today = LocalDate.now();

                Show show1 = new Show(
                        movies.get(0),
                        theatres.get(0),
                        today,
                        LocalTime.of(14, 30),
                        1500.00,
                        "Screen 1",
                        "3D",
                        8,
                        12,
                        ShowStatus.SCHEDULED
                );

                Show show2 = new Show(
                        movies.get(0),
                        theatres.get(0),
                        today,
                        LocalTime.of(18, 45),
                        1800.00,
                        "Screen 1",
                        "3D IMAX",
                        8,
                        12,
                        ShowStatus.SCHEDULED
                );

                Show show3 = new Show(
                        movies.get(1),
                        theatres.get(1),
                        today,
                        LocalTime.of(15, 0),
                        1200.00,
                        "Screen 2",
                        "2D",
                        8,
                        12,
                        ShowStatus.SCHEDULED
                );

                Show show4 = new Show(
                        movies.get(2),
                        theatres.get(2),
                        today.plusDays(1),
                        LocalTime.of(16, 0),
                        1400.00,
                        "Screen 1",
                        "3D",
                        8,
                        12,
                        ShowStatus.SCHEDULED
                );

                List<Show> savedShows = showRepository.saveAll(Arrays.asList(show1, show2, show3, show4));
                logger.info("Sample shows seeded.");

                // 5. Seed a Sample Booking with Confirmed Payment for Customer
                if (bookingRepository.count() == 0 && customer != null) {
                    Show targetShow = savedShows.get(0);
                    List<String> seats = new java.util.ArrayList<>(Arrays.asList("C5", "C6"));
                    double amount = seats.size() * targetShow.getTicketPrice();

                    Booking sampleBooking = new Booking(
                            customer,
                            targetShow,
                            seats,
                            seats.size(),
                            amount,
                            BookingStatus.CONFIRMED
                    );
                    Booking savedBooking = bookingRepository.save(sampleBooking);

                    Payment samplePayment = new Payment(
                            savedBooking,
                            amount,
                            PaymentMethod.CARD,
                            PaymentStatus.SUCCESS,
                            "TXN-SEED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                    );
                    paymentRepository.save(samplePayment);
                    savedBooking.setPayment(samplePayment);

                    logger.info("Sample confirmed booking and payment seeded for customer@cinebook.com");
                }
            }
        }
        logger.info("Database initialization completed successfully.");
    }
}
