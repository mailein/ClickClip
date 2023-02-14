package com.example.FlashCards.configurations;

import com.example.FlashCards.entities.Glossary;
import com.example.FlashCards.entities.User;
import com.example.FlashCards.entities.Word;
import com.example.FlashCards.repositories.GlossaryRepository;
import com.example.FlashCards.repositories.UserRepository;
import com.example.FlashCards.repositories.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("!test")//prevent this Bean from running in tests
@Configuration
public class Preload {
    private final WordRepository wordRepository;

    public Preload(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   GlossaryRepository glossaryRepository) {

        User u1 = User.builder()
                .name("u1")
                .password("password")
                .build();
        User u2 = User.builder()
                .name("u2")
                .password("password")
                .build();
        User u3 = User.builder()
                .name("u3")
                .password("password")
                .build();

        Glossary g1 = Glossary.builder()
                .name("g1")
                .user(u3)
                .build();
        Glossary g2 = Glossary.builder()
                .name("g2")
                .user(u3)
                .build();

        Word w1 = Word.builder()
                .name("w1")
                .glossary(g1)
                .build();
        Word w2 = Word.builder()
                .name("w2")
                .glossary(g1)
                .build();
        Word w3 = Word.builder()
                .name("w3")
                .glossary(g1)
                .build();

        return args -> {
            User savedU1 = userRepository.save(u1);
            log.info("Preloading " + savedU1);
            User savedU2 = userRepository.save(u2);
            log.info("Preloading " + savedU2);
            User savedU3 = userRepository.save(u3);
            log.info("Preloading " + savedU3);

            log.info("Preloading " + glossaryRepository.save(g1));
            log.info("Preloading " + glossaryRepository.save(g2));

            userRepository.deleteById(savedU1.getId());
            log.info("Deleting the preloaded user with id " + savedU1.getId());
            userRepository.deleteById(savedU2.getId());
            log.info("Deleting the preloaded user with id " + savedU2.getId());

            log.info("Preloading " + wordRepository.save(w1));
            log.info("Preloading " + wordRepository.save(w2));
            log.info("Preloading " + wordRepository.save(w3));
        };
    }
}
