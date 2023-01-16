package pl.mgis.problemreport.logging;

import java.time.LocalDateTime;

public class DbEvents {
    long objectId;
    String table;
    String operation;
    String object;
    LocalDateTime dateTime = LocalDateTime.now();

    public DbEvents() {
    }

    public DbEvents(long objectId, String table, String operation, String object) {
        this.objectId = objectId;
        this.table = table;
        this.operation = operation;
        this.object = object;
    }
}
