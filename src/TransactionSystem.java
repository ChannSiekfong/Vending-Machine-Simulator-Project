package src;
public class TransactionSystem {

    String[] logs;   // store text logs
    int count;       // current number of logs
    int maxLogs;     // max log storage

    public TransactionSystem(int maxLogs) {
        this.maxLogs = maxLogs;
        logs = new String[maxLogs];
        count = 0;
    }

    public void addLog(String message) {
        if (count >= maxLogs) {
            // overwrite oldest
            for (int i = 0; i < maxLogs - 1; i++) {
                logs[i] = logs[i + 1];
            }
            logs[maxLogs - 1] = message;
            return;
        }

        logs[count] = message;
        count = count + 1;
    }

    public void showLogs() {
        System.out.println("\n===== TRANSACTION RECORDS =====");
        if (count == 0) {
            System.out.println("No records yet.");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ") " + logs[i]);
        }
    }
}
