package com.ipn.mx.service.impl;

import com.ipn.mx.domain.entity.Menu;
import com.ipn.mx.domain.repository.MenuRepository;
import com.ipn.mx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Menu> readAll() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Menu read(Integer id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        menuRepository.deleteById(id);
    }

    @Override
    @Transactional
    public String saveWithImage(MultipartFile imagen, Menu menu) throws IOException {
        if (imagen.isEmpty()) {
            throw new IllegalArgumentException("La imagen está vacía.");
        }

        menu.setNombreImagen(imagen.getOriginalFilename());
        menu.setTipoImagen(imagen.getContentType());
        menu.setDatosImagen(imagen.getBytes());

        menuRepository.save(menu);
        return "Producto y su imagen almacenados correctamente.";
    }

}
