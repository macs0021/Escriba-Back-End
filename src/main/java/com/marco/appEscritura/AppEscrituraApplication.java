package com.marco.appEscritura;

import com.marco.appEscritura.Utils.CommentType;
import com.marco.appEscritura.dto.CommentDTO;
import com.marco.appEscritura.entity.*;
import com.marco.appEscritura.service.CommentService;
import com.marco.appEscritura.service.DocumentService;
import com.marco.appEscritura.service.ReadingService;
import com.marco.appEscritura.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
					String imageUrl = line.substring(line.indexOf("[\"")+2, line.lastIndexOf("\"]"));
					imageUrls.add(imageUrl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("MUESTRO PORTADA NUMERO 0: " + imageUrls.get(0));
		// Crear usuarios
		List<User> users = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			User user = new User("user" + i, "password" + i, "user" + i + "@example.com");
			users.add(userService.save(user));
		}

		// Hacer que todos los usuarios se sigan entre ellos
		for (User user : users) {
			for (User otherUser : users) {
				if (!user.getUsername().equals(otherUser.getUsername())) {
					userService.updateFollowers(otherUser.getUsername(),user.getUsername());
				}
			}
		}

		List<Document> documents = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			Document document = new Document();
			document.setTittle("Documento " + i);
			document.setCreator(users.get(i - 1));
			document.setText("Texto del documento " + i);
			//document.setPublic(i % 2 == 0);
			document.setRating(i);
			document.setCover("data:image/png;base64," + imageUrls.get(i-1));
			documents.add(documentService.getDocument(documentService.createDocument(document.toDto())));
			document.setBeingRead(Collections.EMPTY_LIST);
		}

		// Crear comentarios, reseñas, respuestas y lecturas
		for (int i = 0; i < users.size(); i++) {
			for (int j = 0; j < documents.size(); j++) {

				// Crear reseña
				Review review = new Review("Reseña " + (i * documents.size() + j + 1), users.get(i), documents.get(j), j + 1);

				CommentDTO reviewDto = review.toDto();
				reviewDto.setCommentType(CommentType.REVIEW);

				Long reviewID = commentService.saveComment(reviewDto).getId();

				review.setId(reviewID);

				// Crear respuesta
				Response response = new Response("Respuesta " + (i * documents.size() + j + 1), users.get(i), documents.get(j), review);

				CommentDTO responseDto = response.toDto();
				responseDto.setCommentType(CommentType.RESPONSE);

				commentService.saveComment(responseDto);

				// Crear lectura
				Reading reading = new Reading(users.get(i), documents.get(j), (float) (j + 1) / 10);
				readingService.createReading(reading.toDto());
			}
		}
	}

}
