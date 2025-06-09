import java.io.*;
import java.util.*;

public class MiniShell {
    private static File currentDir = new File(System.getProperty("user.dir"));

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to MiniShell! Type 'exit' to quit.");

        while (true) {
            try {
                System.out.print(currentDir.getAbsolutePath() + " $ ");
                String line = reader.readLine();
                if (line == null || line.trim().isEmpty()) continue;

                // Support command chaining separated by ';'
                String[] commands = line.split(";");
                for (String cmd : commands) {
                    if (!processCommand(cmd.trim())) {
                        System.out.println("Goodbye!");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean processCommand(String commandLine) throws Exception {
        if (commandLine.isEmpty()) return true;

        // Check for background execution
        boolean background = false;
        if (commandLine.endsWith("&")) {
            background = true;
            commandLine = commandLine.substring(0, commandLine.length() - 1).trim();
        }

        // Handle built-ins separately if no pipe or redirection
        if (!commandLine.contains("|") && !commandLine.contains(">")) {
            String[] parts = commandLine.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case "exit":
                    return false;

                case "pwd":
                    System.out.println(currentDir.getAbsolutePath());
                    return true;

                case "cd":
                    if (parts.length < 2) {
                        System.out.println("cd: missing argument");
                    } else {
                        changeDirectory(parts[1]);
                    }
                    return true;

                case "ls":
                    listFiles();
                    return true;
            }
        }

        // Handle piping and redirection
        executeComplexCommand(commandLine, background);

        return true;
    }

    private static void changeDirectory(String path) {
        File newDir = path.equals("~") ? new File(System.getProperty("user.home")) :
                new File(currentDir, path);
        if (newDir.exists() && newDir.isDirectory()) {
            try {
                currentDir = newDir.getCanonicalFile();
            } catch (Exception e) {
                System.out.println("cd: error: " + e.getMessage());
            }
        } else {
            System.out.println("cd: no such directory: " + path);
        }
    }

    private static void listFiles() {
        File[] files = currentDir.listFiles();
        if (files == null) {
            System.out.println("ls: cannot access directory");
            return;
        }
        Arrays.stream(files).forEach(f -> {
            System.out.println((f.isDirectory() ? "[DIR] " : "      ") + f.getName());
        });
    }

    private static void executeComplexCommand(String commandLine, boolean background) throws Exception {
        // Parse output redirection
        String outputFile = null;
        boolean append = false;

        String cmdPart = commandLine;
        if (commandLine.contains(">")) {
            int redirectIndex = commandLine.indexOf(">");
            cmdPart = commandLine.substring(0, redirectIndex).trim();
            String redirectionPart = commandLine.substring(redirectIndex).trim();

            if (redirectionPart.startsWith(">>")) {
                append = true;
                outputFile = redirectionPart.substring(2).trim();
            } else if (redirectionPart.startsWith(">")) {
                append = false;
                outputFile = redirectionPart.substring(1).trim();
            }
        }

        // Split by pipe
        String[] pipeCommands = cmdPart.split("\\|");
        List<ProcessBuilder> pbs = new ArrayList<>();

        for (String pc : pipeCommands) {
            pc = pc.trim();
            List<String> parts = new ArrayList<>(Arrays.asList(pc.split("\\s+")));

            // Handle built-ins in pipe is tricky; here we try external commands only
            pbs.add(new ProcessBuilder(parts).directory(currentDir));
        }

        // Connect processes with piping
        List<Process> processes = new ArrayList<>();
        Process prev = null;
        for (int i = 0; i < pbs.size(); i++) {
            ProcessBuilder pb = pbs.get(i);
            pb.redirectErrorStream(true);

            if (i > 0) {
                pb.redirectInput(ProcessBuilder.Redirect.PIPE);
            }
            if (i < pbs.size() - 1) {
                pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
            }

            Process proc = pb.start();

            if (prev != null) {
                // Connect output of prev to input of current
                InputStream prevOut = prev.getInputStream();
                OutputStream currIn = proc.getOutputStream();

                pipeStreams(prevOut, currIn);
            }

            processes.add(proc);
            prev = proc;
        }

        // Handle output redirection for last process
        Process last = processes.get(processes.size() - 1);

        InputStream lastOut = last.getInputStream();
        if (outputFile != null) {
            try (OutputStream fileOut = new FileOutputStream(new File(currentDir, outputFile), append)) {
                pipeStreams(lastOut, fileOut);
            }
        } else {
            // Print output to console
            try (BufferedReader br = new BufferedReader(new InputStreamReader(lastOut))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }

        // Close input of last to signal end
        last.getOutputStream().close();

        // Wait for all processes unless background
        if (!background) {
            for (Process proc : processes) {
                proc.waitFor();
            }
        } else {
            System.out.println("Process running in background...");
        }
    }

    private static void pipeStreams(InputStream input, OutputStream output) {
        new Thread(() -> {
            try (InputStream in = input; OutputStream out = output) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
            } catch (IOException e) {
                // Ignore exceptions on pipe closing
            }
        }).start();
    }
}
