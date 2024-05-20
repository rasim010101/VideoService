package com.rasim.videoservice.bootstrap;

import com.rasim.videoservice.entities.*;
import com.rasim.videoservice.repositories.CommentRepository;
import com.rasim.videoservice.repositories.UserRepository;
import com.rasim.videoservice.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class InitData implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String[] NAMES = {
            "Aigerim", "Aijamal", "Aizat", "Ainara", "Ainur", "Aisuluu", "Aiturgan", "Ayym", "Akjol", "Akmaral",
            "Almaz", "Altynbek", "Altynai", "Altynai", "Altynbek", "Alymbek", "Amina", "Anara", "Anarkyz", "Anarkul",
            "Anarchy", "Anwar", "Apkerim", "Aray", "Arailem", "Arzyn", "Asem", "Asel", "Aselma", "Askar", "Asylbek",
            "Asylzat", "Asylkul", "Asylkym", "Asylnur", "Asylsul", "Asyltan", "Asyltokmo", "Asylchubai", "Akhmat",
            "Ahtem", "Akhirkan", "Ayana", "Ayaz", "Ayana", "Ayangerel", "Ayankul", "Bayalin", "Bakyt", "Bakytbek", "Bakytgul",
            "Bakytul", "Balagul", "Barlyk", "Barak", "Begimay", "Bekbolot", "Bekzat", "Bektur", "Bekzat", "Beknazar",
            "Bekbek", "Bekzhan", "Berdigul", "Berdybek", "Bereke", "Bereke", "Bereke", "Berik", "Berikbek", "Berkut",
            "Bibigul", "Bidayim", "Bikaleem", "Bilginur", "Billuk", "Birdayim", "Birdebek", "Bishkek", "Bolot", "Bolotbek",
            "Boronkul", "Boronchu", "Botagoz", "Bubuyram", "Burkitbek", "Burulbai", "Byrsana", "Valentina", "Valeriy",
            "Valeriya", "Vasiliy", "Venira", "Veronika", "Viktor", "Viktoriya", "Vilhelm", "Vitaliy", "Vladimir", "Galym",
            "Gaukhar", "Gulzhamal", "Gulzat", "Gulzanat", "Gulzira", "Gulbarshyn", "Guldar", "Guldana", "Gulzhan",
            "Gulzhigit", "Gulzara", "Gulnara", "Gulnur", "Gulpara", "Gulshat", "Gulchachak", "Gulchubai", "Gulayym",
            "Gulaman", "Gularal", "Gularik", "Gulashek", "Gulibragim", "Gulibragima", "Gulisat", "Gulilim",
            "Gulilim", "Gulisca", "Gulisra", "Gulisra", "Gulisha", "Gulisha", "Gulqair", "Gulnaza", "Gulnayym",
            "Gulnisa", "Gulnura", "Gulnura", "Gultayym", "Gultemir", "Gultemir", "Gulchayyr", "Gulchak", "Gulchek",
            "Gulchin", "Guljar", "Gulim", "Gulimzhan", "Gulimkan", "Gulynur", "Gulychek", "Guluabzhamal", "Guluz",
            "Gulusalim", "Gulusara", "Gulushar", "Gulushar"
    };

    private static final String[] DOMAINS = {
            "example.com", "mail.com", "test.com", "email.com"
    };

    private static final Random RANDOM = new Random();

    @Override
    public void run(String... args) {
        User admin = User.builder()
                .name("Admin")
                .username("admin")
                .email("admin@gmail.com")
                .roles(Set.of(UserRole.ADMIN))
                .password(passwordEncoder.encode("password"))
                .build();

        User staff = User.builder()
                .name("Staff")
                .username("staff")
                .email("staff@gmail.com")
                .roles(Set.of(UserRole.USER))
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(UserRole.STAFF))
                .build();
        userRepository.saveAll(List.of(admin, staff));
        try {
            Set<User> savedUsers = new HashSet<>();
            Set<Video> savedVideos = new HashSet<>();
            Set<Comment> savedComments = new HashSet<>();

            // Create 50 users with random names, usernames, and emails
            for (int i = 0; i < 50; i++) {

                User user = User.builder()
                        .name("user" + i)
                        .username("username" + i)
                        .email("email@" + i + ".com")
                        .password(passwordEncoder.encode("password"))
                        .roles(Set.of(UserRole.USER))
                        .build();
                savedUsers.add(user);
            }
            userRepository.saveAll(savedUsers);

            List<User> userList = new ArrayList<>(savedUsers);

            LocalDate startDate = LocalDate.of(2024, 3, 12);
            ZoneId zoneId = ZoneId.systemDefault();
            Date initialDate = Date.from(startDate.atStartOfDay(zoneId).toInstant());

            // Create 200 videos
            for (int i = 0; i < 200; i++) {
                LocalDate newLocalDate = startDate.plusDays(i);
                Date newDate = Date.from(newLocalDate.atStartOfDay(zoneId).toInstant());
                Video video = Video.builder()
                        .videoName("Video " + i)
                        .description("Video description" + i)
                        .date(newDate)
                        .createdBy(userList.get(i % 50))  // Using modulo to cycle through users
                        .build();
                Video savedVideo = videoRepository.save(video);
                savedVideos.add(savedVideo);
            }

            List<Video> videoList = new ArrayList<>(savedVideos);

            // Create 200 comments
            for (int i = 0; i < 200; i++) {
                Comment comment = Comment.builder()
                        .text("Comment " + i)
                        .video(videoList.get(i % 200))  // Using modulo to cycle through videos
                        .user(userList.get(i % 50))  // Using modulo to cycle through users
                        .build();
                Comment savedComment = commentRepository.save(comment);
                savedComments.add(savedComment);
            }
        } catch (DataAccessException e) {
            // Handle database access exception
            e.printStackTrace();
        }
    }
}
