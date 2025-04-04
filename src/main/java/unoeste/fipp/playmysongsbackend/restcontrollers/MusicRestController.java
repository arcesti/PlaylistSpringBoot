package unoeste.fipp.playmysongsbackend.restcontrollers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.playmysongsbackend.entities.Erro;
import unoeste.fipp.playmysongsbackend.entities.Musica;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "apis")
public class MusicRestController {
    @Autowired
    private HttpServletRequest request;
    private static final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";
    private String nomeArq;
    @PostMapping(value = "add-music")
    public ResponseEntity<Object> addUsuario(@RequestParam("file") MultipartFile file,
                                             @RequestParam("titulo") String titulo,
                                             @RequestParam("artista") String artista,
                                             @RequestParam("estilo") String estilo) {

        try {
            //cria uma pasta na área estática para acomodar os arquivos recebidos, caso não exista
            File uploadFolder = new File(UPLOAD_FOLDER);
            System.out.println(uploadFolder.getAbsolutePath());
            if (!uploadFolder.exists()) uploadFolder.mkdir();
            // Gerar um nome personalizado para o arquivo
            String extensao = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            titulo = titulo.replaceAll("_", "").replaceAll("\\s+", "");
            artista = artista.replaceAll("_", "").replaceAll("\\s+", "");
            estilo = estilo.replaceAll("_", "").replaceAll("\\s+", "");
            String novoNomeArquivo = titulo + "_" + artista + "_" + estilo + extensao;
            nomeArq = novoNomeArquivo;
            System.out.println(novoNomeArquivo);
            // Substituir espaços e caracteres indesejados para segurança
            // Substituir espaços e caracteres indesejados para segurança
            file.transferTo(new File(uploadFolder.getAbsolutePath() + "/" + novoNomeArquivo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao armazenar o arquivo. " + e.getMessage()));
        }
        return ResponseEntity.ok(new Musica(titulo, artista, estilo, file.getOriginalFilename()));
    }

    @GetMapping(value = "find-music")
    public ResponseEntity<Object> finMusics() {
        File uploadFolder = new File(UPLOAD_FOLDER);
        String[] files = uploadFolder.list();
        // CASO LISTA VAZIA, RETORNE ERRO
        List<Musica> musicaList = new ArrayList<>();
        for (String f : files) {
            StringBuilder titulo = new StringBuilder();
            StringBuilder artista = new StringBuilder();
            StringBuilder estilo = new StringBuilder();
            int cont = 0;

            for (int i = 0; i < f.length(); i++) {
                char c = f.charAt(i);

                if (cont == 0 && c != '_') {
                    titulo.append(c);  // Adiciona ao título
                } else if (cont == 1 && c != '_') {
                    artista.append(c);  // Adiciona ao artista
                } else if (cont == 2 && c != '_') {
                    estilo.append(c);  // Adiciona ao estilo
                }

                // Se encontrar um '_', muda para o próximo campo
                if (c == '_') {
                    cont++;
                }
            }

            // Criação da nova instância de Musica
            musicaList.add(new Musica(titulo.toString(), artista.toString(), estilo.toString(), getHostStatic() + f));

        }
        return ResponseEntity.ok(musicaList);
    }

    public String getHostStatic() {
        return "http://"+request.getServerName().toString()+":"+request.getServerPort()+"/uploads/";
    }
}
