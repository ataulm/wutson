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
        Character oliver = new Character("Oliver Queen / Green Arrow", new Actor("Stephen Amell"));
        Character laurel = new Character("Laurel Lance / Black Canary", new Actor("Katie Cassidy"));
        Character roy = new Character("Roy Harper / Arsenal", new Actor("Colton Haynes"));
        Character diggle = new Character("John Diggle", new Actor("David Ramsey"));
        Character detective = new Character("Detective Quentin Lance", new Actor("Paul Blackthorne"));
        Character thea = new Character("Thea Queen", new Actor("Willa Holland"));

        return new Cast(Arrays.asList(oliver, laurel, roy, diggle, detective, thea));
    }

}
