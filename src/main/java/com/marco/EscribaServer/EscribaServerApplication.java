package com.marco.EscribaServer;

import com.marco.EscribaServer.Utils.CommentType;
import com.marco.EscribaServer.dto.CommentDTO;
import com.marco.EscribaServer.entity.*;
import com.marco.EscribaServer.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class EscribaServerApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReadingService readingService;

    @Autowired
    private GenreService genreService;

    public static void main(String[] args) {
        SpringApplication.run(EscribaServerApplication.class, args);
    }

    @Override
    public void run(String... args) {

        List<String> imageUrls = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream("/starterData/resultado.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean test = true;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Image URL:")) {
                    String imageUrl = line.substring(line.indexOf("[\"") + 2, line.lastIndexOf("\"]"));
                    imageUrls.add(imageUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int userCount = 20;
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= userCount; i++) {
            User user = new User("user" + i, "password" + i, "user" + i + "@example.com");

            String baseUrl = "https://ui-avatars.com/api/?name=%s&background=333&color=f3f3f3&size=512";
            String urlStr = String.format(baseUrl, user.getUsername());
            String result = "";

            try (InputStream in = new URL(urlStr).openStream()) {
                byte[] imageBytes = IOUtils.toByteArray(in);
                result = Base64.getEncoder().encodeToString(imageBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setImage(result);
            users.add(userService.save(user));
        }


        Random rand = new Random();

        for (User user : users) {
            List<User> others = new ArrayList<>(users);
            Collections.shuffle(others);

            int followCount = rand.nextInt(userCount) + 1;
            for (int i = 0; i < followCount; i++) {
                User otherUser = others.get(i);
                if (!user.getUsername().equals(otherUser.getUsername())) {
                    userService.updateFollowers(otherUser.getUsername(), user.getUsername());
                }
            }
        }

        List<String> genres = new ArrayList<String>() {{
            add("Novel");
            add("Short Story");
            add("Poetry");
            add("Drama");
            add("Science Fiction");
            add("Fantasy");
            add("Horror");
            add("Mystery");
            add("Romance");
            add("Adventure");
            add("Humor");
            add("Historical");
            add("Detective");
            add("Western");
            add("Dystopian");
            add("Realistic");
            add("Juvenile");
            add("Children's");
            add("History");
            add("Biography");
            add("Business");
            add("Politics");
            add("Travel");
            add("Gastronomy");
            add("Art");
            add("Photography");
            add("Sports");
            add("Education");
            add("Science");
            add("Technology");
            add("Religion");
            add("Environment");
            add("Philosophy");
            add("Sociology");
            add("Journalism");
        }};

        List<Genre> entityGenres = new ArrayList<>();

        for(int i = 0; i < genres.size(); i++){
            Genre auxGenre = genreService.create(genres.get(i));
            entityGenres.add(auxGenre);
        }

        int documentCount = userCount * 3;
        List<Document> documents = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= documentCount; i++) {
            int numGenres = random.nextInt(3) + 2;
            Document document = new Document();

            List<Genre> documentGenres = new ArrayList<>();
            for (int k = 0; k < numGenres; k++) {
                int randomIndex = random.nextInt(genres.size());
                Genre genre = entityGenres.get(randomIndex);
                documentGenres.add(genre);
                genre.getDocumentsOfGenre().add(document);
            }

            document.setGenres(documentGenres);
            document.setTittle("Documento " + i);
            document.setCreator(users.get((i - 1) / 3));
            document.setText("Texto del documento " + i);
            document.setRating(i%5);
            document.setSynopsis("Sinopsis de prueba, esto debería de aparecer correctamente en el documento");
            document.setCover("data:image/png;base64," + imageUrls.get(i - 1));

            int userDocumentCount = (i % 3 == 0) ? 3 : i % 3;
            if (userDocumentCount == 3) {
                document.setPublic(false);
            } else {
                document.setPublic(true);
            }

            Long documentID = documentService.createDocument(document.toDto());
            documents.add(documentService.getDocument(documentID));
            document.setBeingRead(Collections.EMPTY_LIST);

            for (Genre genre : documentGenres) {
                genreService.update(genre.getGenre());
            }
        }

        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < documents.size(); j++) {

                if (documents.get(j).isPublic()) {
                    int randomReview = random.nextInt(5) + 1;

                    Review review = new Review("Reseña " + (i * documents.size() + j + 1), users.get(i), documents.get(j), randomReview);

                    CommentDTO reviewDto = review.toDto();
                    reviewDto.setCommentType(CommentType.REVIEW);

                    Long reviewID = commentService.saveComment(reviewDto).getId();

                    review.setId(reviewID);

                    Response response = new Response("Respuesta " + (i * documents.size() + j + 1), users.get(i), documents.get(j), review);

                    CommentDTO responseDto = response.toDto();
                    responseDto.setCommentType(CommentType.RESPONSE);

                    commentService.saveComment(responseDto);

                    Reading reading = new Reading(users.get(i), documents.get(j), (float) (j + 1) / 10);
                    readingService.createReading(reading.toDto());
                }
            }
        }
        System.out.println("Data set created");
    }
}
