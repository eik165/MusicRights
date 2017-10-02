package com.rr.grm;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MusicCatalogue {


    public static void main(String[] args) throws Exception {

        String command = "NA";

        System.out.println("Music Catalogue, start entring commands:\n ");

        startCatalogueCommandProcessor(System.in, System.out, "./src/main/resources/DistributionPartnersContracts.txt", "./src/main/resources/MusicContracts.txt");

    }

    private static void startCatalogueCommandProcessor(InputStream inStream, OutputStream stdOut,  String partnerContractFile, String MusicContractFile) throws Exception {

        System.setOut(new PrintStream(stdOut));
        Scanner scanIn = new Scanner(inStream);
        String command;
        String response = "";
        while (true) {

            if (scanIn.hasNext()) {
                command = scanIn.nextLine(); // Command Name

                executeCommand( partnerContractFile, MusicContractFile, command);
                System.out.println( " received command : "+command+" \r");

            }
        }
    }


    public static String executeCommand( String partnerContractFile, String MusicContractFile, String command) throws Exception {
        String[] commands;
        commands = command.split("\\s+", 2); // splits all the commands separated by one or more spaces


        String response = "none";
        if (commands[0].toUpperCase().equalsIgnoreCase("QUIT")) {

                System.exit(0);


                response = "QUIT command";

        } else
        {
//            System.out.println("******"+System.getProperty("user.dir"));


            String partnerMediaFormat = loadPartnerData(partnerContractFile, "\\|", commands[0]);

            List<List<String>> matchedAlbumRights = findAlbumRights(MusicContractFile, "\\|", partnerMediaFormat, commands[1]);//"1st May 2012");

            printMatchedAlbumRights(matchedAlbumRights);


        }

        //System.out.println("securities:" + observationCollection.toString());
        return response;
    }


    public static String loadPartnerData(String partnerContractFile, String fileSeprator, String partnerName) throws Exception {

        Path path = Paths.get(partnerContractFile);
        if (Files.exists(path)) {

            Stream<String> lines = Files.lines(path);
            List<String> columns = lines
                    .findFirst()
                    .map((line) -> Arrays.asList(line.split(fileSeprator)))
                    .get();

            int partnerIndex = columns.indexOf("Partner");
            int usageIndex = columns.indexOf("Usage");

            lines = Files.lines(path);

            Optional<String> partnerUsage = lines
                    .skip(1)
                    .map((line) -> Arrays.asList(line.split(fileSeprator)))
                    .filter(s -> s.get(partnerIndex).equals(partnerName))
                    .map(s -> s.get(usageIndex))
                    .findFirst();

            return partnerUsage.get();

        }
        else return "";
    }

    public static List<List<String>> findAlbumRights(String musicContractFile, String fileSeprator, String partnerSupportedMediaType , String dateParam) throws Exception {


            Path path2 = Paths.get(musicContractFile);
            if (Files.exists(path2)) {

                Stream<String> lines2 = Files.lines(path2);
                List<String> columns2 = lines2
                        .findFirst()
                        .map((line) -> Arrays.asList(line.split(fileSeprator, 5)))
                        .get();


                int startDateIndex = columns2.indexOf("StartDate");
                int endDateIndex = columns2.indexOf("EndDate");
                int usagesIndex = columns2.indexOf("Usages");

                lines2 = Files.lines(path2);

                List<List<String>> matchingAlbumRights = lines2
                        .skip(1)
                        .map((line) -> Arrays.asList(line.split(fileSeprator, 5)))
                        .filter(s -> s.get(usagesIndex).contains(partnerSupportedMediaType))
                        .filter(s -> UtilFormatter.isOnOrBeforeDate(dateParam, s.get(startDateIndex)))
                        .filter(
                                s -> UtilFormatter.isOnOrAfterDate(dateParam, s.get(endDateIndex)))
                        .map(s -> {s.set(2, partnerSupportedMediaType); return s;})
                        .collect(Collectors.toList());

                return matchingAlbumRights;

                //              values.forEach((l)->System.out.println(l));


            }

            else
                return null;
    }


    private static void printMatchedAlbumRights(List<List<String>> matchedAlbumRights) throws Exception {

        System.out.println("| "+ String.format("%-13s", "Artist")+"| "+String.format("%-24s","Title")+"| "+String.format("%-17s","Usages")+"| "+String.format("%-13s","StartDate")+"| "+String.format("%-13s","EndDate")+"|");
        matchedAlbumRights.stream().map(s-> "| "+ String.format("%-13s", s.get(0))+"| "+String.format("%-24s",s.get(1))+"| "+String.format("%-17s",s.get(2))+"| "+String.format("%-13s",s.get(3))+"| "+String.format("%-13s",s.get(4))+"|").sorted().forEach(System.out::println);

    }

}

 class UtilFormatter {
    public static boolean isNotNullOrEmpty (String str) {
        return str != null && !str.isEmpty() && (str.trim()).length()>0;
    }

    public static boolean isOnOrAfterDate( String dateParam, String startDate){
        if( startDate.isEmpty())
                return true;
        else
         return isNotNullOrEmpty(startDate) && (parseDate(startDate).isAfter(parseDate(dateParam)) || parseDate(startDate).equals(parseDate(dateParam)));
    }

     public static boolean isOnOrBeforeDate( String dateParam, String endDate){

         return isNotNullOrEmpty(endDate) && (parseDate(endDate).isBefore(parseDate(dateParam))|| parseDate(endDate).equals(parseDate(dateParam)));
     }

     public static DateTime parseDate(String dateParam){

         if (dateParam!= null && !(dateParam.trim()).isEmpty() ){

             String cleanedDate = dateParam.replaceFirst("^(\\d+).*? (\\w+ \\d+)", "$1 $2");
             DateTime dtf = DateTime.parse(cleanedDate, DateTimeFormat.forPattern("dd MMMM yyyy"));
             return dtf;

         }
         else
             return null;
     }
}