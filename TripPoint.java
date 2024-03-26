import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TripPoint {
    private int time;
    private double lat;
    private double lon;
    private static ArrayList<TripPoint> trip = new ArrayList<TripPoint>();

    public TripPoint(int time, double lat, double lon) {
        this.time = time;
        this.lat = lat;
        this.lon = lon;
    }

    public int getTime() {
        return time;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static ArrayList<TripPoint> getTrip() {
        return new ArrayList<>(trip);
    }

    public static double haversineDistance(TripPoint a, TripPoint b) {
        double earthRadius = 6371; // radius in kilometers
        double lat1 = Math.toRadians(a.getLat());
        double lon1 = Math.toRadians(a.getLon());
        double lat2 = Math.toRadians(b.getLat());
        double lon2 = Math.toRadians(b.getLon());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));

        return earthRadius * c;
    }
    
    public static double avgSpeed(TripPoint a, TripPoint b) {
        double distance = haversineDistance(a, b);
        double timeDifference = (b.getTime() - a.getTime()) / 60.0; 
        return distance / Math.abs(timeDifference); 
    }


    public static double totalTime() {
        int totalMinutes = 0;
        for (int i = 1; i < trip.size(); i++) {
            totalMinutes += trip.get(i).getTime() - trip.get(i - 1).getTime();
        }
        return totalMinutes / 60.0; 
    }

    public static double totalDistance() {
        double totalDistance = 0;
        for (int i = 1; i < trip.size(); i++) {
            totalDistance += haversineDistance(trip.get(i - 1), trip.get(i));
        }
        return totalDistance;
    }

    

    public static void readFile(String filename) {
        trip.clear(); 
        boolean dataLine = false; 
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (dataLine) {
                   
                    String[] parts = line.split(",");
                    int time = Integer.parseInt(parts[0]);
                    double lat = Double.parseDouble(parts[1]);
                    double lon = Double.parseDouble(parts[2]);
                    trip.add(new TripPoint(time, lat, lon));
                } else {
                    dataLine = true; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
       
        TripPoint.readFile("triplog.csv"); 

        double totalDistance = TripPoint.totalDistance();
        double totalTime = TripPoint.totalTime();
        System.out.println("Total Distance: " + totalDistance + " kilometers");
        System.out.println("Total Time: " + totalTime + " hours");
    }
    
}