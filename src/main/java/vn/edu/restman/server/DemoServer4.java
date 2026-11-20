package vn.edu.restman.server;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.nio.file.Files;
import java.nio.file.Path;

public final class DemoServer4 {
    private DemoServer4() { }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("restman.port", "8080"));
        String contextPath = System.getProperty("restman.contextPath", "/RestMan");
        Path war = Path.of("target", "RestMan.war").toAbsolutePath().normalize();
        if (!Files.isRegularFile(war)) {
            throw new IllegalStateException("Chua co target/RestMan.war. Hay chay mvn package truoc.");
        }
        Path base = Path.of("target", "embedded-tomcat").toAbsolutePath().normalize();
        Files.createDirectories(base);
        Files.createDirectories(base.resolve("webapps"));

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(base.toString());
        tomcat.setPort(port);
        tomcat.getConnector();
        Context context = tomcat.addWebapp(contextPath, war.toString());
        context.setParentClassLoader(DemoServer4.class.getClassLoader());
        tomcat.start();
        System.out.printf("RestMan dang chay tai http://127.0.0.1:%d%s/%n", port, contextPath);
        tomcat.getServer().await();
    }
}
