package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Menu;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MenuService {
    List<Menu> readAll();
    Menu read(Integer id);
    Menu save(Menu menu);
    void delete(Integer id);

    // Nuevo método para guardar producto con imagen
    String saveWithImage(MultipartFile imagen, Menu menu) throws IOException;
}
