package com.marietton.WikiLearning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyon on 14/07/2017.
 */
@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Data {

    String name;
    // Liste des pages qui vont être présentées à l'utilisateur
    static List<Page> pageList = new ArrayList<>();
    static List<Integer> histoList = new ArrayList<>();
    static List<Etiquette> listEtiquette = new ArrayList<>();

    public Data() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    public static void fillIn(PageRepository pageRepository) {
        pageList = pageRepository.findAll();
    }

    public void calculerChemin(PageRepository pageRepository) {
        pageList = pageRepository.computeChemin(pageList);
    }

    public static List<Integer> getHistoList() {
        return histoList;
    }

    public void setHistoList(List<Integer> histoList) {
        Data.histoList = histoList;
    }

    public void recupererHisto() {

    }

    public static List<Etiquette> getListEtiquette() {
        return listEtiquette;
    }

    public void setListEtiquette(List<Etiquette> listEtiquette) {
        Data.listEtiquette = listEtiquette;
    }
}
