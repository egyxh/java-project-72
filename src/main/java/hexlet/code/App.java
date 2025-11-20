package hexlet.code;

import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DatabaseUtil;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static UrlRepository urlRepository;

    public static void main(String[] args) {
        Javalin app = getApp();
        
        urlRepository = new UrlRepository(DatabaseUtil.getDataSource());

        int port = getPort();
        app.start(port);

        logger.info("Приложение успешно запущено на порту: {}", port);
        logger.info("База данных подключена: {}", DatabaseUtil.testConnection() ? "OK" : "FAILED");
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        app.get("/", ctx -> {
            ctx.result("Hello World");
        });

        app.get("/urls", ctx -> {
            try {
                var urls = urlRepository.findAll();
                ctx.json(urls);
            } catch (Exception e) {
                logger.error("Error getting URLs: {}", e.getMessage());
                ctx.status(500).result("Database error");
            }
        });

        app.post("/urls", ctx -> {
            var name = ctx.formParam("name");
            if (name == null || name.trim().isEmpty()) {
                ctx.status(400).result("Name is required");
                return;
            }

            try {
                var url = new hexlet.code.model.Url(name.trim());
                urlRepository.save(url);
                ctx.status(201).json(url);
            } catch (Exception e) {
                logger.error("Error saving URL: {}", e.getMessage());
                ctx.status(500).result("Database error");
            }
        });

        return app;
    }

    private static int getPort() {
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.isEmpty()) {
            try {
                return Integer.parseInt(portEnv);
            } catch (NumberFormatException e) {
                logger.warn("Invalid PORT environment variable: {}. Using default port 7070", portEnv);
            }
        }
        return 7070;
    }
}
