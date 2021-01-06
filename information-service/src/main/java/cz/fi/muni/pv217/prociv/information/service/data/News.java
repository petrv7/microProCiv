package cz.fi.muni.pv217.prociv.information.service.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;

import javax.persistence.Entity;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;

@Entity
public class News extends PanacheEntity {
    public String news;
    public LocalDate date;

    public static News getLatestNews() {
        List<News> news = News.listAll(Sort.by("date").descending());
        if(news.isEmpty())
            throw new NotFoundException();
        return news.get(0);
    }
}