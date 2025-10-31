import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ServidorHttpSimples {
    public static void main(String[] args) throws Exception {
        // Cria servidor HTTP escutando na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        /* INICIO DO CÓDIGO */

        servidor.createContext("/", troca->{
            enviarArquivo(troca,"index.html", "text/html");
        });

        servidor.createContext("/sobre", troca->{
            enviarArquivo(troca,"sobre.html", "text/html");
        });

        servidor.createContext("/usuario", troca->{
            enviarArquivo(troca,"usuario.html", "text/html");
        });

        servidor.createContext("/prof", troca->{
            enviarArquivo(troca,"prof.html", "text/html");
        });

        servidor.createContext("/estilo.css", troca->{
            enviarArquivo(troca,"estilo.css", "text/css");
        });

        servidor.createContext("/login", troca->{
            String query = troca.getRequestURI().getQuery();
            String partes[] = query.split("&");

            String user,senha;
            user = partes[0].replaceAll("user=", "");
            senha = partes[1].replaceAll("senha=", "");
            String opcao=partes[2].replaceAll("opcoes=", "");



            if( senha.equals("alunoLaguna@42") && opcao.equals("aluno") ){
                System.out.println("Acesso Autorizado");
                troca.getResponseHeaders().set("location", "/usuario");
                troca.sendResponseHeaders(302,-1);

            }
            else if(senha.equals("Lagu31.10@") && opcao.equals("professor")){
                System.out.println("Acesso Autorizado");
                troca.getResponseHeaders().set("location", "/prof");
                troca.sendResponseHeaders(302,-1);

            }
            else {
                System.out.println("Acesso Negado");
            }



        });

        /* FIM DO CÓDIGO */

        servidor.start();
        System.out.println("Servidor rodando em http://localhost:8080/");
    }

    // Envia um arquivo (HTML ou CSS)
    private static void enviarArquivo(com.sun.net.httpserver.HttpExchange troca, String caminho, String tipo) throws IOException {
        File arquivo = new File("src/" + caminho);
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado: " + arquivo.getAbsolutePath());
        }
        byte[] bytes = Files.readAllBytes(arquivo.toPath());
        troca.getResponseHeaders().set("Content-Type", tipo + "; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Envia resposta HTML gerada no código
      
    private static void enviarTexto(com.sun.net.httpserver.HttpExchange troca, String texto) throws IOException {
        byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }
}
