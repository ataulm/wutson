package com.ataulm.wutson.show;

import java.net.URI;
import java.util.Arrays;

public class DummyShowMaker {

    Show getDummyShow() {
        String name = "Arrow";
        String overview = "Arrow is an American action adventure television series that follows billionaire playboy Oliver Queen," +
                " portrayed by Stephen Amell, who, after five years of being stranded on a hostile island, returns home to fight crime" +
                " and corruption as a secret vigilante whose weapon of choice is a bow and arrows. Arrow will also feature appearances" +
                " by other DC Comics characters. To assist in promotion, a preview comic book was released to tie into the television series.";
        URI posterUri = URI.create("https://image.tmdb.org/t/p/w396/adVtiOQ5JoQR5HkyBHWfHiyNlv2.jpg");
        Cast cast = getArrowCast();

        return new Show(name, overview, posterUri, cast);
    }

    private Cast getArrowCast() {
        Character oliver = new Character("Oliver Queen / Green Arrow", new Actor("Stephen Amell", URI.create("https://image.tmdb.org/t/p/w185/kTe4RYfqFze7N6HX1HEnNfHBZ2w.jpg")));
        Character laurel = new Character("Laurel Lance / Black Canary", new Actor("Katie Cassidy", URI.create("https://image.tmdb.org/t/p/w185/lRhzzdmt29n8PDi63QX5MLPAUFz.jpg")));
        Character roy = new Character("Roy Harper / Arsenal", new Actor("Colton Haynes", URI.create("https://image.tmdb.org/t/p/w185/c5s8Nbl9SbBILyc8wQINkFzJgro.jpg")));
        Character diggle = new Character("John Diggle", new Actor("David Ramsey", URI.create("https://image.tmdb.org/t/p/w185/8ZbdY0yrA4GTg0g9LrrhzDHRRy.jpg")));
        Character detective = new Character("Detective Quentin Lance", new Actor("Paul Blackthorne", URI.create("https://image.tmdb.org/t/p/w185/cr11VQutjQCjPK8sq1ec08c04yw.jpg")));
        Character thea = new Character("Thea Queen", new Actor("Willa Holland", URI.create("https://image.tmdb.org/t/p/w185/wSn01hjcRFnamCrvhbUJyeMIbxo.jpg")));

        return new Cast(Arrays.asList(oliver, laurel, roy, diggle, detective, thea));
    }

}
