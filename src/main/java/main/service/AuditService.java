package main.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String FILE_PATH = "audit.csv";

    private AuditService(){}

    public static AuditService getInstance(){
        if(instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String actionName){
        try(FileWriter writer = new FileWriter(FILE_PATH, true)){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.append(actionName).append(",").append(now.format(formatter)).append("\n");
        }catch (IOException e){
            System.out.println("Eroare la scrierea in audit: " + e.getMessage());
        }
    }
}
