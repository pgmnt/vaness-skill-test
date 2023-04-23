// Create a small application to record the time in and time out from GPS and provide the summary page
// to show the attendance of students to go to class in the university.

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GPSAttendanceTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Long[]> attendanceMap = new HashMap<>();

        while (true) {
            System.out.println("Enter the student ID or 'exit' to quit:");
            String studentID = scanner.nextLine();

            if (studentID.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.println("Enter 'in' to record time in or 'out' to record time out:");
            String timeType = scanner.nextLine();

            if (!timeType.equalsIgnoreCase("in") && !timeType.equalsIgnoreCase("out")) {
                System.out.println("Invalid time type.");
                continue;
            }

            double[] gpsLocation = getGPSLocation();

            if (gpsLocation == null) {
                System.out.println("Failed to get GPS location.");
                continue;
            }

            Long[] attendance = attendanceMap.getOrDefault(studentID, new Long[4]);

            if (timeType.equalsIgnoreCase("in")) {
                attendance[0] = new Date().getTime();
                attendance[1] = (long)gpsLocation[0];
                attendance[2] = (long)gpsLocation[1];
                System.out.println("Time in and GPS location have been recorded for student ID " + studentID);
            } else {
                attendance[3] = new Date().getTime();
                System.out.println("Time out has been recorded for student ID " + studentID);
            }

            attendanceMap.put(studentID, attendance);
        }

        System.out.println("Attendance Summary:");
        System.out.println("Student ID\tTime In\t\tLatitude\tLongitude\tTime Out\t\tDuration");

        for (Map.Entry<String, Long[]> entry : attendanceMap.entrySet()) {
            String studentID = entry.getKey();
            Long[] attendance = entry.getValue();
            String timeInStr = attendance[0] != null ? new Date(attendance[0]).toString() : "-";
            String latitudeStr = attendance[1] != null ? String.format("%.6f", attendance[1] / 1e6) : "-";
            String longitudeStr = attendance[2] != null ? String.format("%.6f", attendance[2] / 1e6) : "-";
            String timeOutStr = attendance[3] != null ? new Date(attendance[3]).toString() : "-";
            String durationStr = "-";

            if (attendance[0] != null && attendance[3] != null) {
                long duration = attendance[3] - attendance[0];
                durationStr = String.format("%.2f minutes", duration / 60000.0);
            }

            System.out.println(studentID + "\t\t" + timeInStr + "\t" + latitudeStr + "\t" + longitudeStr + "\t" + timeOutStr + "\t" + durationStr);
        }
    }

    private static double[] getGPSLocation() {
        double latitude = 180;
        double longitude = 180;
        return new double[] {latitude, longitude};
    }
}