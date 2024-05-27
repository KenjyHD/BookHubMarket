package com.kenjy.bookapi.runner;

import com.kenjy.bookapi.entities.Book;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.security.WebSecurityConfig;
import com.kenjy.bookapi.service.BookService;
import com.kenjy.bookapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final UserService userService;
    private final BookService bookService;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(userService::saveUser);
        getBooks().forEach(bookService::saveBook);
        log.info("Database initialized");
    }

    private List<Book> getBooks() {
        return Arrays.stream(BOOKS_STR.split("\n"))
                .map(bookInfoStr -> bookInfoStr.split(";"))
                .map(bookInfoArr -> new Book(bookInfoArr[1], bookInfoArr[0], Float.parseFloat(bookInfoArr[2]), bookInfoArr[3]))
                .collect(Collectors.toList());
    }

    private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@mycompany.com", WebSecurityConfig.ADMIN),
            new User("user", "user", "User", "user@mycompany.com", WebSecurityConfig.USER)
    );

    private static final String BOOKS_STR =
            """
                    author;Any Empire;12.3;pdfPath;
                    author;August Moon;12.3;pdfPath
                    author;The Barefoot Serpent (softcover) by Scott Morse;12.3;pdfPath
                    author;BB Wolf and the 3 LP's;12.3;pdfPath
                    author;Beach Safari by Mawil;12.3;pdfPath
                    author;Belzebubs;12.3;pdfPath
                    author;Bighead by Jeffrey Brown;12.3;pdfPath
                    author;Bodycount;12.3;pdfPath
                    author;Box Office Poison;12.3;pdfPath
                    author;From Hell;12.3;pdfPath
                    author;Cat'n'Bat;12.3;pdfPath
                    author;Crater XV;12.3;pdfPath
                    author;Cry Yourself to Sleep by Jeremy Tinder;12.3;pdfPath
                    author;Dear Beloved Stranger;12.3;pdfPath
                    author;Dear Julia;12.3;pdfPath
                    author;Death by Chocolate - Redux;12.3;pdfPath
                    author;Dragon Puncher (Book 1);12.3;pdfPath
                    author;Dragon Puncher (Book 2): Island;12.3;pdfPath
                    author;Eddie Campbell's Omnibox: The Complete ALEC and BACCHUS (3 Volume Slipcase);12.3;pdfPath
                    author;Far Arden;12.3;pdfPath
                    author;Fingerprints;12.3;pdfPath
                    author;Fox Bunny Funny;12.3;pdfPath
                    author;From Hell;12.3;pdfPath
                    author;God Is Disappointed / Apocrypha Now — SLIPCASE SET;12.3;pdfPath
                    author;God Is Disappointed in You;12.3;pdfPath
                    author;Hieronymus B.;12.3;pdfPath
                    author;Highwayman;12.3;pdfPath
                    author;Hutch Owen (Vol 1): The Collected by Tom Hart;12.3;pdfPath
                    author;Hutch Owen (Vol 2): Unmarketable by Tom Hart;12.3;pdfPath
                    author;Hutch Owen (Vol 3): Let's Get Furious!;12.3;pdfPath
                    author;Infinite Kung Fu;12.3;pdfPath
                    author;The King by Rich Koslowski;12.3;pdfPath
                    author;The League of Extraordinary Gentlemen (Vol III): Century #1 - 1910;12.3;pdfPath
                    author;The League of Extraordinary Gentlemen (Vol III): Century #2 - 1969;12.3;pdfPath
                    author;The League of Extraordinary Gentlemen (Vol III): Century #3 - 2009;12.3;pdfPath
                    author;The League of Extraordinary Gentlemen (Vol III): Century;12.3;pdfPath
                    author;Less Than Heroes by David Yurkovich;12.3;pdfPath
                    author;Liar's Kiss;12.3;pdfPath
                    author;Lone Racer by Nicolas Mahler;12.3;pdfPath
                    author;The Lovely Horrible Stuff;12.3;pdfPath
                    author;Lower Regions;12.3;pdfPath
                    author;Magic Boy and the Robot Elf by James Kochalka;12.3;pdfPath
                    author;Monkey Vs. Robot (Vol 2): Crystal of Power by Koch.;12.3;pdfPath
                    author;Monster on the Hill (Book 1);12.3;pdfPath
                    author;Mosquito by Dan James;12.3;pdfPath
                    author;Moving Pictures;12.3;pdfPath
                    author;Nate Powell's OMNIBOX;12.3;pdfPath
                    author;Okie Dokie Donuts (Story 1): Open for Business!;12.3;pdfPath
                    author;Pinky & Stinky by James Kochalka;12.3;pdfPath
                    author;Pirate Penguin vs Ninja Chicken (Book 1): Troublems with Frenemies;12.3;pdfPath
                    author;Pirate Penguin vs Ninja Chicken (Book 2): Escape from Skull-Fragment Island!;12.3;pdfPath
                    author;Return of the Dapper Men (Deluxe Edition);12.3;pdfPath
                    author;Scene But Not Heard;12.3;pdfPath
                    author;A Shining Beacon;12.3;pdfPath
                    author;Speechless;12.3;pdfPath
                    author;Spiral-Bound by Aaron Renier;12.3;pdfPath
                    author;Sulk (Vol 1): Bighead and Friends;12.3;pdfPath
                    author;Sulk (Vol 2): Deadly Awesome;12.3;pdfPath
                    author;Sulk (Vol 3): The Kind of Strength...;12.3;pdfPath
                    author;Super Spy;12.3;pdfPath
                    author;Super Spy (Vol 2): The Lost Dossiers;12.3;pdfPath
                    author;Swallow Me Whole;12.3;pdfPath
                    author;That Salty Air;12.3;pdfPath
                    author;They Called Us Enemy;12.3;pdfPath
                    author;Three Fingers by Rich Koslowski;12.3;pdfPath
                    author;Too Cool to Be Forgotten;12.3;pdfPath
                    author;The Underwater Welder;12.3;pdfPath
                    author;Upside Down (Book 1): A Vampire Tale;12.3;pdfPath
                    author;Upside Down (Book 2): A Hat Full of Spells;12.3;pdfPath
                    author;Will You Still Love Me If I Wet the Bed by Liz Prince;12.3;pdfPath
                    author;Ye;12.3;pdfPath
                    """;
}
