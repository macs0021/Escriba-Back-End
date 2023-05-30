package com.marco.appEscritura;

import com.marco.appEscritura.Utils.CommentType;
import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.entity.*;
import com.marco.appEscritura.service.CommentService;
import com.marco.appEscritura.service.DocumentService;
import com.marco.appEscritura.service.ReadingService;
import com.marco.appEscritura.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.io.InputStream;
import java.net.URL;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AppEscrituraApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReadingService readingService;


    public static void main(String[] args) {
        SpringApplication.run(AppEscrituraApplication.class, args);
    }

    @Override
    public void run(String... args) {

        List<String> imageUrls = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/com/marco/appEscritura/starterData/resultado.txt"))) {
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


        //System.out.println("MUESTRO PORTADA NUMERO 0: " + imageUrls.get(0));
        // Crear usuario
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


        // Hacer que todos los usuarios se sigan entre ellos
        for (User user : users) {
            for (User otherUser : users) {
                if (!user.getUsername().equals(otherUser.getUsername())) {
                    userService.updateFollowers(otherUser.getUsername(), user.getUsername());
                }
            }
        }

        List<String> genres = Arrays.asList(
                "Novel",
                "Short Story",
                "Poetry",
                "Drama",
                "Science Fiction",
                "Fantasy",
                "Horror",
                "Mystery",
                "Romance",
                "Adventure",
                "Humor",
                "Historical",
                "Detective",
                "Western",
                "Dystopian",
                "Realistic",
                "Juvenile",
                "Children's",
                "History",
                "Biography and Memoir",
                "Self-Help and Personal Development",
                "Business and Finance",
                "Politics and Current Affairs",
                "Travel",
                "Food and Gastronomy",
                "Art and Photography",
                "Sports and Outdoor Activities",
                "Education and Reference",
                "Science and Technology",
                "Religion and Spirituality",
                "Environment and Ecology",
                "Philosophy and Thought",
                "Sociology and Anthropology",
                "Journalism and Essays"
        );

        int documentCount = userCount * 3;
        List<Document> documents = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= documentCount; i++) {
            int numGenres = random.nextInt(3) + 2; // Genera un número aleatorio entre 2 y 4
            Document document = new Document();

            List<String> documentGenres = new ArrayList<>();
            for (int k = 0; k < numGenres; k++) {
                int randomIndex = random.nextInt(genres.size());
                String genre = genres.get(randomIndex);
                documentGenres.add(genre);
            }

            document.setGenres(documentGenres);
            document.setTittle("Documento " + i);
            document.setCreator(users.get((i - 1) / 3));
            document.setText("Texto del documento " + i);
            document.setRating(i);
            document.setSynopsis("Sinopsis de prueba, esto debería de aparecer correctamente en el documento");
            document.setCover("data:image/png;base64," + imageUrls.get(i - 1));


            int userDocumentCount = (i % 3 == 0) ? 3 : i % 3;
            if (userDocumentCount == 3) {
                document.setPublic(false);
            } else {
                document.setPublic(true);
            }

            documents.add(documentService.getDocument(documentService.createDocument(document.toDto())));
            document.setBeingRead(Collections.EMPTY_LIST);
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
        System.out.println("Terminada generación de datos");
    }
}
