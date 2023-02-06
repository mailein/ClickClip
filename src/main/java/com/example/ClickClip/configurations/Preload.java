package com.example.ClickClip.configurations;

import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.entities.Word;
import com.example.ClickClip.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Slf4j
@Configuration
public class Preload {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {

        User u = User.builder()
                .name("username")
                .password("password")
                .glossaries(new LinkedHashSet<>())
                .build();
        Glossary g = Glossary.builder()
                .name("glossaryname")
                .user(u)
                .words(new LinkedHashSet<>())
                .build();
        Word w = Word.builder()
                .name("wordname")
                .glossary(g)
                .build();

        //bidirectional: either set in constructor or in setter
        //only set one side will cause validation error
//        w.setGlossary(g);
//        g.setUser(u);

        g.getWords().add(w);
        u.getGlossaries().add(g);

        return args -> {
            log.info("Preloading " + repository.save(u));
        };
    }
}
