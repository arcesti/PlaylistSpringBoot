package unoeste.fipp.playmysongsbackend.restcontrollers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.playmysongsbackend.entities.Erro;
import unoeste.fipp.playmysongsbackend.entities.Musica;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "apis")
public class MusicRestController {
    @Autowired
    private HttpServletRequest request;
    private static final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";
    @PostMapping(value = "add-music")
    public ResponseEntity<Object> addUsuario(@RequestParam("file") MultipartFile file,
                                             @RequestParam("titulo") String titulo,
                                             @RequestParam("artista") String artista,
                                             @RequestParam("estilo") String estilo) {

        try {
            //cria uma pasta na área estática para acomodar os arquivos recebidos, caso não exista
            File uploadFolder = new File(UPLOAD_FOLDER);
            if (!uploadFolder.exists()) uploadFolder.mkdir();
            // criar um nome para o arquivo
            file.transferTo(new File(uploadFolder.getAbsolutePath() + "/"+file.getOriginalFilename()));
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
            musicaList.add(new Musica("titulo", "artista", "estilo", getHostStatic()+f));
        }
        return ResponseEntity.ok(musicaList);
    }

    public String getHostStatic() {
        return "http://"+request.getServerName().toString()+":"+request.getServerPort()+"/uploads/";
    }
}
