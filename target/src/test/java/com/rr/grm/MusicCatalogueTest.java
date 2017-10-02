package com.rr.grm;

import com.rr.grm.MusicCatalogue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MusicCatalogueTest {


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
     private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void out() {
        System.out.print("hello");
        assert("hello".equals(outContent.toString()));


    }
    @Test
    public void executeCommandYouTube27DecTest(){
        MusicCatalogue mc = new MusicCatalogue();
        try {

            //given
            MusicCatalogue.executeCommand("YouTube 27th Dec 2012", "./src/main/resources/DistributionPartnersContracts.txt", "./src/main/resources/MusicContracts.txt");

            //when execute command
            String response = outContent.toString();

            //then respose is
            assert(response.equals(
                    "| Artist       | Title                   | Usages           | StartDate    | EndDate      |\n" +
                    "| Monkey Claw  | Christmas Special       | streaming        | 25th Dec 2012| 31st Dec 2012|\n" +
                    "| Monkey Claw  | Iron Horse              | streaming        | 1st June 2012|              |\n" +
                    "| Monkey Claw  | Motor Mouth             | streaming        | 1st Mar 2011 |              |\n" +
                    "| Tinie Tempah | Frisky (Live from SoHo) | streaming        | 1st Feb 2012 |              |\n"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void executeCommandITunes1MarchTest(){
        MusicCatalogue mc = new MusicCatalogue();
        try {

            //given
            MusicCatalogue.executeCommand("ITunes 1st March 2012", "./src/main/resources/DistributionPartnersContracts.txt", "./src/main/resources/MusicContracts.txt");

            //when execute command
            String response = outContent.toString();

            //then response is
            assert(response.equals(
                            "| Artist       | Title                   | Usages           | StartDate    | EndDate      |\n" +
                            "| Monkey Claw  | Black Mountain          | digital download | 1st Feb 2012 |              |\n" +
                            "| Monkey Claw  | Motor Mouth             | digital download | 1st Mar 2011 |              |\n" +
                            "| Tinie Tempah | Frisky (Live from SoHo) | digital download | 1st Feb 2012 |              |\n" +
                            "| Tinie Tempah | Miami 2 Ibiza           | digital download | 1st Feb 2012 |              |\n"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void executeCommandITunes1AprilTest(){
        MusicCatalogue mc = new MusicCatalogue();
        try {

            MusicCatalogue.executeCommand("YouTube 1st April 2012", "./src/main/resources/DistributionPartnersContracts.txt", "./src/main/resources/MusicContracts.txt");
            String response = outContent.toString();
            assert(response.equals(
                            "| Artist       | Title                   | Usages           | StartDate    | EndDate      |\n" +
                            "| Monkey Claw  | Motor Mouth             | streaming        | 1st Mar 2011 |              |\n" +
                            "| Tinie Tempah | Frisky (Live from SoHo) | streaming        | 1st Feb 2012 |              |\n"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void startCatalogueCommandProcessor() throws Exception {
        //MusicCatalogue.startCatalogueCommandProcessor();
    }

    @Test
    public void loadPartnerData() throws Exception {

        assertEquals("digital download", MusicCatalogue.loadPartnerData("./src/main/resources/DistributionPartnersContracts.txt", "\\|","ITunes" ));
    }

    @Test
    public void findAlbumRights() throws Exception {

        List<String> rights1 = Arrays.asList("Tinie Tempah", "Frisky (Live from SoHo)", "streaming", "1st Feb 2012","");
        List<String> rights2 = Arrays.asList("Monkey Claw", "Motor Mouth", "streaming", "1st Mar 2011","");

        List<List<String>> expectedRights = Arrays.asList(rights1, rights2);
        assertEquals(expectedRights,MusicCatalogue.findAlbumRights( "./src/main/resources/MusicContracts.txt", "\\|", "streaming", "1st April 2012"));
    }


}