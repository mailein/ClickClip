package com.example.ClickClip.configurations;

import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.entities.Word;
import com.example.ClickClip.repositories.GlossaryRepository;
import com.example.ClickClip.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Slf4j
@Profile("!test")//prevent this Bean from running in tests
@Configuration
public class Preload {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   GlossaryRepository glossaryRepository) {

        User u = User.builder()
                .name("username")
                .password("password")
                .build();
        Glossary g = Glossary.builder()
                .name("glossaryname")
                .user(u)
                .build();
        Word w = Word.builder()
                .name("wordname")
                .glossary(g)
                .build();

        return args -> {
            log.info("Preloading " + userRepository.save(u));
            log.info("Preloading "+ glossaryRepository.save(g));
        };
    }
}
