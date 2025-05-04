package com.ipn.mx.service;

import com.ipn.mx.domain.entity.Menu;

import java.util.List;

public interface MenuService {
    public List<Menu> readAll();
    public Menu read(Integer id);
    public Menu save(Menu menu);
    public void delete(Integer id);
}
