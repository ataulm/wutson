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
        Show.Cast cast = getArrowCast();

        return new Show(name, overview, posterUri, cast);
    }

    private Show.Cast getArrowCast() {
        Show.Character oliver = new Show.Character("Oliver Queen / Green Arrow", new Show.Actor("Stephen Amell"));
        Show.Character laurel = new Show.Character("Laurel Lance / Black Canary", new Show.Actor("Katie Cassidy"));
        Show.Character roy = new Show.Character("Roy Harper / Arsenal", new Show.Actor("Colton Haynes"));
        Show.Character diggle = new Show.Character("John Diggle", new Show.Actor("David Ramsey"));
        Show.Character detective = new Show.Character("Detective Quentin Lance", new Show.Actor("Paul Blackthorne"));
        Show.Character thea = new Show.Character("Thea Queen", new Show.Actor("Willa Holland"));

        return new Show.Cast(Arrays.asList(oliver, laurel, roy, diggle, detective, thea));
    }

}
